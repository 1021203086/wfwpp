package com.cecel.wfwpp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private EditText username;
    private EditText password;
    private FileOutputStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonLogin= findViewById(R.id.bt_login);
        username = (EditText) findViewById(R.id.ed_1);
        password = (EditText) findViewById(R.id.ed_2);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if (!user.startsWith("20") ||
                        user.isEmpty()||
                        pass.isEmpty()||
                        user.length()!=13) {
                    Toast.makeText(LoginActivity.this, "输入有误请重试!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("username",user);
                    intent.putExtra("password",pass);
                    try {
                        out = openFileOutput("ID", Context.MODE_PRIVATE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FileOptions.saveToFile(out,user,pass);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }


}