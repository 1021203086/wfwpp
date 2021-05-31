package com.cecel.wfwpp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class CourseWebview extends AppCompatActivity {
    private WebView webView;
    private String temp;
    private String username;

    /**
     * 用于接收webview运行JavaScript的结果，并在主线程中发出Toast
     */
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:{
                    Toast.makeText(CourseWebview.this, "获取成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CourseWebview.this,CourseTable.class);
                    intent.putExtra("courseData",temp);
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
                }
                case -1:{
                    Toast.makeText(CourseWebview.this,"获取异常，请检查课表信息是否已经加载！",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_webview);
        webView = findViewById(R.id.course_webview);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setLogo(R.drawable.arrow_left);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //以下用于设置缩放
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl("http://zhjw.scu.edu.cn/student/courseSelect/thisSemesterCurriculum/index");

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(url.equals("http://zhjw.scu.edu.cn/login")){
                    webView.evaluateJavascript("document.getElementById('input_username').value="+username+";" ,null);
                }
                super.onPageFinished(view, url);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.course_webview,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.course_import:{
                webView.evaluateJavascript("function getData(){return document.getElementsByTagName('tbody')[1].innerText};" +
                                "getData();",
                        new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        temp = value.substring(1,value.length()-1);
                        Log.i("course data", temp);         //打印课程原始数据
                        if (temp!=null && !temp.equals("ul"))
                            handler.sendEmptyMessage(1);
                        else
                            handler.sendEmptyMessage(-1);
                    }
                });
                break;
            }
            case R.id.course_refresh:{
                webView.reload();
                break;
            }
        }
        return true;
    }

    //显示返回按钮
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    //若按下返回键，返回上一个页面而不是直接退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}