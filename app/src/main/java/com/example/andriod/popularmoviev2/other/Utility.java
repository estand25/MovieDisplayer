package com.example.andriod.popularmoviev2.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.example.andriod.popularmoviev2.R;

/**
 * Utilities class with utility methods
 *
 * Created by StandleyEugene on 7/11/2016.
 */
public class Utility {
    /**
     * Get the current prefer for the app
     * @param context - Context information
     * @return - Return static String with temperature key
     */
    public static String getPreferredMovieType(Context context) {
        // Get the current Shared Preferences for the app
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(context);

        // Popular the type of movie field with the current sort option for the App
        return sharedPref.getString(
                context.getString(R.string.pref_sort_option_key),
                context.getString(R.string.pref_sort_option_default));
    }

    /**
     * Determine what type of screen is being used got it from this post
     * (http://stackoverflow.com/questions/9279111/determine-if-the-device-is-a-smartphone-or-tablet)
     *
     * @param context - Context information
     * @return - Return static boolean if tablet or phone
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
