package com.example.andriod.popularmoviev2.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Handles the setting and getting of SharedPreferences for Last Movie type selected
 * Popular Movie, Top Rated Movie, &  Favorite Movie
 * I wasn't really sure how to do this, but I look around and find this here:
 * http://stackoverflow.com/questions/3624280/how-to-use-sharedpreferences-in-android-to-store-fetch-and-edit-values
 *
 * Created by StandleyEugene on 8/2/2016.
 */
public class LastSelectedMovieType {
    private static final String LAST_SELECTED_MOVIE_TYPE_KEY = "LAST_SELECTED_MOVIE_TYPE_KEY";
    private static final String RESET_GRIDVIEW_POSITION = "RESET_GRIDVIEW_POSITION";
    private static SharedPreferences mPreferences;
    private static LastSelectedMovieType mInstance;
    private static Editor mEditor;

    private LastSelectedMovieType() {
    }

    /**
     * Get the instance based on the Context
     * @return - Return the instance for this class
     */
    public static LastSelectedMovieType getInstance(){
        if(mInstance == null){
            Context context = Constants.cConetext;
            mInstance = new LastSelectedMovieType();
            mPreferences = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
        }
        return mInstance;
    }

    /**
     * Get the String Key for the Preferences
     * @return - Return the String preference for this key
     */
    public String getStringKey() {
        return mPreferences.getString(LAST_SELECTED_MOVIE_TYPE_KEY, "");
    }

    /**
     * Set the string value for the Preferences
     * @param value - The value to set for the Preference
     */
    public void setStringKey(String value) {
        mEditor.putString(LAST_SELECTED_MOVIE_TYPE_KEY, value).apply();
    }

    /**
     * Get the String Key for the Preferences
     * @return - Return the String preference for this key
     */
    public String getStringResetKey() {
        return mPreferences.getString(RESET_GRIDVIEW_POSITION, "");
    }

    /**
     * Set the string value for the Preferences
     * @param value - The value to set for the Preference
     */
    public void setStringesetKey(String value) {
        mEditor.putString(RESET_GRIDVIEW_POSITION, value).apply();
    }
}
