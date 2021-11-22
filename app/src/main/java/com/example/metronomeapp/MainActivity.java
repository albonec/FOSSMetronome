package com.example.metronomeapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.metronomeapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    int tempo;

    EditText TempoInput;

    Button startBtn;
    Button stopBtn;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TempoInput = (EditText) findViewById(R.id.TempoInput);

        startBtn = (Button) findViewById(R.id.startButton);
        stopBtn = (Button) findViewById(R.id.stopButton);

        //final MediaPlayer mediaPlayer = MediaPlayer.create(this);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempo = Integer.valueOf(TempoInput.getText().toString());
                showToast(String.valueOf(tempo));
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add stop code here
            }
        });

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    //Debug function to show text when necessary
    public void showToast(String text) { Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show(); }

    //calculates interval at which to play the metronome tick based on tempo in BPM units.
    public double calcInterval(int tempo) {
        double interval = 60 / tempo;
        return interval;
    }
}