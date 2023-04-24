package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class SoundActivity extends AppCompatActivity {
    private ToneGenerator toneGenerator;
    private Button beepButton;
    private Button letterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        beepButton = findViewById(R.id.beepButton);
        letterButton = findViewById(R.id.letterButton);

        beepButton.setOnClickListener(view -> onBtnBeep());
        letterButton.setOnClickListener(view -> onBtnLetter());

        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    }

    private void playSound(int len){
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_2,len);
    }

    private void onBtnBeep(){
        playSound(100);
    }

    private void onBtnLetter(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (toneGenerator){
                    playSound(100);
                    try {
                        toneGenerator.wait(100+500);
                        Log.d("torsdag", "run: made it");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    playSound(500);
                }
            }
        }).start();
    }
}