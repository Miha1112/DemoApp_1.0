package com.dev.jtunao.demoapp_10;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.jtunao.demoapp_10.R;
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

public class MainActivity extends AppCompatActivity {
    static CardsBg[] cardsBg;
    static int total_score = 50;
    static int active_bg = R.drawable.card_back1;
    static Integer[] backArr = {R.drawable.card_back1,R.drawable.card_back2,R.drawable.card_back3,R.drawable.card_back4};


    static int card_count = 24;
    static Boolean sound = true;
    static MediaPlayer mediaPlayer;

    static Settings settings;


    private Boolean LOAD_SETTING_STATUS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    private void init() {
        TextView textView = findViewById(R.id.moneyScore);
        textView.setText(Integer.toString(total_score));
        mediaPlayer = MediaPlayer.create(this,R.raw.def_snd);
        jsonParse();
        getSettings();
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
                sound = settings.getSound();
                if (sound){
                    mediaPlayer.start();
                }else {
                    mediaPlayer.stop();
                }
                total_score = settings.getMoney();
                card_count = settings.getCard_count();
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
                sound = settings.getSound();
                if (sound){
                    mediaPlayer.start();
                }else {
                    mediaPlayer.stop();
                }
                total_score = settings.getMoney();
                card_count = settings.getCard_count();
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
    }
    private void saveSetting(){
        String fileName = "setting.json";
        File file = new File(getFilesDir(), fileName);
        settings.setCard_count(card_count);
        settings.setMoney(total_score);
        settings.setSound(sound);
        GsonBuilder builderSetting = new GsonBuilder();
        Gson gsonUpdateSetting = builderSetting.create();
        String jsonStringSetting = gsonUpdateSetting.toJson(settings);
        try (FileWriter fileWriter = new FileWriter(file)){
            fileWriter.write(jsonStringSetting);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}