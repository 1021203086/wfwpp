package com.cecel.wfwpp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class wfwWebView extends AppCompatActivity {
    private WebView webView;
    public boolean wfwLogin = true;
    String webTitle;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wfw_webview);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        url = intent.getStringExtra("url");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setLogo(R.drawable.arrow_left);
        }
        webView = findViewById(R.id.webview_test);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(url);

        String autoLogin = String.format("javascript:var cases=new Array();cases=document.getElementsByClassName('form-control');" +
                "cases[0].value='%s';cases[1].value='%s';" +
                "document.getElementsByClassName('btn')[0].click();" , username, password);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }


            @Override
            public void onLoadResource(WebView view, String url) {
                if (actionBar!=null){
                    actionBar.setTitle(view.getTitle());
                }
                webView.evaluateJavascript("function getTitle(){return document.title};getTitle();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        webTitle = value;
                        if (webTitle.equals("\"微服务用户认证系统\"") || webTitle.equals("\"CAS Login\""))
                            wfwLogin=false;

                        if(!wfwLogin)
                            webView.evaluateJavascript(autoLogin,null);

                        //用于随机生成动漫头像
                        if(webTitle.equals("\"校园卡明细\"") || webTitle.equals("\"网络在线终端查询\"")) {
                            webView.evaluateJavascript("document.querySelector('section > img')" +
                                    ".setAttribute('src','https://api.btstu.cn/sjtx/api.php');", null);
                        }

                        if (!webTitle.equals("\"成功登陆\"")){
                            webView.evaluateJavascript("document.querySelector('#success_tip > div')" +
                                    ".innerHTML='<a href=\"http://self.scu.edu.cn:8080/selfservice/\">点此跳转到校园网自助服务</a>';", null);
                        }
                    }
                });

                super.onLoadResource(view, url);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}