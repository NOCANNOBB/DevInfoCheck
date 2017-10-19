package com.kenai.huafu.devinfocheck;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.kenai.huafu.DataTrasmation.ClsBaiduMap;
import com.kenai.huafu.DataTrasmation.Htpp;

public class DevMapSetActivity extends AppCompatActivity {


    private MapView mMapView;
    private ClsBaiduMap  mbaiduMap;
    private TextView mtvJD;
    private TextView mtvWD;
    private TextView mtvDevID;

    private String mDevID;
    private String mJD;
    private String mWD;
    private String mPhone;

    private Button mBtnMapConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dev_map_set);


        Intent intent = getIntent();
        //Bundle bud = intent.getExtras();

        mDevID = intent.getStringExtra("DevID");
        mPhone = intent.getStringExtra("phone");




        mtvDevID = (TextView)findViewById(R.id.info_devid);
        mtvJD = (TextView)findViewById(R.id.info_jd);
        mtvWD = (TextView)findViewById(R.id.info_wd);

        mtvDevID.setText(mDevID);

        mbaiduMap = new ClsBaiduMap(this);
        mbaiduMap.DoInitlization();//地图初始化

        mMapView = (MapView)findViewById(R.id.baidumapset);
        mbaiduMap.DoClsInit(mMapView,null);//地图信息初始化
        //mbaiduMap.SetCenter(39.963175, 116.400244);
        mBtnMapConfirm = (Button)findViewById(R.id.mapConfirm);

        mBtnMapConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread tpThread = new Thread(UpdateJWInfo);
                tpThread.start();
            }
        });


    }

    Runnable UpdateJWInfo  = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            //String tID = NowDevID.substring(0,5);

            String JD = mtvJD.getText().toString();
            String WD = mtvWD.getText().toString();


            String RequestUrl = Htpp.BasicUrl + "/SetDevCoordinate?devid=" + mDevID + "&djingdu=" + JD + "&dweidu=" + WD;
            String BackStr = Htpp.executeHttpGet(RequestUrl);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", BackStr);
            msg.setData(data);
            handler2.sendMessage(msg);
            if(BackStr.contains("设置成功")){
                Intent intent = new Intent(getApplicationContext(),Container.class);
                intent.putExtra(MainActivity.EXTREA_PHONE,mPhone);
                startActivity(intent);
                DevMapSetActivity.this.finish();
            }
        }
    };

    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            if(val.isEmpty()){
                val = "请检查网络连接";
            }
            else{

            }
            Toast.makeText(getApplicationContext(),val,Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                try {
                    String BackStr = "屏幕坐标：" + ev.getX() + "=== " + ev.getY();
                    LatLng latlng1 = mMapView.getMap().getProjection().fromScreenLocation(new Point((int) ev.getX(), (int) ev.getY()));
                    if (latlng1 == null) {
                        return super.onTouchEvent(ev);
                    }
                    mtvJD.setText(String.valueOf(latlng1.longitude));
                    mtvWD.setText(String.valueOf(latlng1.latitude));
                }
                catch (Exception e){}
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
}
