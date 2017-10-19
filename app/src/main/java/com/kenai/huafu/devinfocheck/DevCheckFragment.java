package com.kenai.huafu.devinfocheck;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kenai.huafu.DataTrasmation.Define;
import com.kenai.huafu.DataTrasmation.DevDataInfo;
import com.kenai.huafu.DataTrasmation.DevInfo;
import com.kenai.huafu.DataTrasmation.Htpp;
import com.kenai.huafu.DataTrasmation.ListViewSimpleCurAdapter;
import com.kenai.huafu.DataTrasmation.Register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by zhang on 2017/9/28.
 */

public class DevCheckFragment extends Fragment {

    private ListView mListView;
    private List<DevInfo> mlistInfo = new ArrayList<DevInfo>();
    private int ClickItemPostion;

    private FragmentManager manager;
    private FragmentTransaction ft;

    private String mPhoneNumber;
    private String mDevId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.devcheck_frame,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (ListView)getActivity().findViewById(R.id.DevList);


        manager = getFragmentManager();

        Bundle bundle = getArguments();

        mPhoneNumber = bundle.getString("phone");

        //{
            setInfo();
        //}



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                ClickItemPostion = position;
            }
        });
        SetItemContent();
    }
    @Override
    public void onDestroy(){

        Define.g_IsDataGetThreadStop = true;
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException iex){}
        super.onDestroy();
    }


    private void FillListView(){
        try {
            List<Map<String, Object>> rlist = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < mlistInfo.size(); i++) {
                DevInfo dinfo = mlistInfo.get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("title", dinfo.getId());
                String strText = "电流：" + dinfo.getDevDIANLIU() + "  电压：" + dinfo.getDevDIANYA() + "  温度：" + dinfo.getDevWENDU();
                map.put("info", strText);
                map.put("img", dinfo.getAvatar());
                rlist.add(map);
            }

            Activity act = getActivity();


            ListViewSimpleCurAdapter listAdapter = new ListViewSimpleCurAdapter(act, rlist, R.layout.devlistitem, new String[]{"title", "info", "img"}, new int[]{R.id.title, R.id.info, R.id.img});

            mListView.setAdapter(listAdapter);
        }
        catch (Exception e){
            Log.d("Error","FillListView Error");
        }
    }


    private void CreateItems(){
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            ListView modeListView = new ListView(getActivity());
            String[] modes = new String[]{"删除设备", "报警设置", "坐标设置", "取消"};
            ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                    android.R.id.text1, modes);
            modeListView.setAdapter(modeAdapter);
            builder.setView(modeListView);
            final Dialog dialog = builder.create();
            dialog.show();
            modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0) {
                        DoRemoveServer(ClickItemPostion);
                        Toast.makeText(getContext(), "第一个", Toast.LENGTH_SHORT).show();
                    } else if (position == 1) {
                        ft = manager.beginTransaction();
                        AlarmSetting AS = new AlarmSetting();
                        Bundle bundle = new Bundle();
                        bundle.putString("DevID", mlistInfo.get(ClickItemPostion).getId());
                        bundle.putString("phone", mPhoneNumber);
                        AS.setArguments(bundle);
                        ft.replace(R.id.frame_container, AS);
                        ft.addToBackStack(null);
                        ft.commit();
                    } else if (position == 2) {
                        Intent intent = new Intent(getActivity(), DevMapSetActivity.class);


                        intent.putExtra("DevID", mlistInfo.get(ClickItemPostion).getId());
                        intent.putExtra("phone", mPhoneNumber);

                        startActivity(intent);
                        getActivity().finish();
                    }
                    dialog.dismiss();
                }
            });
        }
        catch (Exception e){
            Log.d("Error","CreateItems Error");
        }
    }

    private void DoRemoveServer(int ClickItemPostion){

        mDevId = mlistInfo.get(ClickItemPostion).getId();

        Thread tpThread = new Thread(GetDevDelNetWork);
        tpThread.start();

        mlistInfo.remove(ClickItemPostion);
    }



    private void SetItemContent(){
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                CreateItems();
            }
        });
    }

    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case 0:
                // 确认报警操作 Toast.makeText(getActivity(), "信息ID:"+infoId,Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),"确认报警操作",Toast.LENGTH_SHORT).show();
                break;

            case 1:
                // 取消操作
                break;

            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void setInfo(){
        mlistInfo.clear();

        Thread tpThread = new Thread(GetDevListNetWork);
        tpThread.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                Bundle data = msg.getData();
                String val = data.getString("value");
                String val2 = data.getString("bvalue");

                String BackStr = val;

                if (BackStr.isEmpty()) {
                    BackStr = "请检查网络连接";
                    Toast.makeText(getContext(), BackStr, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (val2 == "1") {

                    GetDevDataInfo(val);
                }
            }
            catch (Exception e){
                Log.d("Error","handleMessage Error");
            }
            // Toast.makeText(FirstActivity.this, "You clicked Button 1", Toast.LENGTH_SHORT).show();

        }
    };


    private void GetDevDataInfo(String DevInfo){
        try {
            Gson gson = new Gson();
            List<DevDataInfo> reg = gson.fromJson(DevInfo, new TypeToken<List<DevDataInfo>>() {
            }.getType());

            for (DevDataInfo stu : reg) {
                boolean isfind = false;
                for (DevInfo devInfo : mlistInfo) {
                    if (stu.getServerDevInfo().getDevID().compareTo(devInfo.getId()) == 0) {
                        isfind = true;
                        devInfo.setDevWENDU(stu.getWENDU());
                        devInfo.setDevDIANYA(stu.getDIANYA());
                        devInfo.setDevDIANLIU(stu.getDIANLIU());
                        break;
                    }
                }
                if (isfind == false) {
                    DevInfo devInfo = new DevInfo();
                    devInfo.setId(stu.getServerDevInfo().getDevID());
                    devInfo.setDevDIANLIU(stu.getDIANLIU());
                    devInfo.setDevDIANYA(stu.getDIANYA());
                    devInfo.setDevWENDU(stu.getWENDU());
                    devInfo.setAvatar(R.drawable.bingxiang);
                    devInfo.setTitle(stu.getServerDevInfo().getDevName());
                    mlistInfo.add(devInfo);
                }
            }
            FillListView();
        }
        catch(Exception ex){
            Log.d("Error","GetDevDataInfo Error");
        }

    }


    Runnable GetDevListNetWork = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            while (true) {

                if(Define.g_IsDataGetThreadStop){
                    Define.g_IsDataGetThreadStop = false;
                    Log.d("info","thread stop");
                    break;
                }

                try {
                    String PhoneNumber = mPhoneNumber;
                    String RequestUrl = Htpp.BasicUrl + "/GetDevInfos/" + mPhoneNumber;
                    String BackStr = Htpp.executeHttpGet(RequestUrl);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", BackStr);
                    data.putString("bvalue", "1");
                    msg.setData(data);
                    handler.sendMessage(msg);

                    Thread.sleep(3000);
                }
                catch (InterruptedException iex){
                    Log.d("Error","GetDevListNetWork Error");
                }
            }
        }
    };

    Runnable GetDevDelNetWork = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
           // while (true) {
               // try {
                    String PhoneNumber = mPhoneNumber;
                    String RequestUrl = Htpp.BasicUrl + "/DelDev?pHoneNumber=" + mPhoneNumber + "&Devid=" + mDevId;
                    String BackStr = Htpp.executeHttpGet(RequestUrl);
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", BackStr);
                    data.putString("bvalue", "2");
                    msg.setData(data);
                    handler.sendMessage(msg);
        }
    };


}
