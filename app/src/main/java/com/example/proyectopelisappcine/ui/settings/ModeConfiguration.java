package com.example.proyectopelisappcine.ui.settings;

import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;

public class ModeConfiguration {
    private ModeConfiguration() {

    }

    public enum Mode {
        Day_Light, Night_Light, Default
    }

    public static void EspecificarMode(Mode mode) {
        switch (mode) {
            case Day_Light:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Night_Light:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
        }
    }
}