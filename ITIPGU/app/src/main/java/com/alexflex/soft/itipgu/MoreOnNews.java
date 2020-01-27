package com.alexflex.soft.itipgu;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.alexflex.soft.itipgu.logic.SomeOtherNews;
import com.alexflex.soft.itipgu.logic.mWebClient;

public class MoreOnNews extends AppCompatActivity {

    private ViewGroup cl;
    private WebView w;
    private ActionBar actionBar;
    private final String INTENT_EXTRA_NAME = "link";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_on_news);
        cl = findViewById(R.id.main_view);
        actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.message);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onStart(){
            super.onStart();
            w = new WebView(this);
            w.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            w.getSettings().setJavaScriptEnabled(true);
            w.setWebViewClient(new mWebClient());
            new SomeOtherNews(MoreOnNews.this, cl, getIntent().getStringExtra(INTENT_EXTRA_NAME), w).execute();
    }

    @Override
    public void onResume(){
        if(w != null){
            cl.removeAllViews();
            cl.addView(w);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed(){
        if(w.canGoBack()){
            w.goBack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cl = null;
        w = null;
        actionBar = null;
    }
}
