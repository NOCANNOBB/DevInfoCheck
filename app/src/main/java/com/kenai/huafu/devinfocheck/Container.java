package com.kenai.huafu.devinfocheck;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;

import java.util.List;
import java.util.Map;

public class Container extends AppCompatActivity {

    private Button DevCheck;
    private Button Alarm;
    private Button MapShow;
    private Button Own;
    private FrameLayout frameContainer;
    private Button InfoCK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_container);

        DevCheck = (Button)findViewById(R.id.DevCheck);
        Alarm = (Button)findViewById(R.id.Alarm);
        Own = (Button)findViewById(R.id.Self);
        InfoCK = (Button)findViewById(R.id.InfoCheck) ;
        MapShow = (Button)findViewById(R.id.showmap);
        //frameContainer = (FrameLayout)findViewById(R.id.frame_container);

        //设备监控
        DevCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevCheckFragment fragment1 = new DevCheckFragment();
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
                Toast.makeText(this,"添加设备",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false;
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

}
