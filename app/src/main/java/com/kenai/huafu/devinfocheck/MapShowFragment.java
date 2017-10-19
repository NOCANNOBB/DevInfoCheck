package com.kenai.huafu.devinfocheck;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kenai.huafu.DataTrasmation.ClsBaiduMap;
import com.kenai.huafu.DataTrasmation.Define;
import com.kenai.huafu.DataTrasmation.DevAlarm;
import com.kenai.huafu.DataTrasmation.DevInfo;
import com.kenai.huafu.DataTrasmation.Htpp;
import com.kenai.huafu.DataTrasmation.InfoWindowHolder;
import com.kenai.huafu.DataTrasmation.MapDevDataInfo;

import java.security.PublicKey;
import java.util.List;

/**
 * Created by zhang on 2017/9/29.
 */

public class MapShowFragment extends Fragment {
    private MapView mMapView;
    private RelativeLayout mMarkerInfoLy;
    private LinearLayout baidumap_infowindow;
    private ClsBaiduMap  mbaiduMap;
    private TextView mwdShow;
    private String mPhoneNumber;
    public Context appContext;
    private boolean IsThreadStop = false;
    private Container.MyOnTouchListener mTouchListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.devmapshow_frame,container,false);
    }






    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        Bundle bundle = getArguments();

        mPhoneNumber = bundle.getString("phone");


        mbaiduMap = new ClsBaiduMap(getContext());
        mbaiduMap.DoInitlization();//地图初始化

        mMapView = (MapView)getActivity().findViewById(R.id.baidumap);


        baidumap_infowindow = (LinearLayout) LayoutInflater.from (getContext()).inflate (R.layout.baidumap_infowindow, null);

        mbaiduMap.DoClsInit(mMapView,baidumap_infowindow);//地图信息初始化

        mbaiduMap.initMarkerClickEvent();



        Thread tpThread = new Thread(GetDevMapInfos);
        tpThread.start();



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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            GetMapDevInfo(val);
        }
    };


    private void GetMapDevInfo(String devInfos){
        Gson gson =new Gson();
        List<MapDevDataInfo> reg = gson.fromJson(devInfos, new TypeToken<List<MapDevDataInfo>>() {}.getType());
        for (MapDevDataInfo mpi : reg) {

            DevInfo devInfo = new DevInfo();
            devInfo.setId(mpi.getDevDataInfo().getServerDevInfo().getDevID());
            devInfo.setTitle(mpi.getDevDataInfo().getServerDevInfo().getDevID());
            devInfo.setDevWENDU(mpi.getDevDataInfo().getWENDU());
            devInfo.setDevDIANLIU(mpi.getDevDataInfo().getDIANLIU());
            devInfo.setDevDIANYA(mpi.getDevDataInfo().getDIANYA());
            devInfo.setUpTime(mpi.getDevDataInfo().getUpTime());
            //devInfo.
            //if(mpi.getDevAlarmInfo() == null) {
                mbaiduMap.AddPoint(mpi.getWeidu(), mpi.getJingdu(), R.drawable.mapmark, devInfo);
            //}
        }
    };

    @Override
    public void onDestroy(){

        IsThreadStop = true;
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException iex){}
        super.onDestroy();
    }


    Runnable GetDevMapInfos = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            while(true){
                if(IsThreadStop){
                    IsThreadStop = false;
                    break;
                }
                String RequestUrl = Htpp.BasicUrl + "/GetDevMapInfo/" + mPhoneNumber;
                String BackStr = Htpp.executeHttpGet(RequestUrl);
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", BackStr);
                msg.setData(data);
                handler.sendMessage(msg);
                try{
                    Thread.sleep(4000);
                }
                catch (InterruptedException iex){}
            }

        }
    };

}
