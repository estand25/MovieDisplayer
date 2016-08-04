package com.example.andriod.popularmoviev2.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * Helper class used a couple of times during the app to update or delete tables
 *
 * Created by StandleyEugene on 7/10/2016.
 */
public class MovieTableSync {
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    // The current Context for the app
    Context myContext;

    /**
     * Construction for the MovieTableSync class
     *
     * @param context - Get the current Context
     */
    public MovieTableSync(Context context){
        mContentResolver = context.getContentResolver();
        myContext = context;
    }

    /**
     * Insert movie id into the favorite_movie, favorite_review, & favorite_trailer tables
     *
     * @param movieId - The movie id to update movie and add to the favorite_movie table
     */
    public void insertFavoriteMovie(int movieId){
        // Cursor with Movie data
        Cursor movieCursor = mContentResolver.query(
                MovieContract.MovieEntry.buildMovieIDUri(movieId),
                null,
                null,
                null,
                null
        );

        // Move to position 0 in each of the cursor
        movieCursor.moveToPosition(0);

        // Content Value that holds the favorite movie information
        ContentValues favorites = new ContentValues();

        // Add values to content
        favorites.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieCursor.getString(1));
        favorites.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movieCursor.getString(2));
        favorites.put(MovieContract.MovieEntry.COLUMN_ADULT, movieCursor.getInt(3));
        favorites.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieCursor.getString(4));
        favorites.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieCursor.getString(5));
        favorites.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS, movieCursor.getString(6));
        favorites.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movieCursor.getString(7));
        favorites.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movieCursor.getString(8));
        favorites.put(MovieContract.MovieEntry.COLUMN_TITLE, movieCursor.getString(9));
        favorites.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movieCursor.getString(10));
        favorites.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movieCursor.getDouble(11));
        favorites.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movieCursor.getInt(12));
        favorites.put(MovieContract.MovieEntry.COLUMN_VIDEO, movieCursor.getInt(13));
        favorites.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movieCursor.getDouble(14));
        favorites.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE, "favorite_movie");

        // Insert the movie id into favorite table
        mContentResolver.insert(MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(movieId),
                favorites);

        // Cursor with Review data for Movie
        Cursor reviewCursor = mContentResolver.query(
                MovieContract.ReviewEntry.buildReviewMovieIDUri(movieId),
                null,
                null,
                null,
                null
        );

        // Move to position 0 in each of the cursor
        reviewCursor.moveToPosition(0);

        // ContentValue Array that I will past to bulk insert
        ContentValues[] bulkMovieReview = new ContentValues[reviewCursor.getCount()];

        // Loop through all the individual movie's reviews
        for(int r = 0; r < reviewCursor.getCount();r++){
            // Content Value that holds the favorite movie information
            ContentValues favoriteReview = new ContentValues();

            // Set the value of each column and insert the review properties
            favoriteReview.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, reviewCursor.getString(1));
            favoriteReview.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, reviewCursor.getInt(2));
            favoriteReview.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, reviewCursor.getString(3));
            favoriteReview.put(MovieContract.ReviewEntry.COLUMN_CONTENT, reviewCursor.getString(4));
            favoriteReview.put(MovieContract.ReviewEntry.COLUMN_URL, reviewCursor.getString(5));
            favoriteReview.put(MovieContract.ReviewEntry.COLUMN_MOVIE_TYPE, "favorite_movie");

            // Add review details to the ContentValue array
            bulkMovieReview[r] = favoriteReview;

            // Move the Cursor to the next row
            reviewCursor.moveToNext();
        }

        // Insert the movie id into favorite table
        mContentResolver.bulkInsert(MovieContract.FavoriteReviewEntry.buildFavoriteReviewMovieIDUri(movieId),
                bulkMovieReview);

        // Cursor with Review data for Movie
        Cursor trailerCursor = mContentResolver.query(
                MovieContract.TrailerEntry.buildTrailerMovieIDUri(movieId),
                null,
                null,
                null,
                null
        );

        // Move to position 0 in each of the cursor
        trailerCursor.moveToPosition(0);

        // ContentValue Array that I will past to bulk insert
        ContentValues[] bulkMovieTrailer = new ContentValues[trailerCursor.getCount()];

        // Loop through all the individual movie's trailer
        for(int t = 0; t < trailerCursor.getCount();t++){

            // Content Value that holds the favorite movie information
            ContentValues favoriteTrailer = new ContentValues();

            // Set the value of each column and insert the trailer properties
            favoriteTrailer.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, trailerCursor.getString(1));
            favoriteTrailer.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, trailerCursor.getInt(2));
            favoriteTrailer.put(MovieContract.TrailerEntry.COLUMN_ISO_6391, trailerCursor.getString(4));
            favoriteTrailer.put(MovieContract.TrailerEntry.COLUMN_ISO_31661, trailerCursor.getString(5));
            favoriteTrailer.put(MovieContract.TrailerEntry.COLUMN_KEY, trailerCursor.getString(6));
            favoriteTrailer.put(MovieContract.TrailerEntry.COLUMN_NAME, trailerCursor.getString(7));
            favoriteTrailer.put(MovieContract.TrailerEntry.COLUMN_SITE, trailerCursor.getString(8));
            favoriteTrailer.put(MovieContract.TrailerEntry.COLUMN_SIZE, trailerCursor.getInt(9));
            favoriteTrailer.put(MovieContract.TrailerEntry.COLUMN_TYPE, trailerCursor.getString(10));
            favoriteTrailer.put(MovieContract.TrailerEntry.COLUMN_MOVIE_TYPE, "favorite_movie");

            // Add trailer details to the ContentValue array
            bulkMovieTrailer[t] = favoriteTrailer;

            // Move the Cursor to the next row
            trailerCursor.moveToNext();
        }

        // Insert the movie id into favorite table
         mContentResolver.bulkInsert(MovieContract.FavoriteTrailerEntry.buildFavoriteTrailerMovieIDUri(movieId),
                 bulkMovieTrailer);

        // Close favorite movie cursors
        movieCursor.close();
        reviewCursor.close();
        trailerCursor.close();
    }

    /**
     * Query favorite_movie and see if movie already there
     *
     * @param movieId - The movie id to check for
     * @return - Returns true if movie id already exists and false if it doesn't
     */
    public boolean queryFavoriteMovie(int movieId){
        // Create cursor with favorite movie id for passed in movie id
        Cursor cursor = mContentResolver.query(
                MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(movieId),
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
     * Delete movie from favorite_movie, favorite_review, & favorite_trailer
     *
     * @param movieId - The movie id to delete from the table
     */
    public void deleteFavoriteMovie(int movieId){
        // ContentResolver that deletes the specified movie from the favorite_movie table
        int a = mContentResolver.delete(
                MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(movieId),
                MovieContract.FavoriteMovies.TABLE_NAME + "." +
                        MovieContract.FavoriteMovies.COLUMN_MOVIE_ID + " = ? ",
                new String[]{String.valueOf(movieId)});

        // Updated the notifyChange to update  the content provide for favorite_movie
        if (a != 0) {
            myContext.getContentResolver().notifyChange(MovieContract.FavoriteMovies.CONTENT_URI, null);
        }

        // ContentResolver that deletes the specified review from the favorite_review table
        int b = mContentResolver.delete(
                MovieContract.FavoriteReviewEntry.buildFavoriteReviewMovieIDUri(movieId),
                MovieContract.FavoriteReviewEntry.TABLE_NAME + "." +
                        MovieContract.FavoriteReviewEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{String.valueOf(movieId)});

        // Updated the notifyChange to update  the content provide for favorite_review
        if (b != 0) {
            myContext.getContentResolver().notifyChange(MovieContract.FavoriteReviewEntry.CONTENT_URI, null);
        }

        // ContentResolver that deletes the specified review from the favorite_trailer table
        int c = mContentResolver.delete(
                MovieContract.FavoriteTrailerEntry.buildFavoriteTrailerMovieIDUri(movieId),
                MovieContract.FavoriteTrailerEntry.TABLE_NAME + "." +
                        MovieContract.FavoriteTrailerEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{String.valueOf(movieId)});

        // Updated the notifyChange to update  the content provide for favorite_trailer
        if (c != 0) {
            myContext.getContentResolver().notifyChange(MovieContract.FavoriteTrailerEntry.CONTENT_URI, null);
        }
    }
}
