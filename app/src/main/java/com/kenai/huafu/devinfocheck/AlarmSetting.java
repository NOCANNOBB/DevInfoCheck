package com.kenai.huafu.devinfocheck;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kenai.huafu.DataTrasmation.Define;
import com.kenai.huafu.DataTrasmation.Htpp;

/**
 * Created by zhang on 2017/9/28.
 */

public class AlarmSetting extends Fragment {

    private Button btnCancle;
    private Button btnConfirm;
    private FragmentManager manager;
    private FragmentTransaction ft;

    private TextView mViewDevID;
    private EditText mAlarmValue;


    private String mPhoneNumber;
    private String mDevID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alarmset_frame, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        String DevID = bundle.getString("DevID");
        mDevID = DevID;
        mPhoneNumber =  bundle.getString("phone");
        manager = getFragmentManager();
        btnCancle = (Button)getActivity().findViewById(R.id.btnRelease);
        mViewDevID = (TextView)getActivity().findViewById(R.id.AlDevID);
        mViewDevID.setText(DevID);

        mAlarmValue = (EditText)getActivity().findViewById(R.id.setALDev);

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft = manager.beginTransaction();
                DevCheckFragment DCF = new DevCheckFragment();
                ft.replace(R.id.frame_container, DCF);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        btnConfirm = (Button)getActivity().findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread tpThread = new Thread(AlarmSetNetWork);
                tpThread.start();
            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");

            String BackStr = val;

            if(BackStr == "") {
                BackStr = "Get NULL";
            }
            // Toast.makeText(FirstActivity.this, "You clicked Button 1", Toast.LENGTH_SHORT).show();
            Toast.makeText(getContext(),BackStr,Toast.LENGTH_SHORT).show();
        }
    };

    //报警设置

    Runnable AlarmSetNetWork = new Runnable() {

        @Override
        public void run() {
            String PhoneNumber = mPhoneNumber;
            String AlarmSet= mAlarmValue.getText().toString().trim();
            String RequestUrl = Htpp.BasicUrl + "/DevAlarmSet/" + mPhoneNumber;
            String BackStr = Htpp.executeHttpGet(RequestUrl);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", BackStr);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

}
