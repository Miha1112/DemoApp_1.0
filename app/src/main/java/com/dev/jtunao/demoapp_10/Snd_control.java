package com.dev.jtunao.demoapp_10;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

public class Snd_control {
    private boolean lopping = false;
    private float volume = 1;
    private Integer fileName = R.raw.def_snd;
    private MediaPlayer mediaPlayer;

    public void setLopping(boolean lopping) {
        this.lopping = lopping;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isLopping() {
        return lopping;
    }

    public float getVolume() {
        return volume;
    }

    public Snd_control(boolean lopping, float volume, Integer fileName, Context context) {
        this.lopping = lopping;
        this.volume = volume;
        this.fileName = fileName;
        mediaPlayer = MediaPlayer.create(context,fileName);
        mediaPlayer.setLooping(lopping);
        mediaPlayer.setVolume(volume,volume);
    }

    public void play() {
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }
    public void pause(){
        mediaPlayer.pause();
    }
    public void stop(){
        mediaPlayer.stop();
    }
    public boolean isPlay(){
        return mediaPlayer.isPlaying();
    }
}
