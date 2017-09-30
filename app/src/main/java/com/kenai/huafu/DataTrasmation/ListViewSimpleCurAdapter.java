package com.kenai.huafu.DataTrasmation;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2017/9/28.
 */

public class ListViewSimpleCurAdapter extends SimpleAdapter {
    public ListViewSimpleCurAdapter(Context context,List<Map<String,Object>> rlist,int layout, String[] from, int[] to)
    {
        /*rlist = new ArrayList<Map<String,Object>>();
        for(int i = 0; i < mList.size(); i++){
            DevInfo dinfo = mList.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title",dinfo.getTitle());
            String strText = "电流：" + dinfo.getDevDIANLIU() + "  电压：" + dinfo.getDevDIANYA()+ "  温度：" + dinfo.getDevWENDU();
            map.put("info",strText);
            map.put("img",dinfo.getAvatar());
            rlist.add(map);
        }
        * */
        super(context,rlist,layout,from,to);
    }

}
