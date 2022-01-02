package com.example.metronomeapp;

import android.media.MediaPlayer;
import android.os.SystemClock;
import android.widget.EditText;
import androidx.annotation.NonNull;

public class TickThread extends Thread {
    MediaPlayer playTick;
    MediaPlayer playA4;
    long tempo;

    public TickThread(long tempo, MediaPlayer playTick, MediaPlayer playA4) {
        this.tempo = tempo;
        this.playA4 = playA4;
        this.playTick = playTick;
    }

    @Override
    public void run() {
        System.out.println("Thread started");
        //mainActivity.setup();
        while(!isInterrupted()) {
            if(tempo == 440) {
                playA4.start();
            } else {
                playTick.start();
                SystemClock.sleep((long) calcInterval(tempo));
            }
        }
    }

    public float calcInterval(@NonNull long tempo) {
        if (tempo >= 100) {
            if (((60 / tempo) * 1000) <= 0) {
                return 0;
            } else {
                return ((60 / tempo) * 1000) - 10;
            }
        } else {
            return (60 / tempo) * 1000 - tempo/25;
        }
    }

}
