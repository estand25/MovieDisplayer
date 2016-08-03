package com.example.andriod.popularmoviev2.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.other.Constants;
import com.example.andriod.popularmoviev2.other.Utility;

/**
 * IntentService that handles that transfer of favorite movie table data to movies
 *
 * Created by StandleyEugene on 7/28/2016.
 */
public class FavoriteMovieService extends IntentService{
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

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
     * Populate through contentResolver movie with favorite movies
     * @param favorIntent - Service intent that passes service necessary information
     */
    @Override
    protected void onHandleIntent(Intent favorIntent){
        // Delete all the other tables associated to the correct GridView display
        mContentResolver.delete(
                MovieContract.MovieEntry.CONTENT_URI,"movie.movie_type = ?",
                new String[]{"favorite_movie"});

        // Create cursor with all the current favorite movies
        Cursor FavorCursor = mContentResolver.query(
                MovieContract.FavoriteMovies.CONTENT_URI,
                null,
                null,
                null,
                null);

        // Move to position 0 in cursor
        if(FavorCursor.moveToPosition(0)){

            // ContentValue Array that I will past to bulkInsert
            ContentValues[] bulkFavoriteMovies = new ContentValues[FavorCursor.getCount()];

            // Loop through added the individual favorite movie details to the ContentValue
            for(int i = 0;i < FavorCursor.getCount();i++){
                // Content that will hold favorite movie information
                // retrieved from the favorite_movie table
                ContentValues favoriteMovieContent = new ContentValues();

                // Set the value of each column and insert the favorite movie for movie property
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, FavorCursor.getString(1));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, FavorCursor.getString(2));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_ADULT, FavorCursor.getString(3));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, FavorCursor.getString(4));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, FavorCursor.getString(5));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS,  FavorCursor.getString(6));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, FavorCursor.getString(7));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, FavorCursor.getString(8));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_TITLE, FavorCursor.getString(9));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, FavorCursor.getString(10));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_POPULARITY, FavorCursor.getString(11));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, FavorCursor.getString(12));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_VIDEO, FavorCursor.getString(13));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, FavorCursor.getString(14));
                favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE, "favorite_movie"); //blank if not favorite

                // Add the favorite movie detail to the ContentValue array
                bulkFavoriteMovies[i] = favoriteMovieContent;

                Log.v("Insert","Favorite Movie - " + FavorCursor.getString(9));

                // Move to the next row in cursor
                FavorCursor.moveToNext();
            }

            // Bulk insert all the favorite movies into the movie table to display
            mContentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI, bulkFavoriteMovies);

            // Close cursor
            FavorCursor.close();
        }
    }
}
