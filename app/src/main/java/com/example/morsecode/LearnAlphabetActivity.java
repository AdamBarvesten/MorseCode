package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class LearnAlphabetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_alphabet);

        Button aButton = findViewById(R.id.button_a);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_layout);
        ImageView imageView = dialog.findViewById(R.id.a_morse_img);

        aButton.setOnClickListener(v -> {
            imageView.setImageResource(R.drawable.amorse);
            dialog.show();
        });

        imageView.setOnClickListener(v -> dialog.dismiss());
    }
}
