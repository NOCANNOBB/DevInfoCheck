package com.kenai.huafu.DataTrasmation;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhang on 2017/10/12.
 */

public class Define {
    public static boolean g_IsDevDataShowBackgroundRun = false;
    public static boolean g_IsDataGetThreadStop = false;

    public static List<DevAlarmInfo> g_DevAlarmInfo = new ArrayList<DevAlarmInfo>();

    public static Lock lock = new ReentrantLock();


}
