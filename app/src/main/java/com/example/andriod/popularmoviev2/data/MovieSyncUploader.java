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

        // Populate the movie table with either Popular or Top Rate Movie
        if(Utility.getPreferredMovieType(getContext()).equals("movie/popular")) {
            getPopularMovieColl();
        }else{
            getTopRateMovieColl();
        }
    }

    /**
     * Populated movie with popular movie data
     */
    public void getPopularMovieColl(){
        // Remove all the information before populating new data
        deleteAllOtherTable();

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

                    // Add movie details to the ContentValue array
                    bulkPopularMovies[i] = popularMovieContenter;

                    // Increment index
                    i++;

                    //Log.v("Movie Name ",movie.getTitle());
                }
                // Bulk insert all the new content information
                mContentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI,bulkPopularMovies);
            }

            @Override
            public void onFailure(Call<MovieColl> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to populate movie table!!");
            }
        });
    }

    /**
     * Populated movie with top rated movie
     */
    public void getTopRateMovieColl(){
        // Remove all the information before populating new data
        deleteAllOtherTable();

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
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
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
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE,Utility.getPreferredMovieType(getContext())); //short handle for Top Rated Movie

                    // Add movie details to the ContentValue array
                    bulktopRatedMovies[i] = topRatedMovieContenter;

                    // Increment index
                    i++;

                    //Log.v("Movie Name ",movie.getTitle());
                }
                // Bulk insert all the new content information
                mContentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI,bulktopRatedMovies);
            }

            @Override
            public void onFailure(Call<MovieColl> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to populate movie table!!");
            }
        });
    }

    /**
     *  Populated the Genre tables
     */
    public void getGenreInfo(){
        // Remove all the information before populating new data
        deleteAllGenre();

        // Create an instance of the framework that creates the Uri and converter the json to gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(movieRoot)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Uses the framework services to connect to the API
        TheMovieDBAPIService service = retrofit.create(TheMovieDBAPIService.class);

        // Call the service for movie genre
        Call<Genres> jsonGenres = service.getMovieGenres(BuildConfig.THE_MOVIE_DB_API);

        // Start the connect up and see if it successed and you have a response
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

                    Log.v("Genre Name ",genre.getName());
                }
                mContentResolver.bulkInsert(MovieContract.GenreEntry.CONTENT_URI,bulkMovieGenre);
            }

            @Override
            public void onFailure(Call<Genres> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to get populate genre table!!");
            }
        });
    }

    /**
     * Populate the trailer table
     * @param movie_id - Movie id for the specific trailers
     */
    public void getTrailerInfor(final int movie_id){
        // Create an instance of the framework that created the Uri and converter the json to gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(movieRoot)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Uses the framework services to connect to the API
        TheMovieDBAPIService service = retrofit.create(TheMovieDBAPIService.class);

        // Call the service for movie's trailer
        Call<TrailerColl> jsonTrailerColl = service.getMovieTrailer(movie_id,BuildConfig.THE_MOVIE_DB_API);

        // Start the connect up and see if it successed and you have a response
        // or if you failure and you got nothing
        jsonTrailerColl.enqueue(new Callback<TrailerColl>() {
            @Override
            public void onResponse(Call<TrailerColl> call, Response<TrailerColl> response) {
                // Grab the response (the list of movie trailer) from the API
                // and put it in a local list variable
                List<Trailer> movieTrailer = response.body().getTrailers();

                // ContentValue Array that I will past to bulkinsert
                ContentValues[] bulkMovieTrailer = new ContentValues[movieTrailer.size()];

                // Index counter
                int i = 0;

                // Loop through added the individual movie details to the ContentValue
                for(Trailer trailer: movieTrailer){
                     // Content that holds all the popular movie information
                     // retrieved from the Movie DB API
                    ContentValues trailerContanter = new ContentValues();

                    // Set the value of each column and insert the movie property
                    trailerContanter.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID,trailer.getId());
                    trailerContanter.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID,movie_id);
                    trailerContanter.put(MovieContract.TrailerEntry.COLUMN_ISO_6391,trailer.getIso6391());
                    trailerContanter.put(MovieContract.TrailerEntry.COLUMN_ISO_31661,trailer.getIso31661());
                    trailerContanter.put(MovieContract.TrailerEntry.COLUMN_KEY,trailer.getKey());
                    trailerContanter.put(MovieContract.TrailerEntry.COLUMN_NAME,trailer.getName());
                    trailerContanter.put(MovieContract.TrailerEntry.COLUMN_SITE,trailer.getSite());
                    trailerContanter.put(MovieContract.TrailerEntry.COLUMN_SIZE,trailer.getSize());
                    trailerContanter.put(MovieContract.TrailerEntry.COLUMN_TYPE,trailer.getType());

                    // Add trailer details to the ContentValue array
                    bulkMovieTrailer[i] = trailerContanter;

                    // Increment index
                    i++;

                    Log.v("Trailer Name ",trailer.getName());
                }
                mContentResolver.bulkInsert(MovieContract.TrailerEntry.CONTENT_URI,bulkMovieTrailer);
            }

            @Override
            public void onFailure(Call<TrailerColl> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to populate movie trailer table!!");
            }
        });
    }

    /**
     * Populate the reivew table
     * @param movie_id - Movie id for the specific reviews
     */
    public void getReviewInfor(final int movie_id){
        // Create an instance of the framework that create the Uri and converter the json to gson
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(movieRoot)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Uses the framework services to connect to the API
        TheMovieDBAPIService service = retrofit.create(TheMovieDBAPIService.class);

        // Call the service for movie's review
        Call<ReviewColl> jsonReviewColl = service.getMovieReview(movie_id,BuildConfig.THE_MOVIE_DB_API);

        // Start the connect up and see if it successed and you have a response
        // or if you failure and you got nothing
        jsonReviewColl.enqueue(new Callback<ReviewColl>() {
            @Override
            public void onResponse(Call<ReviewColl> call, Response<ReviewColl> response) {
                // Grab the response (the list of movie review) from the API
                // and put it in a local list variable
                List<Review> movieReview = response.body().getReviews();

                // ContentValue Array that I will past to bulkinsert
                ContentValues[] bulkMovieReview = new ContentValues[movieReview.size()];

                // Index counter
                int i = 0;

                // Loop through added the individual movie details to the ContentValue
                for(Review review : movieReview){
                    // Content that holds all the populated movie information
                    // retrieved from the Movie DB API
                    ContentValues reviewContanter = new ContentValues();

                    // Set the value of each column and insert the movie property
                    reviewContanter.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,movie_id);
                    reviewContanter.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID,review.getId());
                    reviewContanter.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,review.getAuthor());
                    reviewContanter.put(MovieContract.ReviewEntry.COLUMN_CONTENT,review.getContent());
                    reviewContanter.put(MovieContract.ReviewEntry.COLUMN_URL,review.getUrl());

                    // Add review detail to the ContentValue aray
                    bulkMovieReview[i] = reviewContanter;

                    // Increment index
                    i++;

                    Log.v("Review Name ",review.getAuthor());
                }
                mContentResolver.bulkInsert(MovieContract.ReviewEntry.CONTENT_URI,bulkMovieReview);
            }

            @Override
            public void onFailure(Call<ReviewColl> call, Throwable t) {
                Log.e(TAG,"Retrofit 2 failed to populate movie review table!!");
            }
        });
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
     * Delete Genre before populate the table
     */
    public void deleteAllGenre(){
        mContentResolver.delete(MovieContract.GenreEntry.CONTENT_URI,"",new String[]{});
    }
}
