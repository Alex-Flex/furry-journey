package com.alexflex.soft.itipgu.logic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexflex.soft.itipgu.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class ITIParserTask extends AsyncTask<Void, Void, Void> {
    private ArrayList<MessageITI> messages = new ArrayList<>();
    private Context context;
    private ViewGroup mainView;

    public ITIParserTask(Context context, ViewGroup mainView) {
        this.context = context;
        this.mainView = mainView;
    }

    @Override
    protected void onPreExecute(){
        if(!(CommonMethods.isOnline(context))) {
            //если нет интернета, то нет и привета
            CommonMethods.whenImOffline(context, mainView, new NewsParserTask(context, mainView));
            this.cancel(true);
        }else {
            CommonMethods.generateLoadingScreen(context, mainView);
        }
    }

    @Override
    protected Void doInBackground(Void... params){
        try {
            //Если задание отменено, то не выполняем ничего
            if(isCancelled()) return null;

            Document document = Jsoup.connect(CommonMethods.urlITI).get();
            Elements news_cards = document.getElementsByTag("article");
            for(Element card : news_cards) {
                messages.add(new MessageITI(card.select("h2.article-title").get(0).text(),
                        card.getElementsByTag("time").get(0).text(),
                        card.getElementsByTag("section").get(0).text()));
            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (NullPointerException ignored) { }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        mainView.removeAllViews();
        if (messages.size() == 0) {
            TextView tw = new TextView(context);
            tw.setText(R.string.no_server);
            tw.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
            tw.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            if (CommonMethods.isNightHere()) {
                tw.setTextColor(Color.WHITE);
            } else {
                tw.setTextColor(Color.BLACK);
            }
            tw.setGravity(Gravity.CENTER);
            mainView.addView(tw);
        } else {
            RecyclerView recyclerView = new RecyclerView(context);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(new ITIRecycler(context, messages));
            mainView.addView(recyclerView);
        }
    }
}
