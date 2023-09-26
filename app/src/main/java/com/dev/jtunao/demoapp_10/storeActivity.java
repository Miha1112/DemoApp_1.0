package com.dev.jtunao.demoapp_10;

import static com.dev.jtunao.demoapp_10.MainActivity.card_count;
import static com.dev.jtunao.demoapp_10.MainActivity.cardsBg;
import static com.dev.jtunao.demoapp_10.MainActivity.settings;
import static com.dev.jtunao.demoapp_10.MainActivity.sound;
import static com.dev.jtunao.demoapp_10.MainActivity.total_score;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class storeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store);
        init();
        ImageView img_menu = findViewById(R.id.back_to_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu(v);
            }
        });
    }
    private void  init(){
        TextView score = findViewById(R.id.moneyScore);
        score.setText(Integer.toString(total_score));
    }
    public void backToMenu(View view){
        saveData();
        saveSetting();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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