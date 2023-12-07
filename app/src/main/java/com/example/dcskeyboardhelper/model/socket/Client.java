package com.example.dcskeyboardhelper.model.socket;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.dcskeyboardhelper.model.socket.json.BaseJson;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class Client {
    private String serverIp;
    private int port;
    private Socket socket;
    private OutputStream os;
    private final MutableLiveData<Integer> connectionStatus;
    private long lastSendTime = -1;//上一次发送消息的时间
    private boolean connected = false;//与服务端的连接状态（是否已经连接)

    private final String endIcon = "\n";//数据传输终止符

    private Thread keepAliveWatchDog, initThread;

    //消息类型
    public static final int TYPE_KEEP_ALIVE = 0;//保活消息
    public static final int TYPE_ACTION = 1;//用户操作消息

    //客户端状态码
    public static final int CODE_TERMINATED = 32;//客户端已销毁Socket

    //网络连接状态
    public static final int SERVER_CONNECT_FAILED = -1;//连接失败
    public static final int NO_CONNECTED = 0;//无连接
    public static final int SERVER_CONNECTED = 1;//连接成功
    public static final int SERVER_CONNECTING = 2;//正在连接
    public static final int STATUS_UNDEFINE = 3;//其它情况或连接状态无变化，仅作占位符用

    private static volatile Client client;

    //单例模式创建
    public static Client getInstance(MutableLiveData<Integer> connectionStatus){
        if (client == null){
            synchronized (Client.class){
                if (client == null){
                    client = new Client(connectionStatus);
                }else {
                    client.destroy();
                }
            }
        } else {
            client.destroy();
        }
        return client;
    }

    private Client(MutableLiveData<Integer> connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public void useDefaultConfig(){
        setNetworkAttr("10.158.205.24", 1688);
    }

    public void setNetworkAttr(String serverIp, int port){
        this.serverIp = serverIp;
        this.port = port;
    }

    //初始化socket和io
    public void init() {
        if (socket != null){//如果之前启动过Client，则把之前的socket，io和线程关了
            destroy();
        }
        initThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("MainActivity", "init: 开始连接，ip" + serverIp + " 端口：" + port);
                    socket = new Socket(serverIp, port);
                    socket.setSoTimeout(30000);
                    os = socket.getOutputStream();

                    //通知livedata连接状态
                    if (socket != null && os != null){
                        connectionStatus.postValue(SERVER_CONNECTED);
                        connected = true;
                        lastSendTime = System.currentTimeMillis();
                        //开启检查连接的线程
                        keepAliveWatchDog = new Thread(new KeepAliveWatchDog());
                        keepAliveWatchDog.start();
                    }else {
                        connectionStatus.postValue(SERVER_CONNECT_FAILED);
                    }
                    try {
                        debug();
                    } catch (IOException e) {
                        e.printStackTrace();
                        disconnect();
                        destroy();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w("MainActivity", "init: 连接超时");
                    connectionStatus.postValue(SERVER_CONNECT_FAILED);
                    destroy();
                }
            }
        });
        initThread.start();
        connectionStatus.postValue(SERVER_CONNECTING);
    }

    public void disconnect(){
        if (connected){
            connected = false;
            connectionStatus.postValue(NO_CONNECTED);
        }
    }

    public void reconnect(){
        if (!connected){
            connectionStatus.postValue(SERVER_CONNECTING);
            init();
        }
    }

    public void destroy(){
        try {
            Thread.sleep(200);
            //进行销毁socket处理
            disconnect();
            keepAliveWatchDog.interrupt();
            initThread.interrupt();
            os.flush();
            os.close();
            if (socket == null){
                return;
            }
            socket.close();//断网了这个socket直接没了抛出空指针
            socket = null;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            socket = null;
        }
    }

    public void debug() throws IOException {
        BaseJson<Object> debug = new BaseJson<>(TYPE_KEEP_ALIVE);
        debug.setErrorMsg("debug");
        sendMessage(debug);
    }

    //向服务端发送消息
    public void sendMessage(String message) throws IOException {
        message += endIcon;//补充一个数据传输终止符
        new Thread(new MessageSender(message.getBytes())).start();
        Log.d("MainActivity","Submit Message: " + message);
    }

    //向服务端发送消息（传入一个对象）
    public void sendMessage(BaseJson baseJson) throws IOException {
        Gson gson = new Gson();
        String message = gson.toJson(baseJson);
        sendMessage(message);
    }

    private class MessageSender implements Runnable{
        private byte[] bytes;

        public MessageSender(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public void run() {
            synchronized (this){
                if (connected){
                    try {
                        os.write(bytes);
                        Log.d("MainActivity","Message Send: " + Arrays.toString(bytes));
                        lastSendTime = System.currentTimeMillis();//发送消息后刷新上一次发送消息的时间
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.w("MainActivity", "IOException");
                        disconnect();
                        reconnect();//发送失败时尝试进行一次重连
                    }
                }
            }
        }
    }

    private class KeepAliveWatchDog implements Runnable{
        private final long checkDelay = 2048;//进行检查的间隔
        private final long keepAliveDelay = 4096;//两次向服务端发送消息的间隔，如果超过这个间隔没有发送消息，则会强制发送一条检查消息
        @Override
        public void run() {
            while (connected){
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastSendTime >= keepAliveDelay){
                    try {
                        sendMessage(new BaseJson<>(TYPE_KEEP_ALIVE));//如果服务端关闭或者连接断了这里会直接抛出异常的，通知客户端进入断开连接状态
                    }catch (IOException e){
                        e.printStackTrace();
                        //如果发送遇到异常，则断开连接
                        Log.d("MainActivity", "sendMsgError");
                        disconnect();
                        reconnect();//尝试进行重新连接
                    }
                }
                try {
                    Thread.sleep(checkDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("MainActivity", "ThreadInterrupted");
                    disconnect();//线程出错就直接断连，不进行重新连接操作
                }
            }
        }
    }

    public MutableLiveData<Integer> getConnectionStatus() {
        return connectionStatus;
    }

    public boolean isConnected() {
        return connected;
    }
}
