package com.example.andriod.popularmoviev2.data;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.example.andriod.popularmoviev2.BuildConfig;
import com.example.andriod.popularmoviev2.activity.DetailMovieFragment;
import com.example.andriod.popularmoviev2.model.Genre;
import com.example.andriod.popularmoviev2.model.Genres;
import com.example.andriod.popularmoviev2.model.Movie;
import com.example.andriod.popularmoviev2.model.MovieColl;
import com.example.andriod.popularmoviev2.model.Review;
import com.example.andriod.popularmoviev2.model.ReviewColl;
import com.example.andriod.popularmoviev2.model.Trailer;
import com.example.andriod.popularmoviev2.model.TrailerColl;
import com.example.andriod.popularmoviev2.other.Utility;
import com.example.andriod.popularmoviev2.service.TheMovieDBAPIService;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Note: Got the information for a initial idea from a thread posts below
 * I didn't want to do an IntentService so AbstractThreadSyncAdapter was the only
 * option post below:
 * http://blog.mediarain.com/2013/05/building-a-better-rest-client/
 * https://discussions.udacity.com/t/after-sync-not-all-movies-inserted-to-database/161971
 * https://developer.android.com/training/sync-adapters/creating-sync-adapter.html
 *
 * Followed this training
 * https://developer.android.com/training/sync-adapters/creating-sync-adapter.html#CreateSyncAdapter
 *
 * Helper class used during a couple of time during the app to popular
 * specific tables and get the initial information for the db.
 * Created by StandleyEugene on 7/10/2016.
 */
public class MovieSyncUploader extends AbstractThreadedSyncAdapter {
    private static String movieRoot = "http://api.themoviedb.org/3/";
    protected final String TAG = getClass().getSimpleName();
    // List of current movie
    List<Movie> popularMovie;

    // Global variable
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    Uri mUri;

    /**
     * Set up the sync adatper
     * @param context - Get the current Context
     * @param autoInitialize - Boolean from auto initialize
     */
    public MovieSyncUploader(Context context, boolean autoInitialize){
        super(context,autoInitialize);

        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adatper. This form of the constructor
     * maintains compatibility with Android 3.0 and later platform
     *
     * @param context - Get the current Context
     * @param autoInitialize - Boolean from auto initialize
     * @param allowParallelSyncs - Boolean from allow paralle sync
     */
    public MovieSyncUploader(Context context,
                             boolean autoInitialize,
                             boolean allowParallelSyncs){
        super(context,autoInitialize,allowParallelSyncs);

        mContentResolver = context.getContentResolver();
    }

    /*
        Specify the code you want to run in the sync adapter. The entire
        sync adapter run a background thread, so you don't have to set
        up your own back processing
     */
    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {
    }

    /**
     * Update movie & add favorite_movie for the specific movie id for the favorite list
     * @param movieId - The movie id to update movie and add to the favorite_movie table
     */
    public void updateMovieFavorite(int movieId, String input){
        // Content Value that holds the movie type (Blank or Favor)
        // Note: FAVOR movie will appear in movie list
        ContentValues movieTypeContent = new ContentValues();
        movieTypeContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE,input);

        // Update the movie type field in movie
        mContentResolver.update(MovieContract.MovieEntry.CONTENT_URI,
                movieTypeContent,
                "movie.movie_id = ?",
                new String[]{String.valueOf(movieId)});
    }

    /**
     * Insert movie id into the favorite_movie table
     * @param movieStuff - Movie array of movie parts
     */
    public void insertFavoriteMovie(String[] movieStuff){
        // Content Value that holds the favorite movie information
        ContentValues favorites = new ContentValues();

        favorites.put(MovieContract.FavoriteMovies.COLUMN_MOVIE_ID, movieStuff[0]);
        favorites.put(MovieContract.FavoriteMovies.COLUMN_POSTER_PATH, movieStuff[1]);
        favorites.put(MovieContract.FavoriteMovies.COLUMN_ADULT, "");
        favorites.put(MovieContract.FavoriteMovies.COLUMN_OVERVIEW, movieStuff[2]);
        favorites.put(MovieContract.FavoriteMovies.COLUMN_RELEASE_DATE, movieStuff[3]);
        favorites.put(MovieContract.FavoriteMovies.COLUMN_GENRE_IDS, movieStuff[4]);
        favorites.put(MovieContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE, movieStuff[5]);
        favorites.put(MovieContract.FavoriteMovies.COLUMN_ORIGINAL_LANGUAGE, movieStuff[6]);
        favorites.put(MovieContract.FavoriteMovies.COLUMN_TITLE, movieStuff[7]);
        favorites.put(MovieContract.FavoriteMovies.COLUMN_BACKDROP_PATH, movieStuff[8]);
        favorites.put(MovieContract.FavoriteMovies.COLUMN_POPULARITY, movieStuff[9]);
        favorites.put(MovieContract.FavoriteMovies.COLUMN_VOTE_COUNT, movieStuff[10]);
        favorites.put(MovieContract.FavoriteMovies.COLUMN_VIDEO, "");
        favorites.put(MovieContract.FavoriteMovies.COLUMN_VOTE_AVERAGE, movieStuff[11]);

        // Insert the movie id into favorite table
        mContentResolver.insert(MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(Integer.parseInt(movieStuff[0])),
                favorites);
    }

    /**
     * Query favorite_movie and see if movie already there
     * @param movieId - The movie id to check for
     * @return - Returns true if movie id already exists and false if it doesn't
     */
    public boolean queryFavoriteMovie(int movieId){
        // Create cursor with favorite movie id for passed in movie id
        Cursor cursor = mContentResolver.query(MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(movieId),
                null,
                null,
                null,
                null);

        // Set boolean based on if cursor can moveToFirst
        boolean result = cursor.moveToFirst();

        // Close the cursor before return
        cursor.close();

        // Return result of move to first for cursor
        return result;
    }

    /**
     * Check if favorite movie exist in the table
     * @param movieId - The movie id to check for
     */
    public void chkFavoriteMovie(int movieId){
        // Check if movie is in the favorite_movie table
        if(queryFavoriteMovie(movieId)){
            updateMovieFavorite(movieId,"FAVOR");
        }
    }

    /**
     * Delete movie from favorite_movie
     * @param movieId - The movie id to delete from the table
     */
    public void deleteFavoriteMovie(int movieId){
        // ContentResolver that deletes the specified movie from the favorite_movie table
        int i = mContentResolver.delete(MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(movieId),
                                       MovieContract.FavoriteMovies.TABLE_NAME + "." + MovieContract.FavoriteMovies.COLUMN_MOVIE_ID + " = ? ",
                                       new String[]{MovieContract.MovieEntry.getMovieID(MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(movieId))});

        // Updated the notifyChange to update  the content provide for favorite_move and movie table
        getContext().getContentResolver().notifyChange(MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(movieId),null);
        getContext().getContentResolver().notifyChange(MovieContract.MovieEntry.CONTENT_URI,null);
    }

     /**
     * Delete all the other tables associated to the correct GridView display
     */
    public void deleteAllOtherTable(){
        mContentResolver.delete(MovieContract.MovieEntry.CONTENT_URI,"", new String[]{});
        mContentResolver.delete(MovieContract.ReviewEntry.CONTENT_URI,"", new String[]{});
        mContentResolver.delete(MovieContract.TrailerEntry.CONTENT_URI,"", new String[]{});
    }

}
