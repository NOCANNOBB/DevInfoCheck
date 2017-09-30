package com.kenai.huafu.devinfocheck;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.kenai.huafu.DataTrasmation.ClsBaiduMap;
import com.kenai.huafu.DataTrasmation.DevInfo;
import com.kenai.huafu.DataTrasmation.InfoWindowHolder;

/**
 * Created by zhang on 2017/9/29.
 */

public class MapShowFragment extends Fragment {
    private MapView mMapView;
    private RelativeLayout mMarkerInfoLy;
    private LinearLayout baidumap_infowindow;
    private ClsBaiduMap  mbaiduMap;

    public Context appContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.devmapshow_frame,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        mbaiduMap = new ClsBaiduMap(getContext());
        mbaiduMap.DoInitlization();//地图初始化

        mMapView = (MapView)getActivity().findViewById(R.id.baidumap);
        mbaiduMap.DoClsInit(mMapView);//地图信息初始化

        DevInfo dinfo = new DevInfo();
        dinfo.setId("10001");
        dinfo.setDevDIANLIU("10A");
        dinfo.setDevDIANYA("200V");
        dinfo.setDevWENDU("-4℃");


        mMarkerInfoLy = (RelativeLayout)getActivity().findViewById(R.id.id_marker_info);
        baidumap_infowindow = (LinearLayout) LayoutInflater.from (getContext()).inflate (R.layout.baidumap_infowindow, null);
        //116.386446,39.922018
        mbaiduMap.SetCenter(116.386446,39.922018);

        int markerid = R.drawable.mapmark;

        mbaiduMap.AddPoint(39.922018,116.386446,markerid,dinfo);



        mbaiduMap.initMarkerClickEvent(baidumap_infowindow);
        //qqweqqwewww
    }
}
