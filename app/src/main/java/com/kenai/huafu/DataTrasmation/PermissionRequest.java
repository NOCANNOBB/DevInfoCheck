package com.kenai.huafu.DataTrasmation;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017/8/11.
 */

public class PermissionRequest {

    private List<String> mPerminssionList = new ArrayList<>();
    public boolean RequestPermission(Context ctx, String[] Perminssions, Activity PermisAct)
    {
        mPerminssionList.clear();
        for (int i = 0; i < Perminssions.length; i++) {
            if (ContextCompat.checkSelfPermission(ctx, Perminssions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPerminssionList.add(Perminssions[i]);
            }
        }

        if (mPerminssionList.isEmpty()) {//未授予的权限为空，表示都授予了
            return  true;
        } else {//请求权限方法
            String[] permissions = mPerminssionList.toArray(new String[mPerminssionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(PermisAct, permissions, 1);
            return  true;
        }

    }


}
