package com.dev.jtunao;

import static com.dev.jtunao.demoapp_10.MainActivity.background;
import static com.dev.jtunao.demoapp_10.MainActivity.backgroundImg;
import static com.dev.jtunao.demoapp_10.MainActivity.total_score;
import static com.dev.jtunao.demoapp_10.MainActivity.main_bg;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dev.jtunao.demoapp_10.BackgroundImg;
import com.dev.jtunao.demoapp_10.CardsBg;
import com.dev.jtunao.demoapp_10.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BackgroundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BackgroundFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Button[] btnArray = new Button[10];

    public BackgroundFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BackgroundFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BackgroundFragment newInstance(String param1, String param2) {
        BackgroundFragment fragment = new BackgroundFragment();
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
        View view = inflater.inflate(R.layout.fragment_background, container, false);
        setBtnArray(view);
        updTextBtn();
        setClickListener(view);
        return view;
    }

    private void setBtnArray(View view){
            btnArray[0]= view.findViewById(R.id.by_1);
            btnArray[1]= view.findViewById(R.id.by_2);
            btnArray[2]= view.findViewById(R.id.by_3);
            btnArray[3]= view.findViewById(R.id.by_4);
            btnArray[4]= view.findViewById(R.id.by_5);
            btnArray[5]= view.findViewById(R.id.by_6);
            btnArray[6]= view.findViewById(R.id.by_7);
            btnArray[7]= view.findViewById(R.id.by_8);
            btnArray[8]= view.findViewById(R.id.by_9);
            btnArray[9]= view.findViewById(R.id.by_10);
    }
    private void setClickListener(View view){
        for (int i = 0; i < btnArray.length;i++)
            btnArray[i].setOnClickListener(clickListener);
    }

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn = getView().findViewById(v.getId());
            for (int o=0;o<backgroundImg.length;o++){
                if (btnArray[o]==btn){
                    //System.out.println("start chek");
                    if(!backgroundImg[o].getIs_bought()){
                        if (total_score > backgroundImg[o].getPrice()){
                            //System.out.println("get price");
                            total_score-=backgroundImg[o].getPrice();
                            backgroundImg[o].setIs_bought(true);
                            for (int p = 0; p<backgroundImg.length;p++){
                                backgroundImg[p].setIs_active(false);
                            }
                            backgroundImg[o].setIs_active(true);
                            main_bg = background[o];
                        }else {
                            Toast.makeText(getContext(),"Not enough money",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if (!backgroundImg[o].getIs_active()){
                            //System.out.println("get active");
                            for (int p = 0; p<backgroundImg.length;p++){
                                backgroundImg[p].setIs_active(false);
                            }
                            // System.out.println("previous bg : "+ active_bg);
                            backgroundImg[o].setIs_active(true);
                            main_bg = background[o];
                            // System.out.println("next bg set: "+active_bg);
                        }
                    }
                    updTextBtn();
                    saveData();
                }
            }
        }
    };

    private void updTextBtn(){
        if (backgroundImg != null && backgroundImg.length > 0) {
            for (int i = 0; i < backgroundImg.length; i++) {
                BackgroundImg backgroundImg1 = backgroundImg[i];
                Button btn = btnArray[i];
                if (backgroundImg1.getIs_active()) {
                    btn.setText("Active");
                } else if (backgroundImg1.getIs_bought()) {
                    btn.setText("Activated");
                } else {
                    btn.setText(Integer.toString(backgroundImg1.getPrice()));
                }
            }
        }
    }
    private void saveData(){
        String fileName = "background.json";
        File file = new File(getActivity().getFilesDir(), fileName);
        // System.out.println("try save data");
        GsonBuilder builder = new GsonBuilder();
        Gson gsonUpdate = builder.create();
        String jsonString = gsonUpdate.toJson(backgroundImg);
        try (FileWriter fileWriter = new FileWriter(file)){
            fileWriter.write(jsonString);
            // System.out.println("data saved successful");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}