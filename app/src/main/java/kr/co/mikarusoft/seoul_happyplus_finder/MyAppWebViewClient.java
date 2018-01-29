package kr.co.mikarusoft.seoul_happyplus_finder;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by KokoroNihonn3DGame on 2017-10-31.
 */

public class MyAppWebViewClient extends WebViewClient {

    Intent get;

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (Uri.parse(url).getHost().endsWith("http://coska.co.kr/bbs/board.php?bo_table="+get.getExtras().getString("name")+"")) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }
}
