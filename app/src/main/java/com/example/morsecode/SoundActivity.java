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

        Button shortButton3 = findViewById(R.id.shortButton3);
        Button longButton3 = findViewById(R.id.longButton3);
        Button letterButton3 = findViewById(R.id.letterButton3);

        Button shortButton4 = findViewById(R.id.shortButton4);
        Button longButton4 = findViewById(R.id.longButton4);
        Button letterButton4 = findViewById(R.id.letterButton4);

        shortButton.setOnClickListener(view -> onBtnShort(1));
        longButton.setOnClickListener(view -> onBtnLong(1));
        letterButton.setOnClickListener(view -> onBtnLetter(1));

        shortButton2.setOnClickListener(view -> onBtnShort(2));
        longButton2.setOnClickListener(view -> onBtnLong(2));
        letterButton2.setOnClickListener(view -> onBtnLetter(2));

        shortButton3.setOnClickListener(view -> onBtnShort(3));
        longButton3.setOnClickListener(view -> onBtnLong(3));
        letterButton3.setOnClickListener(view -> onBtnLetter(3));

        shortButton4.setOnClickListener(view -> onBtnShort(4));
        longButton4.setOnClickListener(view -> onBtnLong(4));
        letterButton4.setOnClickListener(view -> onBtnLetter(4));

    }

    private void playLetter( Character c, int soundIdx){
        String morse = morseCode.getMorse(c);

        new Thread(() -> {
            synchronized (toneGenerator){
                try {
                    for(int i = 0; i < morse.length(); i++){
                        if(morse.charAt(i) == '.'){
                            playShortTone(soundIdx);
                        }else{
                            playLongTone(soundIdx);
                        }
                        toneGenerator.wait(100+500);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void playSound(int len){ toneGenerator.startTone(ToneGenerator.TONE_DTMF_8,len); }

    private void playSound2(int len){ toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, len); }

    private void playSound3(int len){ toneGenerator.startTone(ToneGenerator.TONE_SUP_DIAL, len); }

    private void playSound4(int len){ toneGenerator.startTone(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, len); }

    private void playLongTone(int i){
        switch (i){
            case 1 : playSound(500); break;
            case 2 : playSound2(500); break;
            case 3 : playSound3(500); break;
            case 4 : playSound4(500); break;
        }
     }
    private void playShortTone(int i){
        switch (i){
            case 1 : playSound(100); break;
            case 2 : playSound2(100); break;
            case 3 : playSound3(100); break;
            case 4 : playSound4(100); break;
        }
    }

    private void onBtnShort(int i){
        playShortTone(i);
    }
    private void onBtnLong(int i){ playLongTone(i);}

    private void onBtnLetter(int i){ playLetter('x', i); }
}