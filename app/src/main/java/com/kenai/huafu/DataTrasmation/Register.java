package com.kenai.huafu.DataTrasmation;

/**
 * Created by zhang on 2017/9/18.
 */

public class Register {

    public String PhoneNumber;
    public String Password;
    public String Yzm;

    public String getPhoneNumber(){
        return PhoneNumber;
    }

    public void setPhoneNumber(String pNumber){
        PhoneNumber = pNumber;
    }

    public String getPasswordr(){
        return Password;
    }

    public void setPassword(String pwd){
        Password = pwd;
    }

    public String getYzm(){
        return Yzm;
    }

    public void setYzm(String yzms){
        Yzm =yzms;
    }
}
