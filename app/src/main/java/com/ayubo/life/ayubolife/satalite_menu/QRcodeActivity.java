package com.ayubo.life.ayubolife.satalite_menu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ayubo.life.ayubolife.R;
import com.ayubo.life.ayubolife.activity.PrefManager;
import com.ayubo.life.ayubolife.rest.ApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_APPS;
import static com.ayubo.life.ayubolife.rest.ApiClient.MAIN_URL_LIVE_HAPPY;

public class QRcodeActivity extends AppCompatActivity {
    private PrefManager pref;String hasToken,encodedHashToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        pref = new PrefManager(QRcodeActivity.this);


        hasToken=pref.getLoginUser().get("hashkey");
        try {
            encodedHashToken = URLEncoder.encode(hasToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebView webview = (WebView) findViewById(R.id.webViewwww);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest (final WebView view, String url) {
                if(url.startsWith(MAIN_URL_APPS)){
                    System.out.println("=====Timeline Callback========"+url);
                    return getCssWebResourceResponseFromAsset();
                } else {

                    return super.shouldInterceptRequest(view, url);
                }
            }

            /**
             * Return WebResourceResponse with CSS markup from a String.
             */
            @SuppressWarnings("deprecation")
            private WebResourceResponse getCssWebResourceResponseFromString() {
                return getUtf8EncodedCssWebResourceResponse(new StringBufferInputStream("body { background-color: #F781F3; }"));
            }

            /**
             * Return WebResourceResponse with CSS markup from an asset (e.g. "assets/style.css").
             */
            private WebResourceResponse getCssWebResourceResponseFromAsset() {
                try {
                    return getUtf8EncodedCssWebResourceResponse(getAssets().open("style.css"));
                } catch (IOException e) {
                    return null;
                }
            }

            /**
             * Return WebResourceResponse with CSS markup from a raw resource (e.g. "raw/style.css").
             */
            private WebResourceResponse getCssWebResourceResponseFromRawResource() {
                return null;
            }

            private WebResourceResponse getUtf8EncodedCssWebResourceResponse(InputStream data) {
                return new WebResourceResponse("text/css", "UTF-8", data);
            }

        });
       String url = MAIN_URL_LIVE_HAPPY + "index.php?entryPoint=ayuboLifeTimelineLogin&ref=";
        webview.loadUrl(url+encodedHashToken);


    }
}
