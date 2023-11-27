package com.dev.jtunao.demoapp_10;

import static com.dev.jtunao.demoapp_10.MainActivity.backgroundImg;
import static com.dev.jtunao.demoapp_10.MainActivity.card_count;
import static com.dev.jtunao.demoapp_10.MainActivity.cardsBg;
import static com.dev.jtunao.demoapp_10.MainActivity.main_bg;
import static com.dev.jtunao.demoapp_10.MainActivity.main_snd;
import static com.dev.jtunao.demoapp_10.MainActivity.main_snd_theme;
import static com.dev.jtunao.demoapp_10.MainActivity.mediaPlayer;
import static com.dev.jtunao.demoapp_10.MainActivity.settings;
import static com.dev.jtunao.demoapp_10.MainActivity.sound;
import static com.dev.jtunao.demoapp_10.MainActivity.total_score;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.jtunao.BackgroundFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class storeActivity extends AppCompatActivity {

    private AdView mAdView;

    private StoreBackFragment storeBackFragment = new StoreBackFragment();
    private BackgroundFragment backgroundFragment = new BackgroundFragment();
    private CardsFragment cardsFragment = new CardsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_store);
        init();
        ImageView img_menu = findViewById(R.id.back_to_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu(v);
            }
        });

        RelativeLayout layout = findViewById(R.id.main_layout_store);
        layout.setBackgroundResource(main_bg);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (main_snd.isPlay()){
            main_snd.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!main_snd.isPlay()&&sound){
            main_snd.play();
        }
    }
    private void  init(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                System.out.println("ad failed to load with code: "+ adError);
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                System.out.println("ad successful load");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
        TextView score = findViewById(R.id.moneyScore);
        score.setText(Integer.toString(total_score));

        Button background_btn = findViewById(R.id.back_btn);
        Button backside_cards_btn = findViewById(R.id.cards_btn);
        Button cards_btn = findViewById(R.id.cards_change_btn);
        background_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(backgroundFragment);
            }
        });
        backside_cards_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(storeBackFragment);
            }
        });
        cards_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(cardsFragment);
            }
        });

    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }
    public void backToMenu(View view){
        saveData();
        saveSetting();
        saveDataBackground();
        finish();
    }
    private void saveData(){
        String fileName = "card.json";
        File file = new File(getFilesDir(), fileName);
        // System.out.println("try save data");
        GsonBuilder builder = new GsonBuilder();
        Gson gsonUpdate = builder.create();
        String jsonString = gsonUpdate.toJson(cardsBg);
        try (FileWriter fileWriter = new FileWriter(file)){
            fileWriter.write(jsonString);
            // System.out.println("data saved successful");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void saveDataBackground(){
        String fileName = "background.json";
        File file = new File(getFilesDir(), fileName);
        // System.out.println("try save data");
        GsonBuilder builder = new GsonBuilder();
        Gson gsonUpdate = builder.create();
        String jsonString = gsonUpdate.toJson(backgroundImg);
        try (FileWriter fileWriter = new FileWriter(file)){
            fileWriter.write(jsonString);
            // System.out.println("data saved successful");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void saveSetting(){
        String fileName = "setting.json";
        File file = new File(getFilesDir(), fileName);
        settings.setCard_count(card_count);
        settings.setMoney(total_score);
        settings.setSound(sound);
        settings.setSound_name(getMusicName(main_snd_theme));
        GsonBuilder builderSetting = new GsonBuilder();
        Gson gsonUpdateSetting = builderSetting.create();
        String jsonStringSetting = gsonUpdateSetting.toJson(settings);
        try (FileWriter fileWriter = new FileWriter(file)){
            fileWriter.write(jsonStringSetting);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private String getMusicName(Integer music){
        String name = "";
        //{"Neon gaming", "Novembers", "Stay Retro", "Strangers","Christmas","Main theme"};//in next versions
        switch (music){
            case R.raw.def_snd: name = "Main theme"; break;
            case R.raw.play_snd_neongaming: name = "Neon Gaming"; break;
            case R.raw.play_snd_novembers: name = "Novembers"; break;
            case R.raw.play_snd_stay_retro: name = "Stay Retro"; break;
            case R.raw.play_snd_1: name = "Christmas"; break;
            case R.raw.play_snd_stranger_things: name = "Strangers"; break;
        }
        return name;
    }
}