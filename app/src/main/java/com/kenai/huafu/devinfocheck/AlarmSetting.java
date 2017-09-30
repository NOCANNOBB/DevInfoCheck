package com.kenai.huafu.devinfocheck;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by zhang on 2017/9/28.
 */

public class AlarmSetting extends Fragment {

    private Button btnCancle;
    private FragmentManager manager;
    private FragmentTransaction ft;

    private TextView mViewDevID;

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
        manager = getFragmentManager();
        btnCancle = (Button)getActivity().findViewById(R.id.btnRelease);
        mViewDevID = (TextView)getActivity().findViewById(R.id.AlDevID);
        mViewDevID.setText(DevID);
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

    }

}
