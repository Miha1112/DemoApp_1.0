package com.dev.jtunao.demoapp_10;

import static com.dev.jtunao.demoapp_10.MainActivity.active_bg;
import static com.dev.jtunao.demoapp_10.MainActivity.total_score;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dev.jtunao.demoapp_10.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class playActivity extends AppCompatActivity {
    private Random position;
    private int index_click,index_chek;
    private int clicked_index,cheked_index = -1;
    private int remove_cnt = 0;
    private Boolean isAnyCardClicked,isClickAll = false ;

    private Card[] cardsPlay = new Card[36];


    private Integer[] cardsArr = {R.drawable.card_clubs_1,R.drawable.card_clubs_2,R.drawable.card_clubs_3
            ,R.drawable.card_clubs_4,R.drawable.card_clubs_5,R.drawable.card_clubs_6
            ,R.drawable.card_clubs_7,R.drawable.card_clubs_8,R.drawable.card_clubs_9
            ,R.drawable.card_clubs_10,R.drawable.card_clubs_11,R.drawable.card_clubs_12
            ,R.drawable.card_clubs_13,R.drawable.card_diamonds_1,R.drawable.card_diamonds_2
            ,R.drawable.card_diamonds_3,R.drawable.card_diamonds_4,R.drawable.card_diamonds_5
            ,R.drawable.card_diamonds_6,R.drawable.card_diamonds_7,R.drawable.card_diamonds_8
            ,R.drawable.card_diamonds_9,R.drawable.card_diamonds_10,R.drawable.card_diamonds_11
            ,R.drawable.card_diamonds_12,R.drawable.card_diamonds_13
            ,R.drawable.card_hearts_1,R.drawable.card_hearts_2,R.drawable.card_hearts_3,R.drawable.card_hearts_4
            ,R.drawable.card_hearts_5,R.drawable.card_hearts_6,R.drawable.card_hearts_7,R.drawable.card_hearts_8
            ,R.drawable.card_hearts_9,R.drawable.card_hearts_10,R.drawable.card_hearts_11,R.drawable.card_hearts_12
            ,R.drawable.card_hearts_13,R.drawable.card_spades_1,R.drawable.card_spades_2
            ,R.drawable.card_spades_3,R.drawable.card_spades_4,R.drawable.card_spades_5,R.drawable.card_spades_6
            ,R.drawable.card_spades_7,R.drawable.card_spades_8,R.drawable.card_spades_9,R.drawable.card_spades_10
            ,R.drawable.card_spades_11,R.drawable.card_spades_12,R.drawable.card_spades_13};

    private Integer[] imageViewsArr = {R.id.cards_1,R.id.cards_2,R.id.cards_3,R.id.cards_4,R.id.cards_5,R.id.cards_6,R.id.cards_7
            ,R.id.cards_8,R.id.cards_9,R.id.cards_10,R.id.cards_11,R.id.cards_12,R.id.cards_13,R.id.cards_14,R.id.cards_15,R.id.cards_16
            ,R.id.cards_17,R.id.cards_18,R.id.cards_19,R.id.cards_20,R.id.cards_21,R.id.cards_22,R.id.cards_23,R.id.cards_24};
    private  int[] indexArr = new int[52];
    private  int[] indexPlay = new int[36];
    private Integer[] backArr = {R.drawable.card_back1,R.drawable.card_back2,R.drawable.card_back3,R.drawable.card_back4};
    private Integer[][] cards = new Integer[2][36];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        init();
    }

    private void init(){
        TextView textView = findViewById(R.id.moneyScore);
        textView.setText(Integer.toString(total_score));
        System.out.println(cardsArr.length);
        for(int l = 0;l<52;l++){
            indexArr[l]=l;
        }
        randomaizer(cardsArr,indexArr);

        for (int j =0; j<36;j++){
            cardsPlay[j] = new Card(cards[1][j],active_bg,cards[0][j]);
            cardsPlay[j].setMyImg(findViewById(imageViewsArr[j]));
        }

    }
    private void randomaizer(Integer[] integers, int[] ints){
        Integer[] newCardsArr = new Integer[12];
        int[] newInt = new int[12];
        position = new Random();
        for (int i = 0;i < 12;i++){
               newCardsArr[i] = integers[ThreadLocalRandom.current().nextInt(0,52)];
        }
        for (int p = 0; p<12;p++){
            newInt[p] = ints[ThreadLocalRandom.current().nextInt(0,52)];

        }
        for (int o = 0; o<24;o++){
            cards[0][o] = newCardsArr[position.nextInt(18)];
            cards[1][o] = newInt[position.nextInt(18)];
        }
    };
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

    public void clickOnCrad(View view){
        if(!isClickAll) {
            if (!isAnyCardClicked) {
                isAnyCardClicked = true;
                for (int y = 0; y < cardsPlay.length; y++) {
                    if (cardsPlay[y].getMyImg().getId() == view.getId()) {
                        clicked_index = cardsPlay[y].getInd();
                        ImageView imageView = findViewById(cardsPlay[y].getMyImg().getId());
                        imageView.setImageResource(cardsPlay[y].getMain_img());
                        index_click = y;
                    }
                }
            } else {
                isClickAll = true;
                for (int y = 0; y < cardsPlay.length; y++) {
                    if (cardsPlay[y].getMyImg().getId() == view.getId()) {
                        cheked_index = cardsPlay[y].getInd();
                        ImageView imageView = findViewById(cardsPlay[y].getMyImg().getId());
                        imageView.setImageResource(cardsPlay[y].getMain_img());
                        index_chek = y;
                    }
                }
                if (cardsPlay[index_chek].getMain_img() == cardsPlay[index_click].getMain_img()) {
                    System.out.println("init timer remove");
                    initTimer_remove();
                } else {
                    System.out.println("init timer wrong");
                    initTimer_wrong();
                }
            }
        }else {
            //here must be a code if clicked more than 2 card in the same time, but i`m too lazy)))
        }
    }

    private void initTimer_wrong(){
        CountDownTimer timer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                wrongChoose();
            }
        };
        timer.start();

    }
    private void initTimer_remove(){
        CountDownTimer timer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                removeCard();
            }
        };
        timer.start();

    }
    private void removeCard(){
        RelativeLayout main_layout =  findViewById(R.id.main_play_block);
        total_score+=30;
        TextView textView =  findViewById(R.id.moneyScore);
        textView.setText(Integer.toString(total_score));
        remove_cnt++;
        if (remove_cnt >= 17){

        }
        isAnyCardClicked = false;
        clicked_index = -1;
        cheked_index = -1;
        index_click = -1;
        index_chek = -1;
    }
    private void wrongChoose(){
        ImageView imageView1 = findViewById(cardsPlay[index_chek].getMyImg().getId());
        imageView1.setImageResource(cardsPlay[index_chek].getBack_img());
        ImageView imageView2 = findViewById(cardsPlay[index_click].getMyImg().getId());
        imageView2.setImageResource(cardsPlay[index_click].getBack_img());
        isAnyCardClicked = false;
        clicked_index = -1;
        cheked_index = -1;
        index_click = -1;
        index_chek = -1;
    }
}