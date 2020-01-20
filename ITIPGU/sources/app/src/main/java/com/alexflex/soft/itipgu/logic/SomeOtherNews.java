package com.alexflex.soft.itipgu.logic;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import com.alexflex.soft.itipgu.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;

//применяется для просмотра отличного от новостной ленты контента
public class SomeOtherNews extends AsyncTask<Void, Void, Void> {

    private CommonMethods commonMethods;
    private Context context;
    private ViewGroup layout;
    private Document document;
    private String url;
    private WebView webView;
    private boolean allIsOk = true;

    public SomeOtherNews(Context context, ViewGroup layout, String url, WebView wv){
        this.context = context;
        this.layout = layout;
        this.url = url;
        this.webView = wv;
    }

    protected void onPreExecute(){
        if(!(CommonMethods.isOnline(context))) {
            //если нет интернета, то нет и привета
            CommonMethods.whenImOffline(context, layout, new NewsParserTask(context, layout));
            this.cancel(true);
        }else {
            CommonMethods.generateLoadingScreen(context, layout);
        }
    }

    public Void doInBackground(Void... params){
        try {
            if(isCancelled()) return null;
            document = Jsoup.connect(url).get();
            Elements content = document.select("#content");
            String result = content.get(0).outerHtml();
            document = Jsoup.parse(result);
        } catch (IOException | IllegalArgumentException ignored){

        } catch (IndexOutOfBoundsException e){
            allIsOk = false;
        }
        return null;
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void onPostExecute(Void result){
        if(webView != null) {
            try {
                layout.removeAllViews();
                layout.addView(webView);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new mWebClient());
                if (allIsOk)
                    webView.loadDataWithBaseURL(null, document.toString(), "text/html", document.charset().name(), null);
                else
                    webView.loadUrl(url);
            } catch (NullPointerException e){
                //выводим сообщение о неверной ссылке
                AlertDialog.Builder  dialogBuilder = new AlertDialog.Builder(context)
                        .setTitle(R.string.warning)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setMessage(R.string.invalid_link)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                dialogBuilder.create().show();
            }
        } else {
            try {
                layout.removeAllViews();
                WebView webView = new WebView(context);
                webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new mWebClient());
                layout.addView(webView);
                if (allIsOk)
                    webView.loadDataWithBaseURL(null, document.toString(), "text/html", document.charset().name(), null);
                else
                    webView.loadUrl(url);
            } catch (NullPointerException e){
                //выводим сообщение о неверной ссылке
                AlertDialog.Builder  dialogBuilder = new AlertDialog.Builder(context)
                        .setTitle(R.string.warning)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .setMessage(R.string.invalid_link)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                dialogBuilder.create().show();
            }
        }
    }
}
