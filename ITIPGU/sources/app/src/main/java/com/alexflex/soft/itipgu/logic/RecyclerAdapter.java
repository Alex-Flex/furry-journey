package com.alexflex.soft.itipgu.logic;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alexflex.soft.itipgu.MoreOnNews;
import com.alexflex.soft.itipgu.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.mViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<Message> messages;
    private final String INTENT_EXTRA_NAME = "link";
    private Context context;

    RecyclerAdapter(Context context, ArrayList<Message> messages) {
        layoutInflater = LayoutInflater.from(context);
        this.messages = messages;
        this.context = context;
    }

    @Override
    public @NonNull RecyclerAdapter.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (CommonMethods.isNightHere())
            return new mViewHolder(layoutInflater.inflate(R.layout.news_at_night,parent, false));
        else
            return new mViewHolder(layoutInflater.inflate(R.layout.news_at_daylight, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder viewHolder, final int position){
            Message m = messages.get(position);
            viewHolder.newsText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MoreOnNews.class);
                    intent.putExtra(INTENT_EXTRA_NAME, messages.get(position).getSomething(Message.GET_LINK));
                    context.startActivity(intent);
                }
            });
            viewHolder.newsText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Копируем ссылку на новость при долгом нажатии
                    Context context = v.getContext();
                    CommonMethods.copyToClipboard(context, "label", messages.get(position)
                            .getSomething(Message.GET_LINK));
                    Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
            viewHolder.caption.setText(m.getSomething(Message.GET_CAPTION));
            viewHolder.newsText.setText(m.getSomething(Message.GET_MESSAGE));
            viewHolder.date.setText(m.getSomething(Message.GET_DATE));
            viewHolder.views.setText(m.getSomething(Message.GET_VIEWS));
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }

    static class mViewHolder extends RecyclerView.ViewHolder {
        final TextView caption;
        final TextView newsText;
        final ImageView image;
        final TextView date;
        final TextView views;

        mViewHolder(View v){
            super(v);
            caption = v.findViewById(R.id.caption);
            newsText = v.findViewById(R.id.news_text);
            image = v.findViewById(R.id.picture);
            date = v.findViewById(R.id.date);
            views = v.findViewById(R.id.views);
        }
    }
}
