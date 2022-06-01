package com.example.metronomeapp;

import android.media.MediaPlayer;
import android.os.SystemClock;

import java.io.IOException;

public class TunerThread extends Thread {
    MediaPlayer string;

    TunerThread(MediaPlayer string) {this.string = string;}

    @Override
    public void run() {
        while(!isInterrupted()) {
            string.start();
            this.interrupt();
        }
    }
}
