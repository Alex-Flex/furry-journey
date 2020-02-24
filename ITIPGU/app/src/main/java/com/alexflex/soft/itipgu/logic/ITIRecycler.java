package com.alexflex.soft.itipgu.logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alexflex.soft.itipgu.R;
import java.util.ArrayList;

public class ITIRecycler extends RecyclerView.Adapter<ITIRecycler.mViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<MessageITI> messages;

    ITIRecycler(Context context, ArrayList<MessageITI> messages) {
        layoutInflater = LayoutInflater.from(context);
        this.messages = messages;
    }

    @Override
    public @NonNull
    ITIRecycler.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (CommonMethods.isNightHere())
            return new ITIRecycler.mViewHolder(layoutInflater.inflate(R.layout.iti_at_night, parent, false));
        else
            return new ITIRecycler.mViewHolder(layoutInflater.inflate(R.layout.iti_at_day, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ITIRecycler.mViewHolder viewHolder, final int position){
        MessageITI m = messages.get(position);
        viewHolder.title.setText(m.getSomething(MessageITI.GET_TITLE));
        viewHolder.date.setText(m.getSomething(MessageITI.GET_DATE));
        viewHolder.message.setText(m.getSomething(MessageITI.GET_MESSAGE));
        viewHolder.message.setOnLongClickListener(new View.OnLongClickListener() {
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
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }

    static class mViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView date;
        final TextView message;

        mViewHolder(View v){
            super(v);
            title = v.findViewById(R.id.title);
            date = v.findViewById(R.id.date);
            message = v.findViewById(R.id.message);
        }
    }
}
