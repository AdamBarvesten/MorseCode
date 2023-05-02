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
        Button letterButton2 = findViewById(R.id.letterButton2);

        shortButton.setOnClickListener(view -> onBtnShort());
        longButton.setOnClickListener(view -> onBtnLong());
        letterButton.setOnClickListener(view -> onBtnLetter());

        shortButton2.setOnClickListener(view -> onBtnShort2());
        longButton2.setOnClickListener(view -> onBtnLong2());
        letterButton2.setOnClickListener(view -> onBtnLetter2());
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

    private void playLetter2( Character c){
        String morse = morseCode.getMorse(c);

        new Thread(() -> {
            synchronized (toneGenerator){
                try {
                    for(int i = 0; i < morse.length(); i++){
                        if(morse.charAt(i) == '.'){
                            playShortTone2();
                        }else{
                            playLongTone2();
                        }
                        toneGenerator.wait(100+500);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void playSound(int len){ toneGenerator.startTone(ToneGenerator.TONE_SUP_DIAL,len); }

    private void playSound2(int len){ toneGenerator.startTone(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE,len); }

    private void playLongTone(){ playSound(500); }
    private void playShortTone(){ playSound(100); }

    private void playLongTone2(){ playSound2(500); }
    private void playShortTone2(){ playSound2(100); }

    private void onBtnShort(){
        playShortTone();
    }
    private void onBtnLong(){
        playLongTone();
    }

    private void onBtnShort2() { playShortTone2();}
    private void onBtnLong2() { playLongTone2(); }

    private void onBtnLetter(){ playLetter('x'); }

    private void onBtnLetter2() { playLetter2('x');}




}