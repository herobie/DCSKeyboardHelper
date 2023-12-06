package com.example.dcskeyboardhelper.model;

public class Constant {
    //用于标记Module的切换步骤模式
    public static final int STEP = 101;//步增、步减
    public static final int LOOP = 102;//循环
    public static final int CLICK = 103;//单点

    public static final String DEBUG_MODE = "Debug Mode";
    public static final String SIMULATION_MODE = "Simulation Mode";

    //用于标记是创建还是更新操作
    public static final String UPDATE = "UPDATE";
    public static final String CREATE = "CREATE";

    //Broadcast的滤波名
    public static final String BROADCAST_CONNECT_STATUS_CHANGE = "Connect Status Change";//连接情况改变时的广播
    public static final String BROADCAST_SEND_MESSAGE = "Send Message";//请求发送数据时的广播

    //intent传值的变量名
    public static final String CONNECT_STATUS = "Connect Status";//传递连接情况时调用
    public static final String MESSAGE = "Message";//传递需要发送的消息时调用

    public static long CURRENT_PROFILE_ID = -1;//当前正在使用的存档id

    public static long CURRENT_PAGE_ID = -1;//当前正在展示的页面id
}
