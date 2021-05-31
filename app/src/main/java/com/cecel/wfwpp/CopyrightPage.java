package com.cecel.wfwpp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CopyrightPage extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright_page);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setLogo(R.drawable.arrow_left);
        }
        textView = findViewById(R.id.copyright_text);
        Copyright copyright = Copyright.getInstance();
        String[] group = copyright.getGroupMembers();
        String info = "项目名称："+copyright.getAPPNAME()+"\n\n\n"
                +"项目组员："+group[0]+","+group[1]+"\n\n\n"
                +"联系我们：(QQ)"+copyright.getCONTACT()+"\n\n\n"
                +"All Rights Reserved.";
        textView.setText(info);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}