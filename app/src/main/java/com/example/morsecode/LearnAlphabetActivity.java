package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class LearnAlphabetActivity extends AppCompatActivity {
    private final MorseCode morseCode = new MorseCode();
    Vibrator vibrator;
    VibrationManager vibrationManager;
    SoundManager soundManager;
    private final ToneGenerator toneGenerator =  new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.TONE_SUP_DIAL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_alphabet);

        Button aButton = findViewById(R.id.button_a);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_layout);
        ImageView imageView = dialog.findViewById(R.id.a_morse_img);
        Button playButton = dialog.findViewById(R.id.play_button);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); // Initialize the vibrator
        vibrationManager = new VibrationManager(this);
        soundManager = new SoundManager();
        aButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.amorse);
                dialog.show();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
                soundManager.playLetter('A');
                vibrationManager.playLetter('a');
                //playLetter('A', 0); // Provide the appropriate arguments to playLetter() method
            }
        });
    }

    private void playLetter(Character c, int soundIdx) {
        String morse = morseCode.getMorse(c);

        if (morse != null) {
            new Thread(() -> {
                synchronized (toneGenerator) {
                    try {
                        for (int i = 0; i < morse.length(); i++) {
                            if (morse.charAt(i) == '.') {
                                playShortTone(soundIdx);
                                playShortVibe();
                                Thread.sleep(1000);  // Pause for 1 second between short tones/vibrations
                            } else if (morse.charAt(i) == '-') {
                                playLongTone(soundIdx);
                                playLongVibe();
                                Thread.sleep(2000);  // Pause for 2 seconds between long tones/vibrations
                            }
                            Thread.sleep(500);  // Pause for 0.5 seconds between each dot/dash
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }




    private void playShortVibe() {
        vibrator.vibrate(100);
    }

    private void playLongVibe() {
        vibrator.vibrate(500);
    }

    private void playLongTone(int i) {
        toneGenerator.startTone(ToneGenerator.TONE_SUP_DIAL, 500);
    }

    private void playShortTone(int i) {
        toneGenerator.startTone(ToneGenerator.TONE_SUP_DIAL, 100);
    }
}
