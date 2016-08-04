package com.example.andriod.popularmoviev2.other;

import android.content.Context;

import com.example.andriod.popularmoviev2.data.MovieContract;

/**
 * A constants class used by multiple package
 *
 * Created by StandleyEugene on 7/28/2016.
 */
public final class Constants {
    // Static Context used by LastSelectedMovieType
    public static Context cConetext;

    // Set the movie root
    public static final String movieRoot = "http://api.themoviedb.org/3/";

    // Set-up constant for Genre id information
    public static final String GENRE_ID = "GENRE_DECODING";

    // Set-up constant for Review information
    public static final String REVIEW = "REVIEW";

    // Set-up constant for Trailer Information
    public static final String TRAILER = "TRAILER";

    // String constant for the MovieDetailFragment
    public static final String MOVIE_DETAIL_URI = "URI";

    // String constant for the MovieDetailFragment
    public static final String MOVIE_DETAIL = "MOVIE_DETAIL";

    // Movie Detail Loader for DetailMovieFragment
    public static final int DETAIL_MOVIE_LOADER = 1;

    // Unique identify for they specific load
    public static final int MOVIE_LOADER = 0;

    // Movie String[] for Detail Fragment
    public static final String[] DETAIL_MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_ADULT,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_GENRE_IDS,
            MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_VIDEO,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_MOVIE_TYPE
    };

    // Trailer String[] for Detail Trailer Fragment
    public static final String[] TRAILER_MOVIE_COLUMNS = {
            MovieContract.TrailerEntry.TABLE_NAME + "." + MovieContract.TrailerEntry._ID,
            MovieContract.TrailerEntry.COLUMN_TRAILER_ID,
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.TrailerEntry.COLUMN_ISO_6391,
            MovieContract.TrailerEntry.COLUMN_ISO_31661,
            MovieContract.TrailerEntry.COLUMN_KEY,
            MovieContract.TrailerEntry.COLUMN_NAME,
            MovieContract.TrailerEntry.COLUMN_SITE,
            MovieContract.TrailerEntry.COLUMN_SIZE,
            MovieContract.TrailerEntry.COLUMN_TYPE
    };

    // Review String[] for Detail Review Fragment
    public static final String[] REVIEW_MOVIE_COLUMNS = {
            MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.ReviewEntry._ID,
            MovieContract.ReviewEntry.COLUMN_REVIEW_ID,
            MovieContract.ReviewEntry.COLUMN_MOVIE_ID,
            MovieContract.ReviewEntry.COLUMN_AUTHOR,
            MovieContract.ReviewEntry.COLUMN_CONTENT,
            MovieContract.ReviewEntry.COLUMN_URL
    };

    public static final String SELECTED_KEY = "selected_position";

    // String[] of movie columns
    public static final String[] MOVIE_COLUMNS ={
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_GENRE_IDS,
            MovieContract.MovieEntry.COLUMN_TITLE
    };

}
