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
    Vibrator vibrator;
    VibrationManager vibrationManager;
    SoundManager soundManager;
    ImageView imageView;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_alphabet);

        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_layout);
        imageView = dialog.findViewById(R.id.a_morse_img);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); // Initialize the vibrator
        vibrationManager = new VibrationManager(this);
        soundManager = new SoundManager();

        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            int buttonId = getResources().getIdentifier("button_" + alphabet, "id", getPackageName());
            Button button = findViewById(buttonId);
            final char currentAlphabet = alphabet;
            button.setOnClickListener(v -> showImage(String.valueOf(currentAlphabet)));
        }
    }

    private void showImage(String message) {
        String resourceName = message + "morse";
        int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        imageView.setImageResource(resourceId);
        dialog.show();

        Button playButton = dialog.findViewById(R.id.play_button);
        playButton.setOnClickListener(v -> {
            soundManager.playLetter(message.charAt(0));
            vibrationManager.playLetter(message.charAt(0));
        });
    }
}
