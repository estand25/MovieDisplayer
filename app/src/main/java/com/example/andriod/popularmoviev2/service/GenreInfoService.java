package com.example.andriod.popularmoviev2.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.example.andriod.popularmoviev2.BuildConfig;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.model.Genre;
import com.example.andriod.popularmoviev2.model.Genres;
import com.example.andriod.popularmoviev2.other.Constants;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * IntentService that handles the decoding of movie genre id from The Movie API
 *
 * Created by StandleyEugene on 7/28/2016.
 */
public class GenreInfoService extends IntentService{
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
    public GenreInfoService(){
        super("GenreInfoService");
    }

    /**
     *  Populate through contentResolver the movie specific genre (decode from id -> name)
     * @param movieIntent - Service intent that passes service necessary information
     */
    @Override
    protected void onHandleIntent(Intent movieIntent){
        // Set the current context content Resolver
        mContentResolver = getApplicationContext().getContentResolver();

        // Set the current context content Resolver
        mContentResolver.delete(MovieContract.GenreEntry.CONTENT_URI,"",new String[]{});

        // Create an instance of the framework that creates the Uri and converter the json to gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.movieRoot)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Uses the framework services to connect to the API
        TheMovieDBAPIService service = retrofit.create(TheMovieDBAPIService.class);

        // Call the service for movie genre
        Call<Genres> jsonGenres = service.getMovieGenres(BuildConfig.THE_MOVIE_DB_API);

        // Start the connect up and see if it success and you have a response
        // or if you failure and you got nothing
        jsonGenres.enqueue(new Callback<Genres>() {
            @Override
            public void onResponse(Call<Genres> call, Response<Genres> response) {
                // Grab the response (the list of genres) from the API
                // and put it in a local list variable
                List<Genre> movieGenres = response.body().getGenres();

                // Content Value Array that I will pass to bulk insert
                ContentValues[] bulkMovieGenre = new ContentValues[movieGenres.size()];

                // Index counter
                int i = 0;

                // Loop through added the individual genres details to the content
                for(Genre genre : movieGenres){
                    // Content that holds all the genres information
                    // retrieved from the Movie DB API
                    ContentValues genreContent = new ContentValues();

                    // Set the value of each column and inserts the genre property
                    genreContent.put(MovieContract.GenreEntry.COLUMN_GENRE_ID,genre.getId());
                    genreContent.put(MovieContract.GenreEntry.COLUMN_NAME,genre.getName());

                    // Add genre details to the contentValue array
                    bulkMovieGenre[i] = genreContent;

                    // Increment index
                    i++;
                }
                mContentResolver.bulkInsert(MovieContract.GenreEntry.CONTENT_URI,bulkMovieGenre);
            }

            @Override
            public void onFailure(Call<Genres> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to get populate genre table!!");
            }
        });
    }

}
