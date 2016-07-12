package com.example.andriod.popularmoviev2.data;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.example.andriod.popularmoviev2.BuildConfig;
import com.example.andriod.popularmoviev2.model.Movie;
import com.example.andriod.popularmoviev2.model.MovieColl;
import com.example.andriod.popularmoviev2.other.Utility;
import com.example.andriod.popularmoviev2.service.TheMovieDBAPIService;

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

    /*
        Set up the sync adatper
     */
    public MovieSyncUploader(Context context, boolean autoInitialize){
        super(context,autoInitialize);

        mContentResolver = context.getContentResolver();
    }

    /*
        Set up the sync adatper. This form of the constructor
        maintains compatibility with Android 3.0 and later platform
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

        getPopularMovieColl();
    }

    /*
        Populated movie with popular movie data
     */
    public void getPopularMovieColl(){
        // Create an instance of the framework that creates the Url and converter the json to gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(movieRoot)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Uses the framework services to connect to the API
        TheMovieDBAPIService service = retrofit.create(TheMovieDBAPIService.class);

        // Call the service for popular movie
        Call<MovieColl> jsonMovieColl = service.getPopularMovie(BuildConfig.THE_MOVIE_DB_API);

        // Start the connect up and see if it successed and you have a response
        // or if you failure and you got nothing
        jsonMovieColl.enqueue(new Callback<MovieColl>() {
            @Override
            public void onResponse(Call<MovieColl> call, Response<MovieColl> response) {
                // Grab the response (the List of movies) from the API
                // and put it in a local list variable
                List<Movie> popularMovies = response.body().getMovies();

                // ContentValue Array that I will past to bulkinsert
                ContentValues[] bulkPopularMovies = new ContentValues[popularMovies.size()];

                // Index counter
                int i = 0;

                // Loop through added the individual movie details to the ContentValue
                for(Movie movie : popularMovies){

                    // Content that holds all the popular movie information
                    // retrieved from the Movie DB API
                    ContentValues popularMovieContenter = new ContentValues();

                    // Set the value of each column and inserts the movie property
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_ADULT, movie.getAdult());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS, movie.getGenreIds().toString());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalLanguage());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_POPULARITY,movie.getPopularity());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_VIDEO,movie.getVideo());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,movie.getVoteAverage());
                        popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE,Utility.getPreferredMovieType(getContext())); //short handle for popular movie

                    Log.v("Popular Movie"," "+(i+1)+") Details - " + movie.getTitle());

                    // Add movie details to the ContentValue array
                    bulkPopularMovies[i] = popularMovieContenter;

                    // Increment index
                    i++;
                }
                mContentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI,bulkPopularMovies);
            }

            @Override
            public void onFailure(Call<MovieColl> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to get popular Movie!!");
            }
        });
    }

    /*
        Populated movie with top rated
     */
    public void getTopRateMovieColl(){
        // Create an instance of the framework that creates the Url and converter the json to gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(movieRoot)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Uses the framework services to connect to the API
        TheMovieDBAPIService service = retrofit.create(TheMovieDBAPIService.class);

        // Call the service for popular movie
        Call<MovieColl> jsonMovieColl = service.getTopRateMovie(BuildConfig.THE_MOVIE_DB_API);

        // Start the connect up and see if it successed and you have a response
        // or if you failure and you got nothing
        jsonMovieColl.enqueue(new Callback<MovieColl>() {
            @Override
            public void onResponse(Call<MovieColl> call, Response<MovieColl> response) {
                // Grab the response (the List of movies) from the API
                // and put it in a local list variable
                List<Movie> topRatedMovies = response.body().getMovies();

                // ContentValue Array that I will past to bulkinsert
                ContentValues[] bulktopRatedMovies = new ContentValues[topRatedMovies.size()];

                // Index counter
                int i = 0;

                // Loop through added the individual movie details to the ContentValue
                for(Movie movie : topRatedMovies){

                    // Content that holds all the popular movie information
                    // retrieved from the Movie DB API
                    ContentValues topRatedMovieContenter = new ContentValues();

                    // Set the value of each column and inserts the movie property
                    //topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_ADULT, movie.getAdult());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS, movie.getGenreIds().toString());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalLanguage());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_POPULARITY,movie.getPopularity());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_VIDEO,movie.getVideo());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,movie.getVoteAverage());
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE,"TR"); //short handle for Top Rated Movie

                    Log.v("Top Rated Movie"," "+(i+1)+") Details - " + movie.getTitle());

                    // Add movie details to the ContentValue array
                    bulktopRatedMovies[i] = topRatedMovieContenter;

                    // Increment index
                    i++;
                }
                //mContentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI,bulktopRatedMovies);
            }

            @Override
            public void onFailure(Call<MovieColl> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to get Top Rate Movie!!");
            }
        });
    }
}
