package com.kenai.huafu.DataTrasmation;

/**
 * Created by zhang on 2017/9/29.
 */

public class DevAlarmInfo {
    private String DevID; //信息ID
    private String AlarmInfo;   //信息标题
    private String AlarmLevel; //详细信息
    private int avatar; //图片ID

    private int AlarmID;

    public void setAlarmID(int id){this.AlarmID = id;};
    public int getAlarmID(){return this.AlarmID;}

    //信息ID处理函数
    public void setDevID(String id) {
        this.DevID = id;
    }
    public String getDevID() {
        return DevID;
    }
    //标题
    public void setAlarmInfo(String AlarmInfo) {
        this.AlarmInfo = AlarmInfo;
    }
    public String getAlarmInfo() {
        return AlarmInfo;
    }

    //详细信息
    public void setAlarmLevel(String AlarmLevel) {
        this.AlarmLevel = AlarmLevel;
    }
    public String getAlarmLevel() {
        return AlarmLevel;
    }

    //图片
    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
    public int getAvatar() {
        return avatar;
    }
}
