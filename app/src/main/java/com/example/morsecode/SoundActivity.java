package com.example.morsecode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.widget.Button;

public class SoundActivity extends AppCompatActivity {
    private final ToneGenerator toneGenerator =  new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    private final MorseCode morseCode = new MorseCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        Button shortButton = findViewById(R.id.shortButton);
        Button longButton = findViewById(R.id.longButton);
        Button letterButton = findViewById(R.id.letterButton);
        Button shortButton2 = findViewById(R.id.shortButton2);
        Button longButton2 = findViewById(R.id.longButton2);
        Button shortButton3 = findViewById(R.id.shortButton3);
        Button longButton3 = findViewById(R.id.longButton3);

        shortButton.setOnClickListener(view -> onBtnShort());
        longButton.setOnClickListener(view -> onBtnLong());
        shortButton2.setOnClickListener(view -> onBtnShort2());
        longButton2.setOnClickListener(view -> onBtnLong2());
        shortButton3.setOnClickListener(view -> onBtnShort3());
        longButton3.setOnClickListener(view -> onBtnLong3());
        letterButton.setOnClickListener(view -> onBtnLetter());
    }

    private void playSound(int len){
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_2,len);
    }
    private void playSound1(int len){
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_1,len);
    }
    private void playSound2(int len){
        toneGenerator.startTone(ToneGenerator.TONE_DTMF_4,len);
    }
    private void playSound3(int len){
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT,len);
    }
    private void playSound4(int len){
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ANSWER,len);
    }

    private void playLongTone(){ playSound(500); }
    private void playShortTone(){ playSound(100); }

    private void onBtnShort(){
        playShortTone();
    }
    private void onBtnLong(){
        playLongTone();
    }

    private void onBtnShort2(){
        playSound1(200);
    }
    private void onBtnLong2(){
        playSound2(200);
    }

    private void onBtnShort3(){
        playSound3(200);
    }
    private void onBtnLong3(){
        playSound4(200);
    }

    private void playLetter( Character c){
        String morse = morseCode.getMorse(c);

        new Thread(() -> {
            synchronized (toneGenerator){
                try {
                    for(int i = 0; i < morse.length(); i++){
                        if(morse.charAt(i) == '.'){
                            playShortTone();
                        }else{
                            playLongTone();
                        }
                        toneGenerator.wait(100+500);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void onBtnLetter(){
        playLetter('h');
    }
}