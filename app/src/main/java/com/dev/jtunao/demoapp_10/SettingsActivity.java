package com.dev.jtunao.demoapp_10;

import static com.dev.jtunao.demoapp_10.MainActivity.card_count;
import static com.dev.jtunao.demoapp_10.MainActivity.mediaPlayer;
import static com.dev.jtunao.demoapp_10.MainActivity.settings;
import static com.dev.jtunao.demoapp_10.MainActivity.sound;
import static com.dev.jtunao.demoapp_10.MainActivity.total_score;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;

public class SettingsActivity extends AppCompatActivity {

    Integer[] item = {24,12};
    Spinner spinner;
    ArrayAdapter<Integer> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);
        init();
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

    private void init(){
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
                    mediaPlayer.start();
                }else {
                    sound  = false;
                    settings.setSound(false);
                    mediaPlayer.pause();
                }
            }
        });





        adapterItems = new ArrayAdapter<>(this,R.layout.list_item,item);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapterItems);
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
                switch (settings.getCard_count()){
                    case 24:
                        System.out.println("selected 24" + settings.getCard_count());
                        parent.setSelection(0);
                    break;
                    case 12:
                        System.out.println("selected 12 "+ settings.getCard_count());
                        parent.setSelection(1);
                    break;
                }
            }
        });

    }
    public void backToMenuSettings(View v){
        saveSetting();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void toPrivacy(View v){
        Intent intent = new Intent(this,privacy.class);
        startActivity(intent);
    }

}