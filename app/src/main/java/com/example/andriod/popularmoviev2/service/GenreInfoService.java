package com.example.andriod.popularmoviev2.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.example.andriod.popularmoviev2.BuildConfig;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.model.Genres;
import com.example.andriod.popularmoviev2.other.Constants;

import java.util.HashMap;

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

        // Get data from the movieIntent
        final String[] movieGenreIDs = movieIntent.getStringArrayExtra(Constants.GENRE_ID);

        // String[] of comma separate genre id
        final String[] genres = getIndividualGenreID(movieGenreIDs[0]);

        // Check if genres has already been replaced with genre name
        // I look around for a little weight solution instead of loop or something else
        // Note: Found this post while I was looking
        // http://stackoverflow.com/questions/14206768/how-to-check-if-a-string-is-numeric
        if(!genres[0].matches("[-+]?\\d*\\.?\\d+")){
            return;
        }

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
                HashMap<Integer,String> movieGenres = response.body().genreMap();

                // Blank the genreName field to start
                String genreName = "";

                // Loop through the movie's genre names
                for(int i = 0; i <= genres.length-1;i++) {
                    // Get all the Genre Name and update Genre id in the Movie
                    if(genreName.equals("")){
                        genreName = movieGenres.get(Integer.parseInt(genres[i]));
                    }else{
                        genreName = genreName + "|" + movieGenres.get(Integer.parseInt(genres[i]));
                    }
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

            @Override
            public void onFailure(Call<Genres> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to get populate genre table!!");
            }
        });
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
