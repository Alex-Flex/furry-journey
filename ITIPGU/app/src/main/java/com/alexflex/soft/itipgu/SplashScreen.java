package com.alexflex.soft.itipgu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
