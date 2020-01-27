package com.alexflex.soft.itipgu.logic;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

            Document document = Jsoup.connect("http://iti.spsu.ru/").get();
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
        RecyclerView recyclerView = new RecyclerView(context);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new ITIRecycler(context, messages));
        mainView.addView(recyclerView);
    }
}
