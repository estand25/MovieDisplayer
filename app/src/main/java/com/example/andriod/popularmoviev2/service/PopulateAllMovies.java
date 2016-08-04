package com.example.andriod.popularmoviev2.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.util.Log;

/**
 * IntentService that populates both types of movies Popular Movie & Top Rate Movie
 * plus Genre information
 *
 * Using Android Develop web/app to make this scheduled
 * https://developer.android.com/training/scheduling/alarms.html#type
 *
 * Created by StandleyEugene on 8/1/2016.
 */
public class PopulateAllMovies extends IntentService {
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    // Protected string for class simple
    protected final String TAG = getClass().getSimpleName();

    /**
     * An IntentService must always have a constructor that calls super
     * constructor. The string supplied to the super constructor is used to give
     * a name to the IntentService's background thread
     *
     * Note: Got the above text from the Android Developers below list
     * https://developer.android.com/training/run-background-service/create-service.html#CreateClass
     */
    public PopulateAllMovies(){
        super("PopulateAllMovies");
    }

    /**
     * On the IntentService create I set-up the ContentResolver for us by the
     * onHandleIntent for the services work
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Set the current context content Resolver
        mContentResolver = getApplicationContext().getContentResolver();
    }

    /**
     * Populate SQLiteDatabase with Genre information, Popular Movie information,
     * and Top Rated Movie information
     *
     * @param intent - Intent used to pass information to onHandleIntent if need
     */
    @Override
    protected void onHandleIntent(Intent intent){
        Log.v(TAG,"Starting Movie Services ...");

        // Populate the Movie Genres to get the genre name during the PopularMovieService & TopRateMovieService
        getApplicationContext().startService(new Intent(getApplicationContext(), GenreInfoService.class));

        // Popular Movie Service from The Movie DB API
        getApplicationContext().startService(new Intent(getApplicationContext(), PopularMovieService.class));

        // Top Rated Movie Service from The Movie DB API
        getApplicationContext().startService(new Intent(getApplicationContext(), TopRatedMovieService.class));

        Log.v(TAG,"Ending Movie Services ...");
    }
}
