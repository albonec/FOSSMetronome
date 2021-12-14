package com.example.metronomeapp;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.metronomeapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    int tempo;

    EditText TempoInput;

    Button startBtn, stopBtn;

    Thread thread;

    boolean doRun, isClicked = true;

    final MediaPlayer playTick = MediaPlayer.create(this, R.raw.tick);

    public MainActivity() {
        super(R.layout.fragment_dashboard);
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        TempoInput = findViewById(R.id.TempoInput);

        startBtn = findViewById(R.id.startButton);
        stopBtn = findViewById(R.id.stopButton);

        thread = null;
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();

        startBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            tempo = Integer.valueOf(TempoInput.getText().toString());
                            try {
                                Thread.sleep(tempo);
                                playTick.start();
                                Log.d("time", "run: ");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                thread.start();
            }
        });

        stopBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thread != null) {
                    try {
                        thread.interrupt();
                    } catch (Exception e) {

                    }
                }
            }
        });

    }

    //Debug function to show text when necessary
    public void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    //calculates interval (in milliseconds) at which to play the metronome tick based on tempo in BPM units.
    public float calcInterval(@NonNull float tempo) {
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

