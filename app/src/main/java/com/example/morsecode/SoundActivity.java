package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.widget.Button;

public class SoundActivity extends AppCompatActivity {
    private final ToneGenerator toneGenerator =  new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        Button shortButton = findViewById(R.id.shortButton);
        Button longButton = findViewById(R.id.longButton);
        Button letterButton = findViewById(R.id.letterButton);

        shortButton.setOnClickListener(view -> onBtnShort());
        longButton.setOnClickListener(view -> onBtnLong());

        letterButton.setOnClickListener(view -> onBtnLetter());
    }

    private void playSound(int len){
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_2,len);
    }

    private void playLongTone(){ playSound(500); }
    private void playShortTone(){ playSound(100); }

    private void onBtnShort(){
        playShortTone();
    }
    private void onBtnLong(){
        playLongTone();
    }

    private void onBtnLetter(){
        new Thread(() -> {
            synchronized (toneGenerator){
                playSound(100);
                try {
                    toneGenerator.wait(100+500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                playSound(500);
            }
        }).start();
    }
}