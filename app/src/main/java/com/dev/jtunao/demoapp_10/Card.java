package com.dev.jtunao.demoapp_10;

import android.view.View;
import android.widget.ImageView;

public class Card {
    private int ind = 0;
    private Integer back_img;
    private Integer main_img;
    private ImageView myImg;

    public ImageView getMyImg() {
        return myImg;
    }

    public void setMyImg(ImageView myImg) {
        this.myImg = myImg;
    }

    public void setInd(int ind) {
        this.ind = ind;
    }

    public void setBack_img(Integer back_img) {
        this.back_img = back_img;
    }

    public void setMain_img(Integer main_img) {
        this.main_img = main_img;
    }

    public int getInd() {
        return ind;
    }

    public Integer getBack_img() {
        return back_img;
    }

    public Integer getMain_img() {
        return main_img;
    }

    public Card(int ind, Integer back_img, Integer main_img) {
        this.ind = ind;
        this.back_img = back_img;
        this.main_img = main_img;
    }

}
