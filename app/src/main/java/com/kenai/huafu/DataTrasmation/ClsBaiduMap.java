package com.kenai.huafu.DataTrasmation;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.kenai.huafu.devinfocheck.R;

/**
 * Created by zhang on 2017/9/29.
 */

public class ClsBaiduMap {

    public MapView mapView;
    public BaiduMap mBaidumap;
    private Context appContext;
    public ClsBaiduMap(Context context){
        appContext = context;
    }

    public void DoInitlization(){

    }
    public void DoClsInit(MapView tpmapView){
        mapView = tpmapView;
        mBaidumap = mapView.getMap();
    }

    public void SetCenter(double WEIDU,double JINDU){
        LatLng cenpt = new LatLng(JINDU,WEIDU);//39.963175, 116.400244);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaidumap.setMapStatus(mMapStatusUpdate);
    }

    public void AddPoint(double JINDU,double WEIDU,int ImgID,DevInfo Infos){
        Marker marker = null;
        LatLng point = new LatLng(JINDU, WEIDU);
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(ImgID);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
//在地图上添加Marker，并显示
        marker = (Marker) (mBaidumap.addOverlay(option));
        Bundle bundle = new Bundle();

        bundle.putSerializable("info",Infos);
        marker.setExtraInfo(bundle);
    }

    public void initMarkerClickEvent(final  LinearLayout baidumap_infowindow)
    {
        // 对Marker的点击
        mBaidumap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(final Marker marker)
            {
                DevInfo ShowInfo = (DevInfo) marker.getExtraInfo().get("info");

                createInfoWindow(baidumap_infowindow);



                final LatLng ll = marker.getPosition();
                android.graphics.Point p = mBaidumap.getProjection().toScreenLocation(ll);
                p.y -= 47;
                LatLng llInfo = mBaidumap.getProjection().fromScreenLocation(p);

                BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(baidumap_infowindow);

                // 为弹出的InfoWindow添加点击事件
                InfoWindow mInfoWindow = new InfoWindow(bitmap,llInfo,0, new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {
                        mBaidumap.hideInfoWindow();
                    }
                });
                // 显示最后一条的InfoWindow
                mBaidumap.showInfoWindow(mInfoWindow);
                return true;
            }
        });
    }

    private void createInfoWindow(LinearLayout baidumap_infowindow){

        InfoWindowHolder holder = null;
        if(baidumap_infowindow.getTag () == null){
            holder = new InfoWindowHolder ();

            holder.tv_DevID = (TextView) baidumap_infowindow.findViewById (R.id.tv_DevID);
            holder.tv_WENDU = (TextView) baidumap_infowindow.findViewById (R.id.tv_WENDU);
            holder.tv_DIANYA = (TextView) baidumap_infowindow.findViewById (R.id.tv_DIANYA);
            holder.tv_DIANLIU = (TextView) baidumap_infowindow.findViewById (R.id.tv_DIANLIU);

            baidumap_infowindow.setTag (holder);
        }
        holder = (InfoWindowHolder) baidumap_infowindow.getTag ();

        //holder.tv_entname.setText (bean.getName());
        //holder.tv_checkdept.setText (bean.getName());
        //holder.tv_checkuser.setText (bean.getDistance());
        //holder.tv_checktime.setText (bean.getName());
    }


    private class ViewHolder
    {

        public TextView infoName;

    }

    protected void popupInfo(RelativeLayout mMarkerLy, String info,TextView nameView,String Content)
    {
        ViewHolder viewHolder = null;
        if (mMarkerLy.getTag() == null)
        {
            viewHolder = new ViewHolder();
            viewHolder.infoName = nameView;
            viewHolder.infoName.setText(Content);
            mMarkerLy.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) mMarkerLy.getTag();
        viewHolder.infoName.setText(Content);
    }

}
