package com.dev.jtunao.demoapp_10;

import android.widget.Button;

public class CardsBg {
    private  String name;
    private int price;
    private Boolean is_bought;
    private  Boolean is_active;

    public CardsBg(String name, int price, Boolean is_bought, Boolean is_active) {
        this.name = name;
        this.price = price;
        this.is_bought = is_bought;
        this.is_active = is_active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Boolean getIs_bought() {
        return is_bought;
    }

    public void setIs_bought(Boolean is_bought) {
        this.is_bought = is_bought;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }
}
