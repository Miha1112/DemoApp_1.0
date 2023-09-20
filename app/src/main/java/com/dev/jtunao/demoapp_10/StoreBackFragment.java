package com.dev.jtunao.demoapp_10;

import static com.dev.jtunao.demoapp_10.MainActivity.cardsBg;
import static com.dev.jtunao.demoapp_10.MainActivity.total_score;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StoreBackFragment extends Fragment {
    private int score = total_score;
    private Button[] btnArray = new Button[4];

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_back, container, false);

        // Перенесення коду ініціалізації кнопок і текстів сюди
        btnArray[0] = view.findViewById(R.id.by_first);
        btnArray[1] = view.findViewById(R.id.by_sec);
        btnArray[2] = view.findViewById(R.id.by_third);
        btnArray[3] = view.findViewById(R.id.by_4);

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

        return view;
    }

    public void clickButtonBy(View view) {
        // Обробка кліку на кнопку
    }
}
