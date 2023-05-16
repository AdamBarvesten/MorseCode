package com.example.morsecode;

import android.content.Context;
import android.os.Vibrator;

public class VibrationManager {
    private Context context;
    private final Vibrator vibrator;
    private MorseCode morseCode;

    public VibrationManager(Context context) {
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        morseCode = new MorseCode();

    }

    private void vibrate(long milliseconds) {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(milliseconds);
        }
    }

    public void playLetter(Character character){
        Character c = Character.toLowerCase(character);
        String morse = morseCode.getMorse(c);

        new Thread(() -> {
            synchronized (vibrator){
                try {
                    for(int i = 0; i < morse.length(); i++){
                        if(morse.charAt(i) == '.'){
                            playShortVibration();
                        }else{
                            playLongVibration();
                        }
                        vibrator.wait(100+500);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


    }


    public void playLongVibration() {
        vibrate(200);
    }
    public void playShortVibration() {
        vibrate(100);
    }
}

