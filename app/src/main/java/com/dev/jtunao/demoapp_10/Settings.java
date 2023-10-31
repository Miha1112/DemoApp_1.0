package com.dev.jtunao.demoapp_10;

public class Settings {
    private Boolean sound;
    private int money;
    private int card_count;

    private String sound_name;

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

    public String getSound_name() {
        return sound_name;
    }

    public void setSound_name(String sound_name) {
        this.sound_name = sound_name;
    }

    public Settings(Boolean sound, int money, int card_count, String sound_name) {
        this.sound = sound;
        this.money = money;
        this.card_count = card_count;
        this.sound_name = sound_name;
    }
}
