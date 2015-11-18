package com.test.material.supitsara.materialnavigationtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Splash extends AppCompatActivity {

    Handler handler;
    Runnable runnable;
    long delay_time;
    long time = 3000L;
    ImageView mLogo;
    private static final String MY_PREFS = "my_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mLogo = (ImageView) findViewById(R.id.logo);
        Glide.with(Splash.this).load(R.drawable.avatar).into(mLogo);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "comfortaa_bold.ttf");
        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "comfortaa_light.ttf");
        TextView textView1 = (TextView) findViewById(R.id.cm);
        TextView textView2 = (TextView) findViewById(R.id.walking_street);
        textView1.setTypeface(typeface1);
        textView2.setTypeface(typeface2);
        handler = new Handler();
        runnable= new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(checkLogin()) {
                    intent = new Intent(Splash.this, MainActivity.class);
                } else {
                    intent = new Intent(Splash.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        delay_time = time;
        handler.postDelayed(runnable, delay_time);
        time = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }

    private boolean checkLogin() {
        SharedPreferences shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        boolean login = shared.getBoolean("session", false);
        return login;
    }
}
