package com.kenai.huafu.devinfocheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.kenai.huafu.DataTrasmation.AlarmSoundPlay;
import com.kenai.huafu.DataTrasmation.Define;
import com.kenai.huafu.DataTrasmation.DevAlarmInfo;
import com.kenai.huafu.DataTrasmation.DevInfo;
import com.kenai.huafu.DataTrasmation.Htpp;
import com.kenai.huafu.DataTrasmation.ListViewSimpleCurAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2017/9/28.
 */

public class AlarmFragment extends Fragment {
    private List<DevAlarmInfo> mAlarmList = new ArrayList<DevAlarmInfo>();
    private AlarmSoundPlay mAlarmSoundPlay;

    private Button mbtnPlay;
    private Button mbtnStop;

    private int ClickItemPostion;

    private ListView mListView;
    private ListViewSimpleCurAdapter listAdapter;

    private boolean isThreadStop = false;

    private String ConfirmDevID;
    //private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alarminfo_frame, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView = (ListView)getActivity().findViewById(R.id.AlarmDevList);

        setInfo();


       // FillListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                ClickItemPostion = position;
            }
        });
        SetItemContent();
    }



    @Override
    public void onDestroy(){

        isThreadStop = true;
        try{
            Thread.sleep(1000);
        }
        catch (InterruptedException iex){}
        super.onDestroy();
    }


    private void CreateItems(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ListView modeListView = new ListView(getActivity());
        String[] modes = new String[]{"确认报警","取消"};
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,
                android.R.id.text1,modes);
        modeListView.setAdapter(modeAdapter);
        builder.setView(modeListView);
        final Dialog dialog = builder.create();
        dialog.show();
        modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent,View view,int position,long id)
            {

                if(position == 0)
                {
                   // ConfirmDevID = Define.g_DevAlarmInfo.get()
                    ConfirmAlarm();
                    Toast.makeText(getContext(),"第一个" + ClickItemPostion,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"第二个" + ClickItemPostion,Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }


    private void SetItemContent(){
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                CreateItems();
            }
        });
    }

    private void ConfirmAlarm(){
        Thread tpThread = new Thread(ConfirmDevAlarm);
        tpThread.start();
    }


    Runnable ConfirmDevAlarm = new Runnable() {

        @Override
        public void run() {
            Define.lock.lock();
            String DevID = "";
            try {
                DevID = Define.g_DevAlarmInfo.get(ClickItemPostion).getDevID();
            }
            finally {
                Define.lock.unlock();
            }
            String RequestUrl = Htpp.BasicUrl + "/ConfirmDevAlarm/" + DevID;
            String BackStr = Htpp.executeHttpGet(RequestUrl);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", BackStr);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };


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
        mAlarmList.clear();
        Thread tpThread = new Thread(RefreshDevAlarmInfo);
        tpThread.start();

    }



    Runnable RefreshDevAlarmInfo = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            while (true) {

                if(isThreadStop){
                    isThreadStop = false;
                    //Log.d("info","thread stop");
                    break;
                }

                try {
                    Message msg = new Message();
                    //Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putString("value", "AlarmList");
                    msg.setData(data);
                    handler.sendMessage(msg);
                    Thread.sleep(3000);
                }
                catch (InterruptedException iex){

                }
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            if(val.compareTo("AlarmList") == 0) {
                FillListView();
            }
            else
            {
                FillListView();
                Toast.makeText(getContext(),val,Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void FillListView(){
        Define.lock.lock();
        List<Map<String, Object>> rlist = null;
        try {
            rlist = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < Define.g_DevAlarmInfo.size(); i++) {
                DevAlarmInfo dinfo = Define.g_DevAlarmInfo.get(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("DevID","设备ID：" + dinfo.getDevID());
                String strText = dinfo.getAlarmInfo();
                map.put("Alarminfo",strText);
                map.put("img",dinfo.getAvatar());
                rlist.add(map);
            }
        }
        finally {
            Define.lock.unlock();
        }
        if(rlist !=null) {
            ListViewSimpleCurAdapter tplistAdapter = new ListViewSimpleCurAdapter(getActivity(), rlist, R.layout.devalarmlistitem, new String[]{"DevID", "Alarminfo", "img"}, new int[]{R.id.DevID, R.id.Alarminfo, R.id.gifimg});
            mListView.setAdapter(tplistAdapter);
        }
    }

}
