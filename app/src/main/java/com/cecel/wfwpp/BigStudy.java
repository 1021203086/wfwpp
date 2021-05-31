package com.cecel.wfwpp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BigStudy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_study);
        WebView webView = findViewById(R.id.webview_study);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        //webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString() + " MQQBrowser/6.2 TBS/043807 Mobile Safari/537.36 MicroMessenger/6.6.1.1220(0x26060135) NetType/WIFI Language/zh_CN");
        webView.loadUrl("http://dxx.scyol.com/v_prod6.0.2/?code=001GDb100APuML13dt000Ovyzy1GDb1E&state=123#/pages/isRegister/isRegister?openid=o7Y3ujrUz4dN0d2NB9TQEFQ9MEQc&snum=10&name=%E7%AC%AC%E5%8D%81%E4%B8%80%E5%AD%A3&url=https%3A%2F%2Fh5.cyol.com%2Fspecial%2Fdaxuexi%2Fa5je1cd9rd%2Fm.html");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if (view.getTitle().contains("第")){
                    //用于选择城市的JavaScript代码
                    view.evaluateJavascript("document.getElementById('province').value=23;" +
                            "changeCity(23);" +
                            "document.getElementById('city').value=1;" +
                            "document.getElementsByClassName('sure')[0].click();" +
                            "document.getElementsByClassName('start_btn')[0].click();" +
                            "var video=document.getElementById('Bvideo');" +
                            "var len=video.duration;" +
                            "video.currentTime=len;" ,null);
                }
                super.onLoadResource(view, url);
            }
        });

    }

}