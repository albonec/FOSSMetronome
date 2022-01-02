package com.example.metronomeapp;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.metronomeapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    EditText TempoInput;
    long tempo;

    public MainActivity() {
        super(R.layout.activity_main);
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final MediaPlayer playTick = MediaPlayer.create(this, R.raw.tick);
        final MediaPlayer playA4 = MediaPlayer.create(this, R.raw.a);

        Button startBtn, stopBtn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        TempoInput = findViewById(R.id.TempoInput);

        startBtn = findViewById(R.id.startButton);
        stopBtn = findViewById(R.id.stopButton);

        TickThread thread = new TickThread(tempo, playTick, playA4);


        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.start();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.interrupt();
            }
        });

    }

    //Debug function to show text when necessary
    public void showToast(String text) {
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }
}

