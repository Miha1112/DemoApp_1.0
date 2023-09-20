package com.dev.jtunao.demoapp_10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dev.jtunao.demoapp_10.R;

public class MainActivity extends AppCompatActivity {
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

    private void init(){
        TextView textView = findViewById(R.id.moneyScore);
        textView.setText(Integer.toString(total_score));
    }

    public void goToMainGameScreen(View view){
        Intent intent = new Intent(this, playActivity.class);
        startActivity(intent);
    }
}