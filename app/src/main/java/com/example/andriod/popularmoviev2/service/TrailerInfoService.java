package com.example.andriod.popularmoviev2.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.example.andriod.popularmoviev2.BuildConfig;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.model.Trailer;
import com.example.andriod.popularmoviev2.model.TrailerColl;
import com.example.andriod.popularmoviev2.other.Constants;
import com.example.andriod.popularmoviev2.other.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * IntentService that handles the retrieval of the movie's trailer
 *
 * Created by StandleyEugene on 7/29/2016.
 */
public class TrailerInfoService extends IntentService{
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
    public TrailerInfoService(){
        super("TrailerInfoService");
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
     *  Populate through contentResolver the trailer table for the movie
     * @param movieIntent - Service intent that passes service necessary information
     */
    @Override
    protected void onHandleIntent(Intent movieIntent){
        // Set the current context content Resolver
        mContentResolver.delete(MovieContract.TrailerEntry.CONTENT_URI,"",new String[]{});

        // Get the data from the movieIdIntent
        final int movie_id = movieIntent.getIntExtra(Constants.TRAILER,0);

        // Create an instance of the framework that created the Uri and converter the json to gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.movieRoot)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Uses the framework services to connect to the API
        TheMovieDBAPIService service = retrofit.create(TheMovieDBAPIService.class);

        // Call the service for movie's trailer
        Call<TrailerColl> jsonTrailerColl = service.getMovieTrailer(movie_id, BuildConfig.THE_MOVIE_DB_API);

        // Start the connect up and see if it success and you have a response
        // or if you failure and you got nothing
        jsonTrailerColl.enqueue(new Callback<TrailerColl>() {
            @Override
            public void onResponse(Call<TrailerColl> call, Response<TrailerColl> response) {
                // Grab the response (the list of movie trailer) from the API
                // and put it in a local list variable
                List<Trailer> movieTrailer = response.body().getTrailers();

                // ContentValue Array that I will past to bulk insert
                ContentValues[] bulkMovieTrailer = new ContentValues[movieTrailer.size()];

                // Index counter
                int i = 0;

                // Loop through added the individual movie details to the ContentValue
                for(Trailer trailer: movieTrailer){
                    // Content that holds all the popular movie information
                    // retrieved from the Movie DB API
                    ContentValues trailerContent = new ContentValues();

                    // Set the value of each column and insert the trailer properties
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID,trailer.getId());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID,movie_id);
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_ISO_6391,trailer.getIso6391());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_ISO_31661,trailer.getIso31661());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_KEY,trailer.getKey());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_NAME,trailer.getName());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_SITE,trailer.getSite());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_SIZE,trailer.getSize());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_TYPE,trailer.getType());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_MOVIE_TYPE, Utility.getPreferredMovieType(getApplicationContext())); //blank if not favorite

                    // Add trailer details to the ContentValue array
                    bulkMovieTrailer[i] = trailerContent;

                    // Increment index
                    i++;
                }

                // Insert the content array to our local DB
                mContentResolver.bulkInsert(MovieContract.TrailerEntry.CONTENT_URI,bulkMovieTrailer);
            }

            @Override
            public void onFailure(Call<TrailerColl> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to populate movie trailer table!!");
            }
        });
    }
}
