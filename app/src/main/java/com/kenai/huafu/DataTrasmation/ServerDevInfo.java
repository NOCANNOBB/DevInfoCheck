package com.kenai.huafu.DataTrasmation;

/**
 * Created by zhang on 2017/10/11.
 */

public class ServerDevInfo {
    private static final long serialVersionUID = -758459502806858415L;

    private String mDevID; //信息ID
    private String mDevName;   //信息标题
    private String mDevType; //详细信息

    //信息ID处理函数
    public void setDevID(String id) {
        this.mDevID = id;
    }
    public String getDevID() {
        return mDevID;
    }
    //标题
    public void setDevName(String devName) {
        this.mDevName = devName;
    }
    public String getDevName() {
        return mDevName;
    }

    //详细信息
    public void setDevType(String devType) {
        this.mDevType = devType;
    }
    public String getDevWENDU() {
        return mDevType;
    }
}
