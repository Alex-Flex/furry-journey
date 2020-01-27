package com.alexflex.soft.itipgu.logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alexflex.soft.itipgu.R;

import java.util.ArrayList;

public class ITIRecycler extends RecyclerView.Adapter<ITIRecycler.mViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<MessageITI> messages;
    private Context context;

    ITIRecycler(Context context, ArrayList<MessageITI> messages) {
        layoutInflater = LayoutInflater.from(context);
        this.messages = messages;
        this.context = context;
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
