package com.kenai.huafu.devinfocheck;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kenai.huafu.DataTrasmation.Htpp;
import com.kenai.huafu.DataTrasmation.Register;

public class RegisterActivity extends AppCompatActivity {

    private Button mSubmit;
    private Button mGetYzm;
    private EditText mEditPhone;
    private EditText mEditPwd;
    private EditText mEditYzm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mSubmit = (Button)findViewById(R.id.btnsubmit);
        mGetYzm = (Button)findViewById(R.id.btnyzm);

        mEditPhone = (EditText)findViewById(R.id.editphone);
        mEditPwd = (EditText)findViewById(R.id.editpwd);
        mEditYzm = (EditText)findViewById(R.id.edityzm);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubmit_click(v);
            }
        });

        mGetYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYzm_click(v);
            }
        });
    }

    public void mSubmit_click(View view)
    {
        Thread tpThread = new Thread(networkTask);
        tpThread.start();
        //tpThread.stop();
    }

    public void mYzm_click(View view)
    {
        Thread tpThread = new Thread(CodeGetnetworkTask);
        tpThread.start();
        //tpThread.stop();
    }

    private String JudgeParamAla(String PhoneNumber,String PWD){
        if(PhoneNumber.length() != 11){
            return "电话号码输入有误";
        }
        if(PWD.isEmpty()){
            return "密码不能为空";
        }
        return "";
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


    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
           /* String RequestStr = "http://192.168.1.103:8732/JsonTest/Hello/123";
            String BackStr = Htpp.executeHttpGet(RequestStr);*/
            Register regis = new Register();
            regis.Yzm = mEditYzm.getText().toString();
            regis.Password = mEditPwd.getText().toString();
            regis.PhoneNumber = mEditPhone.getText().toString();

            String JudgeStr = JudgeParamAla(regis.PhoneNumber,regis.Password);
            if(!JudgeStr.isEmpty()){
                Message msg1 = new Message();
                Bundle data1 = new Bundle();
                data1.putString("value", JudgeStr);
                msg1.setData(data1);
                handler.sendMessage(msg1);
                return ;
            }


            Gson gson = new Gson();


            String RequesJsonStr = gson.toJson(regis);
            //"{'CHP':'s','Password':'2','PhoneNumber':'1','Yzm':'3'}
            //String EncodeStr ="{'CHP':'s','Password':'2','PhoneNumber':'1','Yzm':'3'}";
            String RequestStr = Htpp.BasicUrl + "/Registe/"+ EncodeMyUrlParam(RequesJsonStr);
            //  String ParamStr = "JsonStr=myTestStr";
            String BackStr = Htpp.executeHttpGet(RequestStr);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", BackStr);
            msg.setData(data);
            handler.sendMessage(msg);
            if(BackStr.contains("注册成功")){
                Intent intent = new Intent(RegisterActivity.this,Container.class);
                intent.putExtra(MainActivity.EXTREA_PHONE, regis.PhoneNumber);
                startActivity(intent);
            }
        }
    };

    public String EncodeMyUrlParam(String content){
        String returnstr = "";
        //try {
        returnstr = content.replaceAll("\"","\'");
        // } catch (UnsupportedEncodingException e) {
        //   e.printStackTrace();    //输出异常信息
        //  }
        return returnstr;
    }

    Runnable CodeGetnetworkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
           /* String RequestStr = "http://192.168.1.103:8732/JsonTest/Hello/123";
            String BackStr = Htpp.executeHttpGet(RequestStr);*/
            String pHoneNumber = mEditPhone.getText().toString().trim();
            String RequestStr = Htpp.BasicUrl + "/GetYZM/" + pHoneNumber;
            //  String ParamStr = "JsonStr=myTestStr";
            String BackStr = Htpp.executeHttpGet(RequestStr);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", BackStr);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

}
