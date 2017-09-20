package error.wx.h5.com.insteadshowerror;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.bt2)
    Button bt2;
    private WebSettings settings;

    private View mErrorView;

    Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initErrorPage();
        checkurl("https://www.baidu.com");
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkurl("https://www.360.cn/");

            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkurl("https://www.baidu.com");

            }
        });

    }

    public void checkurl(String url){


        if (!Netutils.isNetworkAvalible(getApplicationContext())) {
            fl.removeAllViews();
            webView.loadUrl(url);
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            fl.setVisibility(View.VISIBLE);
            fl.addView(mErrorView);

        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fl.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    fl.removeAllViews();
                }
            }, 1000);

            webView.loadUrl(url);
            settings = webView.getSettings();
            settings.setBuiltInZoomControls(true);//出现+,-号放大缩小,在wap网页上无效
            settings.setUseWideViewPort(true);//出现+,-号放大缩小,在wap网页上无效
            settings.setJavaScriptEnabled(true);//wap网页
            settings.setSaveFormData(true);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    if (title.contains("404")) {
                        showErrorPage();
                    }
                }
            });


            webView.setWebViewClient(new WebViewClient() {

                //确保在本应用中打开
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    showErrorPage();
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    showErrorPage();
                }

            });
        }
    }


    private void showErrorPage() {

        fl.removeAllViews();
        webView.setVisibility(View.GONE);
        fl.setVisibility(View.VISIBLE);
        fl.addView(mErrorView);
    }



    //下面这个要初始化在oncreate中
    private void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(this, R.layout.page_error, null);
            Button button = (Button) mErrorView.findViewById(R.id.btn_reload);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (Netutils.isNetworkAvalible(getApplicationContext())) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webView.setVisibility(View.VISIBLE);
                                fl.setVisibility(View.GONE);
                                fl.removeAllViews();
                            }
                        },1200);
                        webView.reload();
                    } else {
                        return;
                    }


                }
            });
        }
    }

}
