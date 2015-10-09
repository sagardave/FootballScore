package com.example.sdave.footballscore;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import com.example.sdave.footballscore.R;

/**
 * Created by sdave on 4/16/2015.
 */
public class SettingActivity extends PreferenceActivity {
    DBAdapter myDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB = new DBAdapter(this);
        myDB.open();

        addPreferencesFromResource(R.xml.settings);

        Preference button = findPreference(getString(R.string.reset_database));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
            myDB.removeAll();
            return true;
            }
        });

    }
}
