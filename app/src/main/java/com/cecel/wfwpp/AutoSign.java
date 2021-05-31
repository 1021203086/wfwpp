package com.cecel.wfwpp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AutoSign extends AppCompatActivity {

    private String email;
    private String name;
    private String username;
    private String password;
    private String timeOption;  //"0"为9点,"1"为0点10,"2"为7点,"3"为11点
    private int submitOption;
    private int timeChoose;
    private EditText emailText;
    private EditText nameText;
    private RadioGroup radioGroup;
    private RadioGroup radioGroup1;
    MyHandler handler = new MyHandler(this);
    private static ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_sign);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setLogo(R.drawable.arrow_left);
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(AutoSign.this);
        dialog.setTitle("使用须知");
        dialog.setMessage("        本功能引用自http://card.52pika.cn/ \n仅供学习与参考!\n" +
                "        目前发现0点的打卡偶尔会失败，建议修改时间为早上7点或9点\n" +
                "        此系统原则上是为防止忘记打卡设计，不得用于位置欺骗\n" +
                "        如果你离开了学校，请自行停用自动打卡并手动更新位置\n" +
                "        如果你不同意上述协议约定，请删除打卡账号并自行打卡\n" +
                "        取消打卡直接勾选\"停用自动打卡\"提交即可，无需输入其他内容");
        dialog.setCancelable(false);
        dialog.setPositiveButton("我已悉知",new DialogInterface.
                OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();
                username = intent.getStringExtra("username");
                password = intent.getStringExtra("password");
                Toast.makeText(AutoSign.this, "用户学号与密码获取成功...", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setNegativeButton("我任性不同意",new DialogInterface.
                OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        dialog.show();
        Button button = findViewById(R.id.auto_sign_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View v) {
                emailText = findViewById(R.id.ed_mail);
                email = emailText.getText().toString();
                nameText = findViewById(R.id.ed_name);
                name = nameText.getText().toString();
                radioGroup = findViewById(R.id.time_choose);
                radioGroup1 = findViewById(R.id.submit_option);
                timeChoose = radioGroup.getCheckedRadioButtonId();
                submitOption = radioGroup1.getCheckedRadioButtonId();
                switch (timeChoose){
                    case R.id.RB_0:timeOption = "0";break;
                    case R.id.RB_1:timeOption = "1";break;
                    case R.id.RB_2:timeOption = "2";break;
                    case R.id.RB_3:timeOption = "3";break;
                    default:break;
                }
                switch (submitOption){
                    case R.id.start:
                        sendRequest();
                        break;
                    case R.id.close:
                        cancelRequest();
                        break;
                }
                progressDialog = new ProgressDialog(AutoSign.this);
                progressDialog.setTitle("请求提交中");
                progressDialog.setMessage("请稍等...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
    }

    static class MyHandler extends Handler{
        WeakReference<AutoSign> mActivity;
        public MyHandler(AutoSign activity){
            mActivity = new WeakReference<AutoSign>(activity);
        }

        @Override
        public void handleMessage(Message msg){         //用于接收线程发出的message，从打在UI线程打印提示
            AutoSign theActivity = mActivity.get();
            switch (msg.what){
                case -1: {
                    Intent intent = new Intent();
                    intent.putExtra("submitOption",theActivity.submitOption);
                    progressDialog.dismiss();
                    theActivity.setResult(RESULT_OK, intent);
                    theActivity.finish();
                    break;
                }
                case 0: {
                    Toast.makeText(theActivity.getApplicationContext(), "请求失败...请联系开发者或稍后再试", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    break;
                }
                case 1: {
                    Toast.makeText(theActivity.getApplicationContext(), "出现严重错误...请联系开发者", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    break;
                }
            }
        }
    }

    public void sendRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
                    mBuilder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory())
                            .hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier())
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS);
                    OkHttpClient client = mBuilder.build();
                    FormBody requestBody = new FormBody.Builder()
                            .add("user",username)
                            .add("pass",password)
                            .add("mail",email)
                            .add("name",name)
                            .add("time",timeOption)
                            .add("flag","1")
                            .add("tips","0")
                            .add("type","0")
                            .build();
                    Request request = new Request.Builder()
                            .url("https://card.52pika.cn/add/saves.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        Message message = new Message();
                        message.what = -1;
                        handler.sendMessage(message);
                    }else{
                        handler.sendEmptyMessage(0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    public void cancelRequest(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
                    mBuilder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory())
                            .hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier())
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS);
                    OkHttpClient client = mBuilder.build();
                    FormBody requestBody = new FormBody.Builder()
                            .add("user",username)
                            .build();
                    Request request = new Request.Builder()
                            .url("https://card.52pika.cn/del/saves.php")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()){
                        Message message = new Message();
                        message.what = -1;
                        handler.sendMessage(message);
                    }else{
                        handler.sendEmptyMessage(0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}