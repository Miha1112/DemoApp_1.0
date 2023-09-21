package com.dev.jtunao.demoapp_10;

import static com.dev.jtunao.demoapp_10.MainActivity.settings;
import static com.dev.jtunao.demoapp_10.MainActivity.sound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Switch;

import java.sql.SQLOutput;

public class SettingsActivity extends AppCompatActivity {

    Integer[] item = {24,12};
    Spinner spinner;
    ArrayAdapter<Integer> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }
    public void backToMenuSettings(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void init(){
        //switch init
        Switch s = findViewById(R.id.switch_sound);
        s.setChecked(sound);


        adapterItems = new ArrayAdapter<>(this,R.layout.list_item,item);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapterItems);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer item = (Integer) parent.getSelectedItem();
                settings.setCard_count(item);
                System.out.println("selected item "+item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
            }
        });
    }

}