package com.example.proyectopelisappcine.ui.settings;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.proyectopelisappcine.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootkey) {
        setPreferencesFromResource(R.xml.preferences, rootkey);

        ListPreference listPreference = getPreferenceManager().findPreference(getString(R.string.settings_mode));
        if (listPreference.getValue() == null) {
            listPreference.setValue(ModeConfiguration.Mode.Default.name());
        }
        listPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            ModeConfiguration.EspecificarMode(ModeConfiguration.Mode.valueOf((String) newValue));
            return true;
        });
    }
}