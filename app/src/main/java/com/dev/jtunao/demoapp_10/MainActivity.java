package com.dev.jtunao.demoapp_10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dev.jtunao.demoapp_10.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static CardsBg[] cardsBg;
    static int total_score = 50;
    static int active_bg = R.drawable.card_back1;

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

    private void init() {
        TextView textView = findViewById(R.id.moneyScore);
        textView.setText(Integer.toString(total_score));
        jsonParse();
    }

    public void goToMainGameScreen(View view) {
        Intent intent = new Intent(this, playActivity.class);
        startActivity(intent);
    }
    public void goToStore(View view) {
        Intent intent = new Intent(this, storeActivity.class);
        startActivity(intent);
    }

    private void jsonParse() {
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
                    // Отримайте масив об'єктів "cards_bg"
                    JsonArray jsonArray = jsonObject.getAsJsonArray("cards_bg");

                    // Ініціалізуємо Gson
                    Gson gson = new Gson();

                    // Парсимо JSON-масив у масив об'єктів CardBg
                    cardsBg = gson.fromJson(jsonArray, CardsBg[].class);
                    //System.out.println("json parse ready");
                }
            }
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}