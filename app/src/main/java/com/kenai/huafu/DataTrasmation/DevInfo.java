package com.kenai.huafu.DataTrasmation;

import java.io.Serializable;

/**
 * Created by zhang on 2017/9/28.
 */

public class DevInfo implements Serializable {

    private static final long serialVersionUID = -758459502806858414L;

    private String DevID; //信息ID
    private String title;   //信息标题
    private String DevWENDU; //详细信息
    private String DevDIANLIU; //详细信息
    private String DevDIANYA; //详细信息
    private String UpTime;
    private int avatar; //图片ID



    //信息ID处理函数
    public void setId(String id) {
        this.DevID = id;
    }
    public String getId() {
        return DevID;
    }
    //标题
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    //详细信息
    public void setDevWENDU(String wendu) {
        this.DevWENDU = wendu;
    }
    public String getDevWENDU() {
        return DevWENDU;
    }

    //详细信息
    public void setDevDIANLIU(String DIANLIU) {
        this.DevDIANLIU = DIANLIU;
    }
    public String getDevDIANLIU() {
        return DevDIANLIU;
    }

    //详细信息
    public void setDevDIANYA(String DIANYA) {
        this.DevDIANYA = DIANYA;
    }
    public String getDevDIANYA() {
        return DevDIANYA;
    }


    //图片
    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
    public int getAvatar() {
        return avatar;
    }


    public void setUpTime(String upTime){
        this.UpTime = upTime;
    }
    public String getUpTime(){
        return this.UpTime;
    }
}
