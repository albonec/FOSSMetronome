package com.example.metronomeapp;

import android.media.MediaPlayer;
import android.os.SystemClock;
import android.widget.EditText;
import androidx.annotation.NonNull;

public class TickThread extends Thread {
    MediaPlayer playTick;
    MediaPlayer playA4;
    EditText TempoInput;

    public TickThread(MediaPlayer playTick, MediaPlayer playA4, EditText TempoInput) {
        this.playA4 = playA4;
        this.playTick = playTick;
        this.TempoInput = TempoInput;
    }

    @Override
    public void run() {
        long tempo = Integer.valueOf(TempoInput.getText().toString());
        System.out.println("Thread started");
        while(!isInterrupted()) {
            if(tempo == 440) {
                playA4.start();
                SystemClock.sleep(35000);
                this.interrupt();
            } else {
                playTick.start();
                System.out.println((long) calcInterval(tempo));
                SystemClock.sleep((long) calcInterval(tempo));
            }
        }
    }

    public float calcInterval(@NonNull long tempo) {
        if (((60 / tempo) * 1000 - 10) <= 0) {
            return 0;
        }
        if (tempo >= 100) {
                return ((60 / tempo) * 1000) - 10;
            } else {
            return (60 / tempo) * 1000 - tempo/25;
        }
    }

}
