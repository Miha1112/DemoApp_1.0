package com.dev.jtunao.demoapp_10;

import static com.dev.jtunao.demoapp_10.MainActivity.card_count;
import static com.dev.jtunao.demoapp_10.MainActivity.main_bg;
import static com.dev.jtunao.demoapp_10.MainActivity.main_snd;
import static com.dev.jtunao.demoapp_10.MainActivity.main_snd_theme;
import static com.dev.jtunao.demoapp_10.MainActivity.mediaPlayer;
import static com.dev.jtunao.demoapp_10.MainActivity.settings;
import static com.dev.jtunao.demoapp_10.MainActivity.sound;
import static com.dev.jtunao.demoapp_10.MainActivity.total_score;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.sql.SQLOutput;

public class SettingsActivity extends AppCompatActivity {

    Integer[] item = {24,12};
    String[] sound_name = {"Neon gaming", "Novembers", "Stay Retro", "Strangers","Christmas","Main theme"};//in next versions
    Spinner spinner;
    ArrayAdapter<Integer> adapterItems;
    ArrayAdapter<String> adapterItemsSound;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_settings);
        init();

        RelativeLayout layout = findViewById(R.id.main_layout_setting);
        layout.setBackgroundResource(main_bg);
    }
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
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

    private void init(){
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
        ImageView img = findViewById(R.id.back_to_menu);
        Button privacy = findViewById(R.id.privacy);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenuSettings(v);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPrivacy(v);
            }
        });


        //switch init
        Switch s = findViewById(R.id.switch_sound);
        s.setChecked(sound);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sound = true;
                    settings.setSound(true);
                        main_snd = new Snd_control(true,1,main_snd_theme,SettingsActivity.this);
                        main_snd.play();
                }else {
                    sound  = false;
                    settings.setSound(false);
                    main_snd.stop();
                }
            }
        });
        adapterItems = new ArrayAdapter<>(this,R.layout.list_item,item);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapterItems);
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int pos = adapter.getPosition(card_count);
        spinner.setSelection(pos);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer item = (Integer) parent.getSelectedItem();
                card_count = item;
                settings.setCard_count(item);
                System.out.println("selected item "+item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // music spinner
        adapterItemsSound = new ArrayAdapter<>(this,R.layout.list_item,sound_name);
        Spinner music_spinner = findViewById(R.id.spinner_music);
        music_spinner.setAdapter(adapterItemsSound);
        String m_name = getMusicName(main_snd_theme);
        ArrayAdapter adapter_music = (ArrayAdapter) music_spinner.getAdapter();
        int pos_music = adapter_music.getPosition(m_name);
        music_spinner.setSelection(pos_music);

        music_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name =(String) parent.getSelectedItem();
                if (name != getMusicName(main_snd_theme)) {
                    Integer set_music = getMusicIndex(name);
                        setMusic(set_music);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void setMusic(Integer music){
        main_snd_theme = music;
        main_snd.stop();
        main_snd = new Snd_control(true,1,main_snd_theme,this);
        saveSetting();
        if (sound){
            main_snd.play();
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
    public void backToMenuSettings(View v){
        saveSetting();
        finish();
    }
    public void toPrivacy(View v){
        Intent intent = new Intent(this,privacy.class);
        startActivity(intent);
        finish();
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

}