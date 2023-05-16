package com.example.morsecode;

import android.content.Context;
import android.os.Vibrator;

public class VibrationManager {
    private Context context;
    private Vibrator vibrator;

    public VibrationManager(Context context) {
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void vibrate(long milliseconds) {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(milliseconds);
        }
    }

    public void playLongVibration() {
        vibrate(200);
    }
    public void playShortVibration() {
        vibrate(100);
    }
}

