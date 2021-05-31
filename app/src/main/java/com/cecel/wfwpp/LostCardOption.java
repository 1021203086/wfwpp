package com.cecel.wfwpp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LostCardOption extends AppCompatActivity implements View.OnClickListener{
    private Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_card_option);
        Intent intent=getIntent();
        String user = intent.getStringExtra("username");
        String pass = intent.getStringExtra("password");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setLogo(R.drawable.arrow_left);
        }
        Button button1 = findViewById(R.id.lost_option1);
        Button button2 = findViewById(R.id.lost_option2);
        intent1 = new Intent(LostCardOption.this,wfwWebView.class);
        intent1.putExtra("username", user);
        intent1.putExtra("password", pass);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lost_option1:{
                intent1.putExtra("url","https://payapp.scu.edu.cn/rfid/voucher/lossWarrant");
                startActivity(intent1);
                break;
            }
            case R.id.lost_option2:{
                intent1.putExtra("url","https://wfw.scu.edu.cn/site/found/index?tabIdx=1");
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