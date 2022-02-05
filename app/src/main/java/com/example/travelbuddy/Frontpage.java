package com.example.travelbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

public class Frontpage extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=4000;
    Button front;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);
        new Handler().postDelayed(() -> {
            Intent i=new Intent(Frontpage.this,SignInActivity.class);
            startActivity(i);
            finish();
        },SPLASH_TIME_OUT);

    }
}