package com.example.metronomeapp;

import java.util.concurrent.TimeUnit;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.metronomeapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        super(R.layout.fragment_dashboard);
    }

    int tempo;

    EditText TempoInput;

    Button startBtn, stopBtn;

    boolean[] isStopButtonPressed = {false};

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        TempoInput = findViewById(R.id.TempoInput);

        startBtn = findViewById(R.id.startButton);
        stopBtn = findViewById(R.id.stopButton);

        final MediaPlayer playTick = MediaPlayer.create(this, R.raw.tick);

        stopBtn.setOnClickListener(v -> isStopButtonPressed[0] = true);

        startBtn.setOnClickListener(v -> {
            tempo = Integer.valueOf(TempoInput.getText().toString());
            showToast(String.valueOf(tempo));

            while (true) {
                if (isStopButtonPressed[0]) {
                    isStopButtonPressed[0] = false;
                    break;
                } else {
                    playTick.start();
                    try {
                        TimeUnit.MICROSECONDS.sleep(calcInterval(tempo));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    playTick.reset();
                }
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
    }

    //Debug function to show text when necessary
    public void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    //calculates interval (in microseconds) at which to play the metronome tick based on tempo in BPM units.
    public long calcInterval(@NonNull int tempo) {
        if (tempo <= 0) {
            return -1;
        } else {
            double rawInterval = 60 / tempo;
            long translatedInterval = Double.valueOf(1000000 * rawInterval).longValue();
            return translatedInterval;
        }
    }
}