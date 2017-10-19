package com.kenai.huafu.DataTrasmation;

import java.io.Serializable;

/**
 * Created by zhang on 2017/10/18.
 */

public class MapDevDataInfo {
    private static final long serialVersionUID = -758459502806858418L;


    private DevAlarm mAlarmInfo;
    public void setDevAlarmInfo(DevAlarm devAlarmInfo){
        this.mAlarmInfo = devAlarmInfo;
    }
    public DevAlarm getDevAlarmInfo(){
        return this.mAlarmInfo;
    }

    private DevDataInfo mDevData;
    public void setDevDataInfo(DevDataInfo devDataInfo){
        this.mDevData = devDataInfo;
    }
    public DevDataInfo getDevDataInfo(){
        return this.mDevData;
    }

    private double mJingdu;
    public void setJingdu(double jingdu){
        this.mJingdu = jingdu;
    }
    public double getJingdu(){
        return this.mJingdu;
    }
    private double mWeidu;
    public void setWeidu(double weidu){
        this.mWeidu = weidu;
    }
    public double getWeidu(){
        return this.mWeidu;
    }




}
