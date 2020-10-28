package com.example.android.fire_learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ncorti.slidetoact.SlideToActView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SlideToActView sta=(SlideToActView) findViewById(R.id.slide_to_act);
        sta.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                Intent transfer=new Intent(SplashScreen.this, Login.class);
                startActivity(transfer);
                finish();
            }
        });
    }
}