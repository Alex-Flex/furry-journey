package com.alexflex.soft.itipgu.logic;

import android.content.Context;
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
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class NewsParserTask extends AsyncTask<Void, Void, Void> {

    private ArrayList<Message> messages = new ArrayList<>();
    private Context context;
    private ViewGroup mainView;

    public NewsParserTask(Context context, ViewGroup mainView) {
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

            Document document = Jsoup.connect(CommonMethods.urlITK).get();

            //приступаем к выбору элементов
            Elements msgs = document.getElementsByClass("Message");
            Elements titles = document.getElementsByClass("eTitle");

            //выбираем и помещаем в глобальную переменную класса
            for (int i=0;i<msgs.size();i++) {
                if (isCancelled()) return null;
                if (msgs.get(i).toString().contains("img")) {
                    //в сообщении есть фото
                    Message message = new Message(document.getElementsByClass("e-author").get(i).text(),
                            msgs.get(i).text(),
                            document.getElementsByClass("e-date").get(i).text(),
                            document.getElementsByClass("e-reads").get(i).text(),
                            titles.get(i).select("a").attr("href"),
                            msgs.get(i).getElementsByTag("img").get(0).attr("src"));

                    //добавляем только что полученные данные в новостную ленту
                    messages.add(message);
                } else {
                    //в сообщении нет фото
                    Message message = new Message(document.getElementsByClass("e-author").get(i).text(),
                            msgs.get(i).text(),
                            document.getElementsByClass("e-date").get(i).text(),
                            document.getElementsByClass("e-reads").get(i).text(),
                            titles.get(i).select("a").attr("href"),
                            null);

                    //добавляем только что полученные данные в новостную ленту
                    messages.add(message);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        } catch (NullPointerException ignored) { }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
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
            recyclerView.setAdapter(new RecyclerAdapter(context, messages));
            mainView.addView(recyclerView);
        }
    }
}
