package com.example.andriod.popularmoviev2.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.example.andriod.popularmoviev2.BuildConfig;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.model.Movie;
import com.example.andriod.popularmoviev2.model.MovieColl;
import com.example.andriod.popularmoviev2.other.Constants;
import com.example.andriod.popularmoviev2.other.Utility;
import com.example.andriod.popularmoviev2.service.TheMovieDBAPIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * IntentService that handles the retrieve of populate movie information for The Movie DB API
 *
 * Created by StandleyEugene on 7/28/2016.
 */
public class PopularMovieService extends IntentService{
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
    public PopularMovieService(){
        super("PopulateMovieService");
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
     * Populate SQLiteDatabase through contentResolver with the Movie DB API for popular movies
     * @param pMovieIntent - Service intent that passes service necessary information
     */
    @Override
    protected void onHandleIntent(Intent pMovieIntent){
        // Delete all the other tables associated to the correct GridView display
        mContentResolver.delete(
                MovieContract.MovieEntry.CONTENT_URI,"movie.movie_type = ?",
                new String[]{"movie/popular"});

        // Create an instance of the framework that creates the Url and converter the json to gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.movieRoot)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Uses the framework services to connect to the API
        TheMovieDBAPIService service = retrofit.create(TheMovieDBAPIService.class);

        // Call the service for popular movie
        Call<MovieColl> jsonMovieColl = service.getPopularMovie(BuildConfig.THE_MOVIE_DB_API);

        // Start the connect up and see if it success and you have a response
        // or if you failure and you got nothing
        jsonMovieColl.enqueue(new Callback<MovieColl>() {
            @Override
            public void onResponse(Call<MovieColl> call, Response<MovieColl> response) {
                // Grab the response (the List of movies) from the API
                // and put it in a local list variable
                List<Movie> popularMovies = response.body().getMovies();

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
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_VIDEO, movie.getVideo());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE, "movie/popular"); //blank if not favorite

                    // Insert single movie records into SQLiteDatabase using contentResolver
                    mContentResolver.insert(MovieContract.MovieEntry.CONTENT_URI,popularMovieContenter);

                    // Changes Genre id -> Genre Name by passing genre id list and movie id
                    // right after inserting the movie record
                    startService(new Intent(getApplicationContext(), GenreDecodeService.class).
                            putExtra(Constants.GENRE_ID, new String[] {movie.getGenreIds().toString(),movie.getId().toString()}));
                }
            }

            @Override
            public void onFailure(Call<MovieColl> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to populate movie table!!");
            }
        });
    }
}
