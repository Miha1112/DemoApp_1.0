package com.dev.jtunao.demoapp_10;

import static com.dev.jtunao.demoapp_10.MainActivity.active_bg;
import static com.dev.jtunao.demoapp_10.MainActivity.cardArrAct;
import static com.dev.jtunao.demoapp_10.MainActivity.card_count;
import static com.dev.jtunao.demoapp_10.MainActivity.cardsBg;
import static com.dev.jtunao.demoapp_10.MainActivity.main_snd_theme;
import static com.dev.jtunao.demoapp_10.MainActivity.settings;
import static com.dev.jtunao.demoapp_10.MainActivity.sound;
import static com.dev.jtunao.demoapp_10.MainActivity.total_score;
import static com.dev.jtunao.demoapp_10.MainActivity.backArr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StoreBackFragment extends Fragment {
    private int score = total_score;
    private Button[] btnArray = new Button[5];

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public StoreBackFragment() {
        // Required empty public constructor
    }

    public static StoreBackFragment newInstance(String param1, String param2) {
        StoreBackFragment fragment = new StoreBackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_back, container, false);
        btnArray[0] = view.findViewById(R.id.by_first);
        btnArray[1] = view.findViewById(R.id.by_sec);
        btnArray[2] = view.findViewById(R.id.by_third);
        btnArray[3] = view.findViewById(R.id.by_4);
        btnArray[4] = view.findViewById(R.id.by_5);

        btnArray[0].setOnClickListener(clickListener);
        btnArray[1].setOnClickListener(clickListener);
        btnArray[2].setOnClickListener(clickListener);
        btnArray[3].setOnClickListener(clickListener);
        btnArray[4].setOnClickListener(clickListener);

        updBtnText();

        return view;
    }
    private  final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //System.out.println("click on btn");
            // Обробка клікa на кнопку
            Button btn = getView().findViewById(v.getId());
            for (int o=0;o<cardsBg.length;o++){
                if (btnArray[o]==btn){
                    //System.out.println("start chek");
                    if(!cardsBg[o].getIs_bought()){
                        if (total_score > cardsBg[o].getPrice()){
                            //System.out.println("get price");
                            total_score-=cardsBg[o].getPrice();
                            cardsBg[o].setIs_bought(true);
                            for (int p = 0; p<cardsBg.length;p++){
                                cardsBg[p].setIs_active(false);
                            }
                            cardsBg[o].setIs_active(true);
                            active_bg = backArr[o];
                        }else {
                            Toast.makeText(getContext(),"Not enough money",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if (!cardsBg[o].getIs_active()){
                            //System.out.println("get active");
                            for (int p = 0; p<cardsBg.length;p++){
                                cardsBg[p].setIs_active(false);
                            }
                           // System.out.println("previous bg : "+ active_bg);
                            cardsBg[o].setIs_active(true);
                            active_bg = backArr[o];
                           // System.out.println("next bg set: "+active_bg);
                        }
                    }
                    updBtnText();
                    saveData();
                    saveSetting();
                }
            }
        }
    };
    private void saveData(){
        String fileName = "card.json";
        File file = new File(getActivity().getFilesDir(), fileName);
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
        File file = new File(getActivity().getFilesDir(), fileName);
        settings.setCard_count(card_count);
        settings.setMoney(total_score);
        settings.setSound(sound);
        settings.setSound_name(getMusicName(main_snd_theme));
        settings.setActivated_card(cardArrAct);
        GsonBuilder builderSetting = new GsonBuilder();
        Gson gsonUpdateSetting = builderSetting.create();
        String jsonStringSetting = gsonUpdateSetting.toJson(settings);
        try (FileWriter fileWriter = new FileWriter(file)){
            fileWriter.write(jsonStringSetting);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void updBtnText(){
        if (cardsBg != null && cardsBg.length > 0) {
            for (int i = 0; i < cardsBg.length; i++) {
                CardsBg cards = cardsBg[i];
                Button btn = btnArray[i];
                if (cards.getIs_active()) {
                    btn.setText("Active");
                } else if (cards.getIs_bought()) {
                    btn.setText("Activated");
                } else {
                    btn.setText(Integer.toString(cards.getPrice()));
                }
            }
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
