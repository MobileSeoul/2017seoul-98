package kr.co.mikarusoft.seoul_happyplus_finder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = (WebView)findViewById(R.id.webview);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        Intent getExtra = getIntent();
        String n = getExtra.getExtras().getString("name");

        webView.loadUrl("http://coska.co.kr/bbs/board.php?bo_table="+n+"");

        getExtra.putExtra("name", n);

        webView.setWebViewClient(new WebViewClient());

    }

    public void onBackPressed(){
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}