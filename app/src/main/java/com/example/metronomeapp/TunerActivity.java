package com.example.metronomeapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TunerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        Button AButton, DButton, GButton, EButton, MainButton;

        AButton = findViewById(R.id.AButton);
        DButton = findViewById(R.id.DButton);
        GButton = findViewById(R.id.GButton);
        EButton = findViewById(R.id.EButton);
        MainButton = findViewById(R.id.MainButton);

        final MediaPlayer tunerA = MediaPlayer.create(this, R.raw.a);
        final MediaPlayer tunerD = MediaPlayer.create(this, R.raw.d);
        final MediaPlayer tunerG = MediaPlayer.create(this, R.raw.g);
        final MediaPlayer tunerE = MediaPlayer.create(this, R.raw.e);


        AButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TunerThread(tunerA).start();
            }
        });

        DButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TunerThread(tunerD).start();
            }
        });

        GButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TunerThread(tunerG).start();
            }
        });

        EButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TunerThread(tunerE).start();
            }
        });



        MainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TunerActivity.this, MainActivity.class));
            }
        });


    }

}
