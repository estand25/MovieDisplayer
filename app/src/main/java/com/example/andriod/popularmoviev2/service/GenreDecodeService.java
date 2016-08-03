package com.example.andriod.popularmoviev2.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.other.Constants;

/**
 * IntentService that handle the decoding of the individual movie's
 * genre id list to genre name list
 *
 * Created by StandleyEugene on 7/30/2016.
 */
public class GenreDecodeService extends IntentService{
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    // Protected string for class simple
    protected final String TAG = getClass().getSimpleName();

    // String[] of genre columns
    private static final String[] GENRE_COLUMNS = {
            MovieContract.GenreEntry.COLUMN_GENRE_ID,
            MovieContract.GenreEntry.COLUMN_NAME
    };

    // These indices are tied to GENRE_COLUMNS. If GENRE_COLUMNS change, these need change too
    static final int COL_GENRE_ID = 0;
    static final int COL_GENRE_NAME = 1;

    /**
     * An IntentService must always have a constructor that calls super
     * constructor. The string supplied to the super constructor is used to give
     * a name to the IntentService's background thread
     *
     * Note: Got the above text from the Android Developers below list
     * https://developer.android.com/training/run-background-service/create-service.html#CreateClass
     * Context context
     */
    public GenreDecodeService(){
        super("GenreDecodeService");
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
     * Populate through contentResolver the movie genre id for movie (decode from id -> name)
     * @param intent - Service intent that passes service necessary information
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Get data from the movieIntent
        final String[] movieGenreIDs = intent.getStringArrayExtra(Constants.GENRE_ID);

        // String[] of comma separate genre id
        final String[] genres = getIndividualGenreID(movieGenreIDs[0]);

        // Check if genres has already been replaced with genre name
        // I look around for a little weight solution instead of loop or something else
        // Note: Found this post while I was looking
        // http://stackoverflow.com/questions/14206768/how-to-check-if-a-string-is-numeric
        if (!genres[0].matches("[-+]?\\d*\\.?\\d+")) {
            return;
        }

        // Blank the genreName field to start
        String genreName = "";

        // Loop through the movie's genre names
        for (int i = 0; i <= (genres.length - 1); i++){
            // Create cursor with all the current favorite movies
            Cursor cursor = mContentResolver.query(MovieContract.GenreEntry.CONTENT_URI.buildUpon().appendPath(genres[i]).build(),
                    null,
                    null,
                    null,
                    null);

            // Move to position 0 in cursor
            cursor.moveToPosition(0);

            // Get all the Genre Name and update Genre id in the Movie
            if(genreName.equals("")){
                genreName = cursor.getString(COL_GENRE_NAME);
            }else{
                genreName = genreName + "|" + cursor.getString(COL_GENRE_NAME);
            }

            // Close cursor after getting the genre names
            // based on the genre id is done (free-up a little memory)
            cursor.close();
        }

        // Content that holds all the genre name information
        // retrieved from the Movie DB API
        ContentValues movieIdNameContent = new ContentValues();
        movieIdNameContent.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS,genreName);

        // Update the Genre ids field in movie from genre id to genre name plus | in-between
        mContentResolver.update(MovieContract.MovieEntry.CONTENT_URI,
                movieIdNameContent,
                "movie.movie_id = ?",
                new String[]{movieGenreIDs[1]});
    }

    /**
     * Remove first and end brackets then splits the line by comma
     * @param line - String of genre id with brackets
     * @return - String[] of genres id without brackets
     */
    public String[] getIndividualGenreID(String line){
        String result = "";
        result = line.substring(1, line.length()-1);

        // Note: I know is little regression pattern, but I'm not that good
        // so I had to search around to found this one
        // http://stackoverflow.com/questions/15633228/how-to-remove-all-white-spaces-in-java
        result = result.replaceAll("\\s+","");
        return result.split(",");
    }
}
