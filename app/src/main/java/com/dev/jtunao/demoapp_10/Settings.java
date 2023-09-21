package com.dev.jtunao.demoapp_10;

public class Settings {
    private Boolean sound;
    private int money;
    private int card_count;

    public Boolean getSound() {
        return sound;
    }

    public void setSound(Boolean sound) {
        this.sound = sound;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getCard_count() {
        return card_count;
    }

    public void setCard_count(int card_count) {
        this.card_count = card_count;
    }

    public Settings(Boolean sound, int money, int card_count) {
        this.sound = sound;
        this.money = money;
        this.card_count = card_count;
    }
}
