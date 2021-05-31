package com.cecel.wfwpp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private boolean isLogin=false;
    private String username;
    private String password;
    private FileInputStream in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String temp;
        Button buttonToLogin = findViewById(R.id.bt_to_login);
        //读取用户信息文件
        try {
            in = openFileInput("ID");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (in != null && !(temp=FileOptions.loadFromFile(in)).isEmpty() ){
            String[] studentID = MD5Util.decode(temp).split("#");
            username = studentID[0];
            password = studentID[1];
            buttonToLogin.setVisibility(View.GONE);
            isLogin = true;
        }
        Button button1 = findViewById(R.id.jkmrb);   //健康每日报
        Button button2 = findViewById(R.id.xykmx);   //校园卡明细
        Button button3 = findViewById(R.id.kbcx);    //课表查询
        Button button4 = findViewById(R.id.xywxg);   //校园网相关
        Button button5 = findViewById(R.id.qndxx);   //青年大学习
        Button button6 = findViewById(R.id.xykgszl); //校园卡挂失招领

        buttonToLogin.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);

    }
        @Override
        public void onClick(View v) {       //设置监听器
            switch (v.getId()){
                case R.id.bt_to_login:{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent,0);
                    break;
                }
                case R.id.jkmrb:{
                    if(!isLogin){
                        Toast.makeText(MainActivity.this, "您似乎还未登录...", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Intent intent = new Intent(MainActivity.this, AutoSign.class);
                    intent.putExtra("username",username);
                    intent.putExtra("password",password);
                    startActivityForResult(intent,1);
                    break;
                }
                case R.id.qndxx:{
                    if(!isLogin){
                        Toast.makeText(MainActivity.this, "您似乎还未登录...", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Intent intent = new Intent(MainActivity.this,BigStudy.class);
                    startActivity(intent);
                    break;
                }
                case R.id.kbcx:{
                    if(!isLogin){
                        Toast.makeText(MainActivity.this, "您似乎还未登录...", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Intent intent = new Intent(MainActivity.this,CourseTable.class);
                    intent.putExtra("username",username);
                    intent.putExtra("password",password);
                    startActivity(intent);
                    break;
                }
                case R.id.xykmx:{
                    if(!isLogin){
                        Toast.makeText(MainActivity.this, "您似乎还未登录...", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Intent intent = new Intent(MainActivity.this, wfwWebView.class);
                    intent.putExtra("username",username);
                    intent.putExtra("password",password);
                    intent.putExtra("url","https://wfw.scu.edu.cn/site/idCard/history");
                    startActivity(intent);
                    break;
                }
                case R.id.xywxg:{
                    if(!isLogin){
                        Toast.makeText(MainActivity.this, "您似乎还未登录...", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Intent intent = new Intent(MainActivity.this,SchoolNetOption.class);
                    intent.putExtra("username",username);
                    intent.putExtra("password",password);
                    startActivity(intent);
                    break;
                }
                case R.id.xykgszl:{
                    if(!isLogin){
                        Toast.makeText(MainActivity.this, "您似乎还未登录...", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Intent intent = new Intent(MainActivity.this,LostCardOption.class);
                    intent.putExtra("username",username);
                    intent.putExtra("password",password);
                    startActivity(intent);
                    break;
                }
                default:break;
        }
    }

    //用于处理活动加载后的返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:{
                if(resultCode == RESULT_OK) {
                    username = data.getStringExtra("username");
                    password = data.getStringExtra("password");
                    Toast.makeText(MainActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    isLogin = true;
                    Button bt = findViewById(R.id.bt_to_login);
                    bt.setVisibility(View.GONE);
                }else {
                    Toast.makeText(MainActivity.this, "你取消了登录！", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case 1:{
                if(resultCode == RESULT_OK){
                    int submitOption = data.getIntExtra("submitOption",-1);
                    String text;
                    if(submitOption == R.id.start)
                        text="自动打卡提交成功！";
                    else if(submitOption == R.id.close)
                        text="撤销打卡提交成功！";
                    else
                        text="可能发生了奇怪的错误，请联系开发者...";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }else if (resultCode == RESULT_CANCELED){
                    Toast.makeText(getApplicationContext(), "你取消了请求！", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:break;
        }
    }


    //添加右上角菜单按钮
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    //设置右上角菜单按钮的监听器
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_quit_login:{
                if (isLogin){
                    deleteFile("ID");
                    deleteFile("CourseInfo");

                    Button buttonToLogin = findViewById(R.id.bt_to_login);
                    buttonToLogin.setVisibility(View.VISIBLE);
                    isLogin = false;
                }else
                    Toast.makeText(MainActivity.this, "您似乎还未登录...", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "您已退出登录", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menu_about:{
                Intent intent = new Intent(MainActivity.this,CopyrightPage.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }
}