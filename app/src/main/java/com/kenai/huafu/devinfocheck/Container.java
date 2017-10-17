package com.kenai.huafu.devinfocheck;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.IDNA;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karics.library.zxing.android.CaptureActivity;
import com.kenai.huafu.DataTrasmation.AlarmSoundPlay;
import com.kenai.huafu.DataTrasmation.Define;
import com.kenai.huafu.DataTrasmation.DevAlarm;
import com.kenai.huafu.DataTrasmation.DevAlarmInfo;
import com.kenai.huafu.DataTrasmation.DevDataInfo;
import com.kenai.huafu.DataTrasmation.Htpp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Container extends AppCompatActivity {

    private Button DevCheck;
    private Button Alarm;
    private Button MapShow;
    private Button Own;
    private FrameLayout frameContainer;
    private Button InfoCK;
    private String mPhoneNumber;
    private AlarmSoundPlay mAlarm;
    private Thread m_AlarmThread;
    private int m_SleepTime = 3000;
    private boolean IsActDestory = false;

    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
      //  getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_container);

        mAlarm =new AlarmSoundPlay();

        m_AlarmThread = new Thread(BKAlarmInfoGet);
        m_AlarmThread.start();

        DevCheck = (Button)findViewById(R.id.DevCheck);
        Alarm = (Button)findViewById(R.id.Alarm);
        Own = (Button)findViewById(R.id.Self);
        InfoCK = (Button)findViewById(R.id.InfoCheck) ;
        MapShow = (Button)findViewById(R.id.showmap);

        Intent intent = getIntent();
        mPhoneNumber = intent.getStringExtra(MainActivity.EXTREA_PHONE);

        //frameContainer = (FrameLayout)findViewById(R.id.frame_container);

        //设备监控
        DevCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevCheckFragment fragment1 = new DevCheckFragment();

                Bundle bundle = new Bundle();
                bundle.putString("phone",mPhoneNumber);
                bundle.putString("alarm","");
                fragment1.setArguments(bundle);
                addFragment(fragment1, "DevCheckFragment");
            }
        });
        //报警信息
        Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmFragment fragment1 = new AlarmFragment();
                addFragment(fragment1, "AlarmFragment");
            }
        });

        //我
        Own.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OwnInfo fragment1 = new OwnInfo();
                addFragment(fragment1, "OwnInfo");
            }
        });
        //信息查询
        InfoCK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevInfoCheck fragment1 = new DevInfoCheck();
                addFragment(fragment1, "DevInfoCheck");
            }
        });

        //地图显示
        MapShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapShowFragment fragment1 = new MapShowFragment();
                addFragment(fragment1, "MapShowFragment");
            }
        });

    }

    @Override
    public void onDestroy(){

        IsActDestory = true;
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException iex){}

        if (mAlarm.IsSoundPlaying) {
            mAlarm.StopSondPlay();
        }

        super.onDestroy();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            if (!val.isEmpty()) {
                Gson gson =new Gson();
                List<DevAlarm> reg = gson.fromJson(val, new TypeToken<List<DevAlarm>>() {}.getType());
                if(reg.size() == 0){
                    if (mAlarm.IsSoundPlaying) {
                        mAlarm.StopSondPlay();
                    }
                    Define.lock.lock();
                    try{
                        Define.g_DevAlarmInfo.clear();
                    }
                    finally {
                        Define.lock.unlock();
                    }
                    return;
                }
                SetAlarmList(reg);
                if(!mAlarm.IsSoundPlaying) {
                    mAlarm.PlaySound(getApplicationContext(), R.raw.alarm1);
                }
            } else {
                if (mAlarm.IsSoundPlaying) {
                    mAlarm.StopSondPlay();
                }
            }

        }
    };


    private void SetAlarmList(List<DevAlarm> reg){
        Define.lock.lock();
        try{
            Define.g_DevAlarmInfo.clear();
            for(int i = 0; i < reg.size(); i++) {
                String ShowContent = "报警值：" + reg.get(i).getAlarmValue() + " 当前值：" + reg.get(i).getNowValue() + " 报警时间：" + reg.get(i).getAlarmTime();
                DevAlarmInfo DAI = new DevAlarmInfo();
                DAI.setDevID(reg.get(i).getDevID());
                DAI.setAlarmLevel(reg.get(i).getAlarmType());
                DAI.setAlarmInfo(ShowContent);
                DAI.setAvatar(R.drawable.alarm1);
                Define.g_DevAlarmInfo.add(DAI);
            }
        }
        finally {
            Define.lock.unlock();
        }
    }


    Runnable BKAlarmInfoGet = new Runnable(){
        @Override
        public void run(){
            String PhoneNumber = mPhoneNumber;
            while (true) {

                if(IsActDestory){
                    IsActDestory = false;
                    break;}

                String RequestUrl = Htpp.BasicUrl + "/GetDevAlarmInfo/" + mPhoneNumber;

                String BackStr = Htpp.executeHttpGet(RequestUrl);
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value", BackStr);
                msg.setData(data);
                handler.sendMessage(msg);
                try {
                    Thread.sleep(m_SleepTime);
                }catch (InterruptedException e){}
            }
        }
    };



    private void addFragment(Fragment fragment, String tag) {
        FragmentManager manager=getFragmentManager();
        FragmentTransaction ft=manager.beginTransaction();
                /*
                * add是将一个fragment实例添加到Activity的最上层
                * replace替换containerViewId中的fragment实例，
                *   注意，它首先把containerViewId中所有fragment删除，然后再add进去当前的fragment
                * */
        ClearAllFragment(manager,ft);
        ft.add(R.id.frame_container, fragment,tag);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_new:
                AddDev();
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false;
        }
    }

    private void AddDev(){
        Intent intent = new Intent(this,
                CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);
                Toast.makeText(this,"content",Toast.LENGTH_SHORT).show();
            }
        }
    }



    private boolean IsHaveFragment(FragmentManager manager,FragmentTransaction ft,String Tag){
        Fragment fg = manager.findFragmentByTag(Tag);
        if(fg != null){
            ft.remove(fg);
            return true;
        }
        else
        {
            return false;
        }
    }

    private void ClearAllFragment(FragmentManager manager,FragmentTransaction ft){
        Fragment fg = manager.findFragmentByTag("DevCheckFragment");
        if(fg != null){
            ft.remove(fg);
            fg = null;
        }
        fg = manager.findFragmentByTag("AlarmFragment");
        if(fg != null){
            ft.remove(fg);
            fg = null;
        }
        fg = manager.findFragmentByTag("AlarmSetting");
        if(fg != null){
            ft.remove(fg);
            fg = null;
        }
        fg = manager.findFragmentByTag("OwnInfo");
        if(fg != null){
            ft.remove(fg);
            fg = null;
        }
        fg = manager.findFragmentByTag("DevInfoCheck");
        if(fg != null){
            ft.remove(fg);
            fg = null;
        }
        fg = manager.findFragmentByTag("MapShowFragment");
        if(fg != null){
            ft.remove(fg);
            fg = null;
        }
    }

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }



}
