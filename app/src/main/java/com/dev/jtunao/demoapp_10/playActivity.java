package com.dev.jtunao.demoapp_10;

import static com.dev.jtunao.demoapp_10.MainActivity.active_bg;
import static com.dev.jtunao.demoapp_10.MainActivity.card_count;
import static com.dev.jtunao.demoapp_10.MainActivity.cardsBg;
import static com.dev.jtunao.demoapp_10.MainActivity.main_snd;
import static com.dev.jtunao.demoapp_10.MainActivity.main_snd_theme;
import static com.dev.jtunao.demoapp_10.MainActivity.settings;
import static com.dev.jtunao.demoapp_10.MainActivity.sound;
import static com.dev.jtunao.demoapp_10.MainActivity.total_score;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class playActivity extends AppCompatActivity {
    private Random position;
    private int index_click,index_chek;
    private AdView mAdView;
    private int clicked_index,cheked_index = -1;
    private int rem_ind  = 3;//removed card index for auto update cards
    private int remove_cnt = 0;
    private int remove_card_cnt = 0;
    private Boolean isAnyCardClicked = false ;
    private Boolean canClickFix = true;

    private Card[] cardsPlay = new Card[card_count];


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
    private Integer[][] cards = new Integer[2][36];
    private ImageView btn_regen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_play);
        init();
        setBgCard();
        getActiveCards();
        btn_regen = findViewById(R.id.refresh_btn);
        btn_regen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_cnt = 0;
                init();
                setBgCard();
            }
        });

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
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenuPlay();
            }
        });

        ImageView imgStore = findViewById(R.id.store_btn);
        imgStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStore();
            }
        });
        TextView textView = findViewById(R.id.moneyScore);
        textView.setText(Integer.toString(total_score));
        System.out.println(cardsArr.length);
        for(int l = 0;l<52;l++){
            indexArr[l]=l;
        }
        randomaizer(cardsArr,indexArr);
        System.out.println("active bg set: "+active_bg);

        for (int j =0; j<card_count;j++){
            cardsPlay[j] = new Card(cards[1][j],active_bg,cards[0][j]);
            cardsPlay[j].setMyImg(findViewById(imageViewsArr[j]));
        }

    }
    private void randomaizer(Integer[] integers, int[] ints){
        Integer[] newCardsArr = new Integer[card_count/2];
        int[] newInt = new int[card_count/2];
        position = new Random();
        for (int i = 0;i < card_count/2;i++){
               newCardsArr[i] = integers[ThreadLocalRandom.current().nextInt(0,52)];
        }
        for (int p = 0; p<card_count/2;p++){
            newInt[p] = ints[ThreadLocalRandom.current().nextInt(0,52)];

        }
        for (int o = 0; o<card_count;o++){
            cards[0][o] = newCardsArr[position.nextInt(card_count/2)];
            cards[1][o] = newInt[position.nextInt(card_count/2)];
        }
    };
    public void backToMenuPlay(){
        finish();
    }
    public void goToStore(){
        saveSetting();
        Intent intent = new Intent(this, storeActivity.class);
        startActivity(intent);
    }

    public void clickOnCrad(View view){
        if (canClickFix){
            if (!isAnyCardClicked) {
                isAnyCardClicked = true;
                for (int y = 0; y < cardsPlay.length; y++) {
                    if (cardsPlay[y].getMyImg().getId() == view.getId()) {
                        clicked_index = cardsPlay[y].getInd();
                        ImageView imageView = findViewById(cardsPlay[y].getMyImg().getId());
                        if (imageView.getDrawable() != null) {
                            imageView.setImageResource(cardsPlay[y].getMain_img());
                            index_click = y;
                        }
                    }
                }
            } else {
                canClickFix = false;
                for (int y = 0; y < cardsPlay.length; y++) {
                    if (cardsPlay[y].getMyImg().getId() == view.getId()) {
                        cheked_index = cardsPlay[y].getInd();
                        ImageView imageView = findViewById(cardsPlay[y].getMyImg().getId());
                        if (imageView.getDrawable() != null) {
                            imageView.setImageResource(cardsPlay[y].getMain_img());
                            index_chek = y;
                        }
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
            //just bugfix things))))))))
        }
    }

    private void initTimer_wrong(){
        CountDownTimer timer = new CountDownTimer(1000,500) {
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
        CountDownTimer timer = new CountDownTimer(1000,500) {
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
        main_layout.removeView(findViewById(cardsPlay[index_click].getMyImg().getId()));
        main_layout.removeView(findViewById(cardsPlay[index_chek].getMyImg().getId()));
        remove_cnt++;
        if (remove_cnt >= card_count/2-(card_count/2)/4){//for infinity game, in next versions maybe add on/off to this fiches
            init();
            setBgCard();
            Toast.makeText(this,"Auto cards updated", Toast.LENGTH_SHORT).show();
        }
        isAnyCardClicked = false;
        canClickFix = true;
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
        canClickFix = true;
        clicked_index = -1;
        cheked_index = -1;
        index_click = -1;
        index_chek = -1;
    }
    private void getActiveCards(){
        System.out.println("try delete element");
        remove_card_cnt = (imageViewsArr.length - 1) - (card_count - 1);
        Integer[] layoutArr = {R.id.center_card_second,R.id.center_card};
        int  k = 0;
        for (int i = 0; i <remove_card_cnt;i++){
            int o = imageViewsArr.length-(i+1);
            if ( i != 0 && i % 8 == 0){
                k++;
            }
            RelativeLayout layout = findViewById(layoutArr[k]);
            System.out.println("delete count successful : " + i + " position in array: " + o);
            ImageView img = findViewById(imageViewsArr[o]);
            layout.removeView(img);
        }
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

    private void setBgCard(){
        for (int i = 0; i < card_count;i++ ){
            ImageView imageView = findViewById(cardsPlay[i].getMyImg().getId());
            imageView.setImageResource(cardsPlay[i].getBack_img());
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
}