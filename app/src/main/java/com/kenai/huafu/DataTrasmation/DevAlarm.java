package com.kenai.huafu.DataTrasmation;

/**
 * Created by zhang on 2017/9/29.
 */

public class DevAlarm {

    private static final long serialVersionUID = -758459502806858417L;

    private String DevID; //信息ID
    private String AlarmInfo;   //信息标题
    private double AlarmValue; //详细信息
    private double NowValue;
    private String AlarmTime;
    private String ConFrimTime;
    private String HuiFuTime;
    private String AlarmType;
    // private int avatar; //图片ID

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
    public void setAlarmValue(double avalue) {
        this.AlarmValue = avalue;
    }
    public double getAlarmValue() {
        return AlarmValue;
    }

    //图片
    public void setNowValue(double nValue) {
        this.NowValue = nValue;
    }
    public double getNowValue() {
        return NowValue;
    }

    public void setAlarmTime(String Atime) {
        this.AlarmTime = Atime;
    }
    public String getAlarmTime(){
        return  this.AlarmTime;
    }
    public void setConFrimTime(String CTime) {
        this.ConFrimTime = CTime;
    }
    public String getConFrimTime(){
        return  this.ConFrimTime;
    }

    public void setHuiFuTime(String HuiFuTime) {
        this.HuiFuTime = HuiFuTime;
    }
    public String getHuiFuTime(){
        return  this.HuiFuTime;
    }

    public void setAlarmType(String AlarmType) {
        this.AlarmType = AlarmType;
    }
    public String getAlarmType(){
        return  this.AlarmType;
    }

}
