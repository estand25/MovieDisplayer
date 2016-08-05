package com.example.andriod.popularmoviev2.activity;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.other.LastSelectedMovieType;
import com.example.andriod.popularmoviev2.other.Utility;

/**
 * Setting activity option screen
 *
 * Created by StandleyEugene on 6/30/2016.
 */
public class SettingActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    /**
     * Taken from Sun-shine app (SettingsActivity.java file)
     * @param savedInstanceState
     */
    @Override @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_option_key)));

        Log.v("Create", "SettingActivity - onCreate");
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     *
     * Taken from Sun-shine app (SettingsActivity.java file)
     */
    @SuppressWarnings("deprecation")
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * Taken from Sun-shine app (SettingsActivity.java file)
     *
     * @param preference
     * @param value
     * @return
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }
}
