package com.example.metronomeapp;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.metronomeapp.databinding.ActivityMainBinding;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    int tempo;

    EditText TempoInput;

    Button startStopBtn;

    boolean doRun, isClicked = true;

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

        startStopBtn = findViewById(R.id.startStopButton);

        final MediaPlayer playTick = MediaPlayer.create(this, R.raw.tick);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    while(true) {
                        tempo = Integer.valueOf(TempoInput.getText().toString());
                        playTick.start();
                        SystemClock.sleep(calcInterval(tempo));
                    }
            }
        });
        }

    //Debug function to show text when necessary
    public void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    //calculates interval (in milliseconds) at which to play the metronome tick based on tempo in BPM units.
    public long calcInterval(@NonNull int tempo) {
        if (tempo <= 0) {
            return 0;
        } else {
            double rawInterval = 60 / tempo;
            long translatedInterval = Double.valueOf((1000 * rawInterval)).longValue();
            if (translatedInterval < 1) {
                return 1;
            } else {
                return translatedInterval;
            }
        }
    }
}
