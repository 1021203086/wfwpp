package com.cecel.wfwpp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SchoolNetOption extends AppCompatActivity implements View.OnClickListener{
    private Intent intent1;
    Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_net_option);
        Intent intent=getIntent();
        String user = intent.getStringExtra("username");
        String pass = intent.getStringExtra("password");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setLogo(R.drawable.arrow_left);
        }

        Button button1 = findViewById(R.id.net_option1);
        Button button2 = findViewById(R.id.net_option2);
        button3 = findViewById(R.id.net_option3);
        intent1 = new Intent(SchoolNetOption.this,wfwWebView.class);
        intent1.putExtra("username", user);
        intent1.putExtra("password", pass);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.net_option1:{
                intent1.putExtra("url","https://payapp.scu.edu.cn/net_fee_change/");
                startActivity(intent1);
                break;
            }
            case R.id.net_option2:{
                intent1.putExtra("url","https://wfw.scu.edu.cn/site/network/device");
                startActivity(intent1);
                break;
            }
            case R.id.net_option3:{
                intent1.putExtra("url","http://192.168.2.135");
                startActivity(intent1);
                break;
            }
            default:break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}