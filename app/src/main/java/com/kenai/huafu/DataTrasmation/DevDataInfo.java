package com.kenai.huafu.DataTrasmation;

/**
 * Created by zhang on 2017/10/11.
 */

public class DevDataInfo {

    private static final long serialVersionUID = -758459502806858416L;

    private ServerDevInfo mDevInfo; //信息ID
    private String mWENDU;   //信息标题
    private String mDIANLIU; //详细信息
    private String mDIANYA;

    //信息ID处理函数
    public void setServerDevInfo(ServerDevInfo serverDevInfo) {
        this.mDevInfo = serverDevInfo;
    }
    public ServerDevInfo getServerDevInfo() {
        return mDevInfo;
    }
    //标题
    public void setWENDU(String wendu) {
        this.mWENDU = wendu;
    }
    public String getWENDU() {
        return mWENDU;
    }

    //详细信息
    public void setDIANLIU(String dianliu) {
        this.mDIANLIU = dianliu;
    }
    public String getDIANLIU() {
        return mDIANLIU;
    }

    public void setDIANYA(String dianya) {
        this.mDIANYA = dianya;
    }
    public String getDIANYA() {
        return mDIANYA;
    }

}
