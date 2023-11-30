package com.example.dcskeyboardhelper.model;

public class Constant {
    //用于标记Module的切换步骤模式
    public static final int STEP = 101;
    public static final int LOOP = 102;

    public static final String DEBUG_MODE = "Debug Mode";
    public static final String SIMULATION_MODE = "Simulation Mode";

    //用于标记是创建还是更新操作
    public static final String UPDATE = "UPDATE";
    public static final String CREATE = "CREATE";

    //当发生room数据更新回调（Livedata的observer等）时，根据这些标签来标识刚刚对数据库进行的操作
    public static final int ROOM_UNDEFINE = 0;//下面情况以外的状况
    public static final int ROOM_SINGLE_UPDATE = 1;
    public static final int ROOM_SINGLE_REMOVE = 2;
    public static final int ROOM_SINGLE_INSERT = 3;
    public static final int ROOM_SWAP = 4;

    public static int CURRENT_ROOM_ACTION = -1;//用于标记数据库更新操作

    public static long CURRENT_PROFILE_ID = 0;//当前正在使用的存档id

    public static long CURRENT_PAGE_ID = 0;//当前正在展示的页面id
}
