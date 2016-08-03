package com.example.andriod.popularmoviev2.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Handles the setting and getting of SharedPreferences for Last Activity
 * I wasn't really sure how to do this, but I look around and find this here:
 * http://stackoverflow.com/questions/3624280/how-to-use-sharedpreferences-in-android-to-store-fetch-and-edit-values
 *
 * Created by StandleyEugene on 8/2/2016.
 */
public class LastActivity {
    private static final String LAST_ACTIVITY_KEY = "LAST_ACTIVITY_KEY";
    private static SharedPreferences mPreferences;
    private static LastActivity mInstance;
    private static Editor mEditor;
    private static LastActivity ourInstance;

    private LastActivity() {
    }

    /**
     * Get the instance based on the Context
     * @return - Return the instance for this class
     */
    public static LastActivity getInstance(){
        if(mInstance == null){
            Context context = Constants.cConetext;
            mInstance = new LastActivity();
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
        return mPreferences.getString(LAST_ACTIVITY_KEY, "");
    }

    /**
     * Set the string value for the Preferences
     * @param value - The value to set for the Preference
     */
    public void setStringKey(String value) {
        mEditor.putString(LAST_ACTIVITY_KEY, value).apply();
    }
}
