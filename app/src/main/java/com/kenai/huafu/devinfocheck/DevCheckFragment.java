package com.kenai.huafu.devinfocheck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kenai.huafu.DataTrasmation.DevInfo;
import com.kenai.huafu.DataTrasmation.ListViewSimpleCurAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang on 2017/9/28.
 */

public class DevCheckFragment extends Fragment {

    private ListView mListView;
    private List<DevInfo> mlistInfo = new ArrayList<DevInfo>();
    private int ClickItemPostion;

    private FragmentManager manager;
    private FragmentTransaction ft;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.devcheck_frame,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView = (ListView)getActivity().findViewById(R.id.DevList);
        manager = getFragmentManager();
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
        for(int i = 0; i < mlistInfo.size(); i++){
            DevInfo dinfo = mlistInfo.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title",dinfo.getTitle());
            String strText = "电流：" + dinfo.getDevDIANLIU() + "  电压：" + dinfo.getDevDIANYA()+ "  温度：" + dinfo.getDevWENDU();
            map.put("info",strText);
            map.put("img",dinfo.getAvatar());
            rlist.add(map);
        }

        ListViewSimpleCurAdapter listAdapter = new ListViewSimpleCurAdapter(getActivity(),rlist,R.layout.devlistitem,new String[]{"title","info","img"},new int[]{R.id.title,R.id.info,R.id.img});

        mListView.setAdapter(listAdapter);
    }


    private void CreateItems(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ListView modeListView = new ListView(getActivity());
        String[] modes = new String[]{"删除设备","报警设置","取消"};
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
                    mlistInfo.remove(ClickItemPostion);
                    FillListView();
                    Toast.makeText(getContext(),"第一个",Toast.LENGTH_SHORT).show();
                }else if (position == 1){
                    ft = manager.beginTransaction();
                    AlarmSetting AS = new AlarmSetting();
                    Bundle bundle = new Bundle();
                    bundle.putString("DevID",mlistInfo.get(ClickItemPostion).getId());
                    AS.setArguments(bundle);
                    ft.replace(R.id.frame_container, AS);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(position == 2){

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
        mlistInfo.clear();
        int i=0;
        while(i<10){
            DevInfo information = new DevInfo();
            information.setId("1000" + Integer.toString(i));
            information.setTitle("标题"+i);
            information.setDevDIANLIU("33");
            information.setDevDIANYA("22");
            information.setDevWENDU("11");
            information.setAvatar(R.drawable.bingxiang);

            mlistInfo.add(information); //将新的info对象加入到信息列表中
            i++;
        }
    }
}
