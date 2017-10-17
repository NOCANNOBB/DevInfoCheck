package com.kenai.huafu.devinfocheck;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
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
    private TextView mwdShow;

    public Context appContext;

    private Container.MyOnTouchListener mTouchListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        mTouchListener = new Container.MyOnTouchListener() {

            @Override
            public boolean onTouch(MotionEvent ev) {
                switch (ev.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        String BackStr = "屏幕坐标：" + ev.getX() + "=== " + ev.getY();
                        LatLng latlng1 = mMapView.getMap().getProjection().fromScreenLocation(new Point((int)ev.getX(),(int)ev.getY()));

                        BackStr = BackStr + "经纬度坐标：" + latlng1.longitude + "===" + latlng1.latitude;
                        Toast.makeText(getContext(), BackStr, Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        //Toast.makeText(getContext(), "up事件", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
        ((Container) getActivity())
                .registerMyOnTouchListener(mTouchListener);
        return inflater.inflate(R.layout.devmapshow_frame,container,false);
    }






    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        mbaiduMap = new ClsBaiduMap(getContext());
        mbaiduMap.DoInitlization();//地图初始化

        mMapView = (MapView)getActivity().findViewById(R.id.baidumap);
        mbaiduMap.DoClsInit(mMapView);//地图信息初始化

        mMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mwdShow.setText("map dianji");
                Toast.makeText(getContext(),"diandiandian",Toast.LENGTH_SHORT).show();
            }
        });



        /**
         * 接收Activity的Touch回调的对象
         * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
         */
          /*DevInfo dinfo = new DevInfo();
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



        mbaiduMap.initMarkerClickEvent(baidumap_infowindow);*/
        //qqweqqwewww
    }

}
