package com.dev.jtunao.demoapp_10;

import static com.dev.jtunao.demoapp_10.MainActivity.mediaPlayer;
import static com.dev.jtunao.demoapp_10.MainActivity.sound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class privacy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_privacy);
        ImageView menu_btn = findViewById(R.id.back_to_menu);
        WebView webView = findViewById(R.id.web_priv);
        webView.loadUrl("https://www.termsfeed.com/live/a9bf44c1-6aa7-4703-a273-59e12e108bb1");

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu(v);
            }
        });
        if (mediaPlayer.isPlaying()==false&&sound == true){
            mediaPlayer.start();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer.isPlaying()== false && sound == true){
            mediaPlayer.start();
        }
    }
    public void backToMenu(View v){
        finish();
    }



}