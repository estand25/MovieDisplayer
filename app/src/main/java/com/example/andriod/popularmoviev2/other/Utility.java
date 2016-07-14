package com.example.andriod.popularmoviev2.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.andriod.popularmoviev2.R;

/**
 * Created by StandleyEugene on 7/11/2016.
 */
public class Utility {
    /**
     * Get the current prefer for the app
     * @param context
     * @return
     */
    public static String getPreferredMovieType(Context context) {
        // Get the current Shared Preferences for the app
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(context);

        // Popular the location field with the current sort option for the App
        return sharedPref.getString(
                context.getString(R.string.pref_sort_option_key),
                context.getString(R.string.pref_sort_option_default));
    }
}
