package com.alexflex.soft.itipgu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alexflex.soft.itipgu.logic.CommonMethods;

public class AboutAppActivity extends AppCompatActivity implements View.OnClickListener {

    private final String VKONTAKTE = "VK";
    private final String TELEGRAM = "TG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        ImageView vk = findViewById(R.id.vk_info);
        ImageView telega = findViewById(R.id.telegram_info);
        vk.setOnClickListener(this);
        telega.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.vk_info:
                    CommonMethods.openInOtherApp(AboutAppActivity.this, VKONTAKTE, "https://vk.com/alex_sylverfox");
                    break;
                case R.id.telegram_info:
                    CommonMethods.openInOtherApp(AboutAppActivity.this, TELEGRAM, "https://t.me/AAlexAlexander");
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(AboutAppActivity.this, R.string.no_activity, Toast.LENGTH_LONG).show();
        }
    }
}
