package com.example.dcskeyboardhelper.model.socket;

import com.example.dcskeyboardhelper.model.bean.Key;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class KeyCodes {
    //最上面一排Fx的按钮
    public static final int VK_F1 = 112;

    public static final int VK_F2 = 113;

    public static final int VK_F3 = 114;

    public static final int VK_F4 = 115;

    public static final int VK_F5 = 116;

    public static final int VK_F6 = 117;

    public static final int VK_F7 = 118;

    public static final int VK_F8 = 119;

    public static final int VK_F9 = 120;

    public static final int VK_F10 = 121;

    public static final int VK_F11 = 122;

    public static final int VK_F12 = 123;

    //键盘0~9
    public static final int VK_0 = 48;

    public static final int VK_1 = 49;

    public static final int VK_2 = 50;

    public static final int VK_3 = 51;

    public static final int VK_4 = 52;

    public static final int VK_5 = 53;

    public static final int VK_6 = 54;

    public static final int VK_7 = 55;

    public static final int VK_8 = 56;

    public static final int VK_9 = 57;

    //箭头
    public static final int VK_UP = 22;

    public static final int VK_DOWN = 24;

    public static final int VK_LEFT = 25;

    public static final int VK_RIGHT = 26;

    //功能键
    public static final int VK_LEFT_CONTROL = 17;

    public static final int VK_RIGHT_CONTROL = 0x020D;//实际上是CONTEXT_MENU,但在我电脑上是右ctrl + fn按出来的

    public static final int VK_WINDOWS = 29;

    public static final int VK_LEFT_ALT = 18;

    public static final int VK_RIGHT_ALT = 0xFF7E;

    public static final int VK_LEFT_SHIFT = 16;

    public static final int VK_TAB = 9;

    public static final int VK_CAPS = 20;

    public static final int VK_ESC = 27;

    public static final int VK_BACKSPACE = 8;

    public static final int VK_ENTER = 13;

    public static final int VK_SPACE = 32;

    //小键盘上的一些键位
    public static final int VK_INSERT = 46;

    public static final int VK_DELETE = 45;

    public static final int VK_PAGE_UP = 33;

    public static final int VK_PAGE_DOWN = 34;

    public static final int VK_HOME = 36;

    public static final int VK_END = 35;

    //A-Z
    public static final int VK_A = 65;

    public static final int VK_B = 66;

    public static final int VK_C = 67;

    public static final int VK_D = 68;

    public static final int VK_E = 69;

    public static final int VK_F = 70;

    public static final int VK_G = 71;

    public static final int VK_H = 72;

    public static final int VK_I = 73;

    public static final int VK_J = 74;

    public static final int VK_K = 75;

    public static final int VK_L = 76;

    public static final int VK_M = 77;

    public static final int VK_N = 78;

    public static final int VK_O = 79;

    public static final int VK_P = 80;

    public static final int VK_Q = 81;

    public static final int VK_R = 82;

    public static final int VK_S = 83;

    public static final int VK_T = 84;

    public static final int VK_U = 85;

    public static final int VK_V = 86;

    public static final int VK_W = 87;

    public static final int VK_X = 88;

    public static final int VK_Y = 89;

    public static final int VK_Z = 90;

    //一些符号
    public static final int VK_COMMA = 44;//逗号 " , "

    public static final int VK_PERIOD = 46;//句号 " . "

    public static final int VK_SEMICOLON = 59;//分号 “ ； ”

    public static final int VK_SLASH = 47;//问号 “ ？ ”

    public static final int VK_OPEN_BRACKET = 91; //左方括号 " [ "

    public static final int VK_BACK_SLASH = 92; //竖线 “ | ”

    public static final int VK_CLOSE_BRACKET = 93; //右方括号 " ] "

    //获取键盘名及其KeyCode
    public static List<Key> getKeys(){
        Class clazz;
        try {
            clazz = Class.forName("com.example.dcskeyboardhelper.model.socket.KeyCodes");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        List<Key> keys = new ArrayList<>();
        Field[] fields = clazz.getFields();//反射获取变量名及其值
        for (Field field : fields){
            try {
                String name = field.getName();
                int code = field.getInt(0);
                keys.add(new Key(name, code));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                break;
            }
        }
        return keys;
    }

    //根据KeyCode查找变量名
    public static String getKeyNameByValue(int code){
        try {
            Class clazz = Class.forName("com.example.dcskeyboardhelper.model.socket.KeyCodes");
            Field[] fields = clazz.getFields();//反射获取变量名及其值
            for (Field field : fields){
                if (field.getInt(0) == code){
                    return field.getName();
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
