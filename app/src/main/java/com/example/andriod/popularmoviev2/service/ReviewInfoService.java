package com.example.andriod.popularmoviev2.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.example.andriod.popularmoviev2.BuildConfig;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.model.Review;
import com.example.andriod.popularmoviev2.model.ReviewColl;
import com.example.andriod.popularmoviev2.other.Constants;
import com.example.andriod.popularmoviev2.other.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * IntentService that handles the retrieve of movie reviews
 *
 * Created by StandleyEugene on 7/28/2016.
 */
public class ReviewInfoService extends IntentService{
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
    public ReviewInfoService(){
        super("ReviewInfoService");
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
     * Populate through contentResolver the review table for the movie
     * @param movieIdIntent - Service intent that passes service necessary information
     */
    @Override
    protected void onHandleIntent(Intent movieIdIntent){
        // Set the current context content Resolver
        mContentResolver.delete(MovieContract.ReviewEntry.CONTENT_URI,"", new String[]{});

        // Get the data from the movieIdIntent
        final int movie_id = movieIdIntent.getIntExtra(Constants.REVIEW,0);

        // Create an instance of the framework that create the Uri and converter the json to gson
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.movieRoot)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Uses the framework services to connect to the API
        TheMovieDBAPIService service = retrofit.create(TheMovieDBAPIService.class);

        // Call the service for movie's review
        Call<ReviewColl> jsonReviewColl = service.getMovieReview(movie_id, BuildConfig.THE_MOVIE_DB_API);

        // Start the connect up and see if it success and you have a response
        // or if you failure and you got nothing
        jsonReviewColl.enqueue(new Callback<ReviewColl>() {
            @Override
            public void onResponse(Call<ReviewColl> call, Response<ReviewColl> response) {
                // Grab the response (the list of movie review) from the API
                // and put it in a local list variable
                List<Review> movieReview = response.body().getReviews();

                // ContentValue Array that I will past to bulk insert
                ContentValues[] bulkMovieReview = new ContentValues[movieReview.size()];

                // Index counter
                int i = 0;

                // Loop through added the individual movie details to the ContentValue
                for(Review review : movieReview){
                    // Content that holds all the popular movie information
                    // retrieved from the Movie DB API
                    ContentValues reviewContent = new ContentValues();

                    // Set the value of each column and insert the review properties
                    reviewContent.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,movie_id);
                    reviewContent.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID,review.getId());
                    reviewContent.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,review.getAuthor());
                    reviewContent.put(MovieContract.ReviewEntry.COLUMN_CONTENT,review.getContent());
                    reviewContent.put(MovieContract.ReviewEntry.COLUMN_URL,review.getUrl());
                    reviewContent.put(MovieContract.ReviewEntry.COLUMN_MOVIE_TYPE, Utility.getPreferredMovieType(getApplicationContext())); //blank if not favorite

                    // Add review detail to the ContentValue array
                    bulkMovieReview[i] = reviewContent;

                    // Increment index
                    i++;
                }

                // Insert the content array to our local DB
                mContentResolver.bulkInsert(MovieContract.ReviewEntry.CONTENT_URI,bulkMovieReview);
            }

            @Override
            public void onFailure(Call<ReviewColl> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to populate movie review table!!");
            }
        });
    }
}
