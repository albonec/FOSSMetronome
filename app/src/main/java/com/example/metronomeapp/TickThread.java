package com.example.metronomeapp;

import android.media.MediaPlayer;
import android.os.SystemClock;
import android.widget.EditText;
import androidx.annotation.NonNull;

public class TickThread extends Thread {
    MediaPlayer playTick;
    MediaPlayer playA4;
    MainActivity mainActivity = new MainActivity();
    long tempo;

    public TickThread(long tempo) {
        this.tempo = tempo;
    }

    @Override
    public void run() {
        //mainActivity.setup();
        while(!isInterrupted()) {
            if(tempo == 440) {
                //playA4.start();
            } else {
//                playTick.start();
                System.out.println("Thread started");
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
