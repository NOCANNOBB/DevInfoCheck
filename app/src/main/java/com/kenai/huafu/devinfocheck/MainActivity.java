package com.kenai.huafu.devinfocheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kenai.huafu.DataTrasmation.Htpp;
import com.kenai.huafu.DataTrasmation.PermissionRequest;
import com.kenai.huafu.DataTrasmation.Register;

public class MainActivity extends AppCompatActivity {
    private Button mbtnReg;
    private Button mbtnLogon;

    private EditText etPhone;
    private EditText etPassWord;

    public final  static  String EXTREA_PHONE = "com.kenai.huafu.USERINFO";
    SharedPreferences sp;
    SharedPreferences.Editor editorMain;

    private String mUserName;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PermissionRequest pr = new PermissionRequest();
        String[] Permissions = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_EXTERNAL_STORAGE","android.permission.ACCESS_COARSE_LOCATION","android.permission.ACCESS_FINE_LOCATION"};
        pr.RequestPermission(this.getApplicationContext(),Permissions,this);


        sp = getSharedPreferences("userInfo", 0);
        String name=sp.getString("USER_NAME", "");
        String pass =sp.getString("PASSWORD", "");

        if((!name.isEmpty()) && (!pass.isEmpty())){
            mUserName = name;
            mPassword = pass;
            Thread tpThread = new Thread(ReLogOnnetworkTask);
            tpThread.start();
            MainActivity.this.finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mbtnLogon = (Button)findViewById(R.id.btnlogon);
        mbtnReg = (Button)findViewById(R.id.u_register);

        etPhone = (EditText)findViewById(R.id.phone);
        etPassWord = (EditText)findViewById(R.id.password);

        mbtnLogon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mUserName = etPhone.getText().toString().trim();
                mPassword = etPassWord.getText().toString().trim();

                Thread tpThread = new Thread(ReLogOnnetworkTask);
                tpThread.start();
            }

        });

        mbtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpToReg(view);
            }
        });


    }


    public void JumpToReg(View view){
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");

            String BackStr = val;

            if(BackStr == "")
            {
                BackStr = "Get NULL";
            }
            // Toast.makeText(FirstActivity.this, "You clicked Button 1", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),BackStr,Toast.LENGTH_SHORT).show();
        }
    };

    Runnable ReLogOnnetworkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作

            String PhoneNumber = mUserName;
            String PassWord = mPassword;
            String RequestUrl = Htpp.BasicUrl + "/LogOn?pHoneNumber=" + PhoneNumber + "&PWD=" + PassWord;
            String BackStr = Htpp.executeHttpGet(RequestUrl);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", BackStr);
            msg.setData(data);
            handler.sendMessage(msg);
            if(BackStr.contains("登录成功")){
                SharedPreferences.Editor editor =sp.edit();
                editor.putString("USER_NAME", PhoneNumber);
                editor.putString("PASSWORD", PassWord);
                editor.commit();
                //界面跳转传递参数
                Intent intent = new Intent(getApplicationContext(),Container.class);
                intent.putExtra(EXTREA_PHONE,PhoneNumber);
                startActivity(intent);
                MainActivity.this.finish();
            }
        }
    };
}
