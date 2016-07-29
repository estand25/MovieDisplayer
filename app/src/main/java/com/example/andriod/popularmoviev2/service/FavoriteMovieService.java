package com.example.andriod.popularmoviev2.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import com.example.andriod.popularmoviev2.data.MovieContract;

/**
 * IntentService that handles that transfer of favorite movie table data to movies
 *
 * Created by StandleyEugene on 7/28/2016.
 */
public class FavoriteMovieService extends IntentService{
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
    public FavoriteMovieService(){
        super("FavoriteMovieService");
    }

    /**
     * Populate through contentResolver movie with favorite movies
     */
    @Override
    protected void onHandleIntent(Intent favorIntent){
        // Set the current context content Resolver
        mContentResolver = getApplicationContext().getContentResolver();

        // Create cursor with all the current favorite movies
        Cursor cursor = mContentResolver.query(MovieContract.FavoriteMovies.CONTENT_URI,
                null,
                null,
                null,
                null);

        // Move to position 0 in cursor
        cursor.moveToPosition(0);

        // ContentValue Array that I will past to bulkInsert
        ContentValues[] bulkFavoriteMovies = new ContentValues[cursor.getCount()];

        // Set the movie type
        String favor = "FAVOR";

        // Loop through added the individual favorite movie details to the ContentValue
        for(int i = 0;i<= (cursor.getCount()-1);i++){
            // Content that will hold favorite movie information
            // retrieved from the favorite_movie table
            ContentValues favoriteMovieContent = new ContentValues();

            // Set the value of each column and insert the favorite movie for movie property
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, cursor.getString(1));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, cursor.getString(2));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_ADULT, cursor.getString(3));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, cursor.getString(4));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, cursor.getString(5));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS,  cursor.getString(6));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, cursor.getString(7));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, cursor.getString(8));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_TITLE, cursor.getString(9));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, cursor.getString(10));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_POPULARITY, cursor.getString(11));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, cursor.getString(12));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_VIDEO, cursor.getString(13));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, cursor.getString(14));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE, favor); //blank if not favorite

            // Add the favorite movie detail to the ContentValue array
            bulkFavoriteMovies[i] = favoriteMovieContent;

            // Move to the next row in cursor
            cursor.moveToNext();
        }
        // Close cursor
        cursor.close();

        // Bulk insert all the favorite movies into the movie table to display
        mContentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI,bulkFavoriteMovies);
    }
}
