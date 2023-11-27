package com.dev.jtunao.demoapp_10;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.jtunao.demoapp_10.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    static CardsBg[] cardsBg;
    public static BackgroundImg[] backgroundImg;
    public static Integer[] background = {R.drawable.background_1,R.drawable.background_2,R.drawable.background_3,R.drawable.background_4,
            R.drawable.background_5,R.drawable.background_6,R.drawable.background_7,R.drawable.background_8,R.drawable.background_9};
    public static int total_score = 50;
    static int active_bg = R.drawable.card_back1;
    public static Integer main_bg = R.drawable.default_bg;
    public static int cardArrAct = 1;
    private FirebaseAnalytics firebaseAnalytics;

    static Integer[] backArr = {R.drawable.card_back1,R.drawable.card_back2,R.drawable.card_back3,R.drawable.card_back4,R.drawable.new_cards_bg_2r};


    static int card_count = 24;
    static Boolean sound = true;
    static MediaPlayer mediaPlayer;
    static Integer main_snd_theme = R.raw.play_snd_neongaming;
    static Snd_control main_snd = null;

    static Settings settings;


    private Boolean LOAD_SETTING_STATUS = false;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
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
        init();

        Button exitBtn = findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //get setup background images
        getBackgroundImg();
        RelativeLayout layout = findViewById(R.id.main_layout);
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

    private void init() {
        ImageView store_btn  = findViewById(R.id.store_btn);
        store_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStore(v);
            }
        });
        jsonParse();
        getSettings();
        TextView textView = findViewById(R.id.moneyScore);
        textView.setText(Integer.toString(total_score));
        if (main_snd == null){
            main_snd = new Snd_control(true,1,main_snd_theme,this);
            if (sound){
                main_snd.play();
            }
        }else {
            if (sound){
                main_snd.play();
            }else {
                main_snd.stop();
            }
        }
    }

    public void goToMainGameScreen(View view) {
        Intent intent = new Intent(this, playActivity.class);
        startActivity(intent);
    }
    public void goToStore(View view) {
        Intent intent = new Intent(this, storeActivity.class);
        startActivity(intent);
    }
    public void goToSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void jsonParse() {
        String fileName = "card.json";
        File file = new File(getFilesDir(),fileName);
        if (!file.exists()){
            //System.out.println("cards bg from default");
            String file_name = "";
            InputStream inputStream = getResources().openRawResource(R.raw.cards);
            try {
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                String jsonString = new String(buffer, "UTF-8");
                JsonElement jsonElement = JsonParser.parseString(jsonString);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has("cards_bg") && jsonObject.get("cards_bg").isJsonArray()) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("cards_bg");
                        Gson gson = new Gson();
                        cardsBg = gson.fromJson(jsonArray, CardsBg[].class);
                        for (int o=0;o<cardsBg.length;o++) {
                            //System.out.println("start chek");
                            if (cardsBg[o].getIs_active()) {
                                active_bg = backArr[o];
                            }
                        }
                    }
                }
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }else{
            try {
                //System.out.println("load cards bg from local files");
                FileReader reader = new FileReader(file);
                Gson gson = new Gson();
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(reader);
                System.out.println("is obj json : "+ jsonElement.isJsonArray());
                cardsBg = gson.fromJson(jsonElement, CardsBg[].class);

                for (int o = 0; o < cardsBg.length; o++) {
                  //  System.out.println("start check");
                    if (cardsBg[o].getIs_active()) {
                        active_bg = backArr[o];
                    }
                }
                //System.out.println("load successful");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        }
    }

    private void getSettings(){
        String fileName = "setting.json";
        String music_name = "Main Theme";
        File file = new File(getFilesDir(),fileName);
        if (!file.exists()) {
            System.out.println("settings loading from default");
            InputStream inputStreamSetting = getResources().openRawResource(R.raw.settings);
            try {
                int size = inputStreamSetting.available();
                byte[] bufferSetting = new byte[size];
                inputStreamSetting.read(bufferSetting);
                String jsonSettingString = new String(bufferSetting, "UTF-8");
                Gson gson = new Gson();
                settings = gson.fromJson(jsonSettingString, Settings.class);
                //System.out.println(settings);
                total_score = settings.getMoney();
                card_count = settings.getCard_count();
                sound = settings.getSound();
                cardArrAct = settings.getActivated_card();
                music_name = settings.getSound_name();
                if (music_name == null){
                    music_name = "Main theme";
                }
                main_snd_theme = getMusicIndex(music_name);
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }else{
            System.out.println("load settings from local file");
            try {
                //System.out.println("load cards bg from local files");
                FileReader reader = new FileReader(file);
                Gson gson = new Gson();
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(reader);
                settings = gson.fromJson(jsonElement, Settings.class);
                //System.out.println(settings);
                total_score = settings.getMoney();
                card_count = settings.getCard_count();
                sound = settings.getSound();
                music_name = settings.getSound_name();
                if (music_name == null){
                    music_name = "Main theme";
                }
                main_snd_theme = getMusicIndex(music_name);
                System.out.println("setting loaded: "+ card_count + " " + total_score + " " + sound);
                //System.out.println("load successful");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GsonBuilder builder = new GsonBuilder();
        Gson gsonUpdate = builder.create();
        String jsonString = gsonUpdate.toJson(cardsBg);
        try (FileWriter fileWriter = new FileWriter("card.json")){
            fileWriter.write(jsonString);
        }catch (IOException e){
            e.printStackTrace();
        }
        saveSetting();
        saveBg();
    }
    private void saveSetting(){
        String fileName = "setting.json";
        File file = new File(getFilesDir(), fileName);
        settings.setCard_count(card_count);
        settings.setMoney(total_score);
        settings.setSound(sound);
        settings.setSound_name(getMusicName(main_snd_theme));
        settings.setActivated_card(cardArrAct);
        GsonBuilder builderSetting = new GsonBuilder();
        Gson gsonUpdateSetting = builderSetting.create();
        String jsonStringSetting = gsonUpdateSetting.toJson(settings);
        try (FileWriter fileWriter = new FileWriter(file)){
            fileWriter.write(jsonStringSetting);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private Integer getMusicIndex(String music){
        Integer name = R.raw.def_snd;
        //{"Neon gaming", "Novembers", "Stay Retro", "Strangers","Christmas","Main theme"};//in next versions
        switch (music){
            case "Main theme": name = R.raw.def_snd; break;
            case "Neon Gaming": name = R.raw.play_snd_neongaming; break;
            case "Novembers": name = R.raw.play_snd_novembers; break;
            case "Stay Retro": name = R.raw.play_snd_stay_retro; break;
            case "Christmas": name = R.raw.play_snd_1; break;
            case "Strangers": name = R.raw.play_snd_stranger_things; break;
        }
        return name;
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

    private void getBackgroundImg(){
        String fileName = "background.json";
        File file = new File(getFilesDir(),fileName);
        if (!file.exists()){
            System.out.println("bg from default bg");
            InputStream inputStream = getResources().openRawResource(R.raw.background);
            try {
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                String jsonString = new String(buffer, "UTF-8");
                JsonElement jsonElement = JsonParser.parseString(jsonString);
                if (jsonElement.isJsonObject()) {
                    System.out.println("first if");
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    System.out.println("json object is: " + jsonObject);
                    System.out.println("JSON chekers: " +jsonObject.has("background_img"));
                    System.out.println("JSON chekers: " +jsonObject.get("background_img").isJsonArray());
                    if (jsonObject.has("background_img") && jsonObject.get("background_img").isJsonArray()) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("background_img");
                        System.out.println("second if");
                        Gson gson = new Gson();
                        backgroundImg = gson.fromJson(jsonArray, BackgroundImg[].class);
                        for (int o=0;o<backgroundImg.length;o++) {
                            System.out.println("start chek bg");
                            if (backgroundImg[o].getIs_active()) {
                                main_bg = background[o];
                            }
                        }
                        System.out.println("load successful 1");
                    }
                }
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }else{
            try {
                System.out.println("load  bg from local files");
                FileReader reader = new FileReader(file);
                Gson gson = new Gson();
                JsonParser jsonParser = new JsonParser();
                JsonElement jsonElement = jsonParser.parse(reader);
                System.out.println("is obj json : "+ jsonElement.isJsonArray());
                backgroundImg = gson.fromJson(jsonElement, BackgroundImg[].class);
                for (int o = 0; o < backgroundImg.length; o++) {
                      System.out.println("start check bg");
                    if (backgroundImg[o].getIs_active()) {
                        main_bg = background[o];
                    }
                }
                System.out.println("load successful");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }


        }

    }
    private void saveBg(){
        GsonBuilder builder = new GsonBuilder();
        Gson gsonUpdate = builder.create();
        String jsonString = gsonUpdate.toJson(backgroundImg);
        try (FileWriter fileWriter = new FileWriter("background.json")){
            fileWriter.write(jsonString);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}