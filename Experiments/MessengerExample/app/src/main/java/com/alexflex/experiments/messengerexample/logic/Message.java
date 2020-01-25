package com.alexflex.experiments.messengerexample.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String username;
    private String text;
    private @ColorInt int color;
    private ArrowDirection arrowDirection;

    public Message(){}
    public Message(@NonNull String username,
                   @NonNull String text,
                   @ColorInt int bubbleColor,
                   ArrowDirection arrowDirection) {
        this.username = username;
        this.text = text;
        this.color = bubbleColor;
        this.arrowDirection = arrowDirection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @SuppressLint("SimpleDateFormat")
    public void drawIt(@NonNull ViewGroup parent) {
        Context context = parent.getContext();

        //создаём новые группы видов
        BubbleLayout bubbleLayout = new BubbleLayout(context);
        LinearLayout linearLayout = new LinearLayout(context);
        TextView name = new TextView(context);
        TextView yourWishes = new TextView(context);
        TextView date = new TextView(context);

        //работаем с созданными ранее элементами
        bubbleLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        linearLayout.setLayoutParams(params);
        name.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        yourWishes.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        date.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        name.setText(username);
        yourWishes.setText(text);
        date.setText(new SimpleDateFormat("dd-mm-yyyy hh:mm").format(new Date()));

        name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        name.setTextColor(Color.GRAY);

        yourWishes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        yourWishes.setTextColor(Color.WHITE);

        yourWishes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 7);
        yourWishes.setTextColor(Color.GRAY);

        bubbleLayout.setCornersRadius(7);
        bubbleLayout.setBubbleColor(color);
        bubbleLayout.setArrowDirection(arrowDirection);

        linearLayout.addView(name);
        linearLayout.addView(yourWishes);
        linearLayout.addView(date);
        bubbleLayout.addView(linearLayout);

        parent.addView(bubbleLayout);
    }
}
