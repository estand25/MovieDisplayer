package com.example.andriod.popularmoviev2.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Helper class used a couple of times during the app to update or delete tables
 *
 * Created by StandleyEugene on 7/10/2016.
 */
public class MovieTableSync {
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    Context myContext;

    /**
     * Set up the sync adatper. This form of the constructor
     * maintains compatibility with Android 3.0 and later platform
     *
     * @param context - Get the current Context
     */
    public MovieTableSync(Context context){
        mContentResolver = context.getContentResolver();
        myContext = context;
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
        // Because a null deletes all rows
        if (i != 0) {
            myContext.getContentResolver().notifyChange(MovieContract.MovieEntry.CONTENT_URI,null);
            myContext.getContentResolver().notifyChange(MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(movieId), null);
        }
    }

     /**
     * Delete all the other tables associated to the correct GridView display
     */
    public void deleteAllOtherTable(){
        mContentResolver.delete(MovieContract.MovieEntry.CONTENT_URI,"", new String[]{});
        mContentResolver.delete(MovieContract.ReviewEntry.CONTENT_URI,"", new String[]{});
        mContentResolver.delete(MovieContract.TrailerEntry.CONTENT_URI,"", new String[]{});
    }

    /**
     * Get the current row information for one movie
     * @param movieID - Specific movie id to look for
     * @return - Cursor with current movie row information
     */
    public Cursor getMovieType(int movieID){
        Cursor cursor = mContentResolver.query(MovieContract.MovieEntry.buildMovieIDUri(movieID),
                null,//new String[]{"movie_type"},
                null,
                null,
                null
        );
        cursor.moveToPosition(0);
        return cursor;
    }

}
