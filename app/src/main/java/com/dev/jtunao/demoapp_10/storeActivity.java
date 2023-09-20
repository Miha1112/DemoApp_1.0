package com.dev.jtunao.demoapp_10;

import static com.dev.jtunao.demoapp_10.MainActivity.total_score;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class storeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
    }
    public void backToMenu(){
        saveMoney();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveMoney(){
        try {
            File file = new File(this.getExternalFilesDir(null), "results.txt");

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("FileSave" + " File created");
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write("money_score" +  "," + total_score);
            writer.newLine();
            writer.close();
            System.out.println("FileSave" + " File created");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FileSave" + " error");
        }

    }
}