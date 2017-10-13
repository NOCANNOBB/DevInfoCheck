package com.kenai.huafu.DataTrasmation;

import com.google.gson.Gson;

/**
 * Created by zhang on 2017/9/18.
 */

public class Serlize {
    public static String ObjectToJsonStr(Register reg){

        Gson gson =new Gson();
        String JsonStr = gson.toJson(reg);
        return JsonStr;
    }

    public static Register ObjectToJsonStr(String JsonStr){

        Gson gson =new Gson();
        Register reg = gson.fromJson(JsonStr, Register.class);
        return reg;
    }
}
