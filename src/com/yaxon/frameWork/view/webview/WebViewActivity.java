package com.yaxon.frameWork.view.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import com.yaxon.frameWork.R;
import com.yaxon.frameWork.debug.ToastUtils;

/**
 * android webView �� js����
 * @author guojiaping 2016-11-10 ����<br>
 */
public class WebViewActivity extends Activity{
    private WebView webView;
    private EditText editText;
    private final static String TAG = "webViewActivity";
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        initView();
        setWebViewParam();
    }

    private void setWebViewParam(){
        String url = "file:///android_asset/webview.html";
        webView.loadUrl(url);

        webView.setVerticalScrollbarOverlay(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsInterface(this),"AndroidWebView");
        webView.setWebChromeClient(new WebChromeClient());

    }

    private void initView(){
        editText = (EditText) findViewById(R.id.input_et);
        webView = (WebView) findViewById(R.id.webView);
    }

    //�ű����õĶ���
    private class JsInterface{
        private Context mContext;
        public JsInterface(Context context){
            this.mContext = context;
        }

        @JavascriptInterface
        public void showInfoFromJs(String name){
            ToastUtils.showShort(mContext,name);
        }
    }

    public void sendInfoToJs(View view){
        String msg = editText.getText().toString();

        //����HTML��js���벢���ݲ���
        webView.loadUrl("javascript:showInfoFromJava('" + msg + "')");
    }
}
