package vmp.company.vexmall;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DaumZipDialog extends Dialog {

    LinearLayout ll;
    WebView webView;
    TextView zipText;
    TextView address;
    Handler handler;

    public DaumZipDialog(Context context, int id, TextView zipText, TextView address) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        setContentView(id);
        ll = findViewById(R.id.signup_zip_layout);
        webView = findViewById(R.id.daum_zip_webview);
        this.zipText = zipText;
        this.address = address;


        // WebView 초기화
        init_webView();

        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();

    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    public void init_webView() {
        // JavaScript 허용

        webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");
        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());
        // webview url load
        webView.loadUrl("http://vmp.company/vexMall/back/postJoin.php");

    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    zipText.setText(arg1);
                    address.setText(String.format("%s %s", arg2, arg3));

                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();

                    dismiss();
                }
            });
        }
    }

}
