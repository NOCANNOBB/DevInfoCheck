package com.kenai.huafu.devinfocheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.kenai.huafu.DataTrasmation.DevAlarmInfo;
import com.kenai.huafu.DataTrasmation.DevInfo;
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


        FillListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                ClickItemPostion = position;
            }
        });
        SetItemContent();
    }

    private void FillListView(){

        List<Map<String,Object>>rlist = new ArrayList<Map<String,Object>>();
        for(int i = 0; i < mAlarmList.size(); i++){
            DevAlarmInfo dinfo = mAlarmList.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("DevID","设备ID：" + dinfo.getDevID());

            map.put("Alarminfo",dinfo.getAlarmInfo());
            map.put("img",dinfo.getAvatar());
            rlist.add(map);
        }

        listAdapter = new ListViewSimpleCurAdapter(getActivity(),rlist,R.layout.devalarmlistitem,new String[]{"DevID","Alarminfo","img"},new int[]{R.id.DevID,R.id.Alarminfo,R.id.gifimg});
        mListView.setAdapter(listAdapter);
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
                    mAlarmList.remove(ClickItemPostion);
                    FillListView();
                    Toast.makeText(getContext(),"第一个",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"第二个",Toast.LENGTH_SHORT).show();
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
        int i=0;
        while(i<10){
            DevAlarmInfo information = new DevAlarmInfo();

            information.setDevID("报警1000" + Integer.toString(i));
            information.setAlarmInfo("温度过高报警，当前温度 30℃");
            information.setAlarmLevel("2");
            information.setAvatar(R.drawable.alarm1);
            mAlarmList.add(information); //将新的info对象加入到信息列表中
            i++;
        }
    }

}
