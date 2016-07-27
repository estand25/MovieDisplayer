package com.example.andriod.popularmoviev2.data;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.example.andriod.popularmoviev2.BuildConfig;
import com.example.andriod.popularmoviev2.activity.DetailMovieFragment;
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

import java.util.HashMap;
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
                    popularMovieContenter.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE,Utility.getPreferredMovieType(getContext())); //blank if not favorite

                    // Add movie details to the ContentValue array
                    bulkPopularMovies[i] = popularMovieContenter;

                    // Increment index
                    i++;
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
                    topRatedMovieContenter.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE,Utility.getPreferredMovieType(getContext())); //blank if not favorite

                    // Add movie details to the ContentValue array
                    bulktopRatedMovies[i] = topRatedMovieContenter;

                    // Increment index
                    i++;
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

    public void getFavoriteMovieColl(){
        // Create cursor with all the current favorite movies
        Cursor cursor = mContentResolver.query(MovieContract.FavoriteMovies.CONTENT_URI,
                null,
                null,
                null,
                null);

        Log.v("Favorite movie count ",String.valueOf(cursor.getCount()));
        Log.v("Favorite Column ",cursor.getColumnName(0));
        Log.v("Favorite Column data ",cursor.getColumnName(1));
        cursor.moveToPosition(0);
        Log.v("Favorite movie Column ",cursor.getColumnName(2).toString());

        // ContentValue Array that I will past to bulkInsert
        ContentValues[] bulkFavoriteMovies = new ContentValues[cursor.getCount()];

        // Set the movie type
        String favor = "FAVOR";

        // Loop through added the individual favorite movie details to the ContentValue
        for(int i = 0;i<= (cursor.getCount()-1);i++){
            Log.v("Favorite Count ",String.valueOf(i));
            Log.v("Favorite Title ",cursor.getString(9));

            // Content that will hold favorite movie information
            // retrieved from the favorite_movie table
            ContentValues favoriteMovieContent = new ContentValues();

            // Set the value of each column and insert the favorite movie for movie property
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, cursor.getString(1));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, cursor.getString(2));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_ADULT, cursor.getString(3));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, cursor.getString(4));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, cursor.getString(5));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS,  cursor.getString(6));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, cursor.getString(7));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, cursor.getString(8));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_TITLE, cursor.getString(9));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, cursor.getString(10));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_POPULARITY, cursor.getString(11));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, cursor.getString(12));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_VIDEO, cursor.getString(13));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, cursor.getString(14));
            favoriteMovieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE, favor); //blank if not favorite

            // Add the favorite movie detail to the ContentValue array
            bulkFavoriteMovies[i] = favoriteMovieContent;

            // Move to the next row in cursor
            cursor.moveToNext();
        }
        cursor.close();
        // Bulk insert all the favorite movies into the movie table to display
        mContentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI,bulkFavoriteMovies);
    }

    /**
     * Update movie & add favorite_movie for the specific movie id for the favorite list
     * @param movieId - The movie id to update movie and add to the favorite_movie table
     */
    public void updateMovieFavorite(int movieId){
        // Content Value that holds the movie type (Blank or Favor)
        // Note: FAVOR movie will appear in movie list
        ContentValues movieTypeContent = new ContentValues();
        movieTypeContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_TYPE,"FAVOR");

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

        Log.v("Favorite row count ",String.valueOf(cursor.getCount()));

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
            updateMovieFavorite(movieId);
        }
    }

    public void deleteFavoriteMovie(int movieId){
        int i = mContentResolver.delete(MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(movieId),
                                                MovieContract.FavoriteMovies.TABLE_NAME + "." + MovieContract.FavoriteMovies.COLUMN_MOVIE_ID + " = ? ",
                                                new String[]{MovieContract.MovieEntry.getMovieID(MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(movieId))});

        if(i != 0){
            getContext().getContentResolver().notifyChange(MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(movieId),null);
        }
    }

    /**
     *  Populated the Genre tables
     */
    public void getGenreInfo(String movieGenres,final int movieId){
        // Remove all the information before populating new data
        deleteAllGenre();

        // String[] of comma separate genre id
        final String[] genres = getIndividualGenreID(movieGenres);

        // Check if genres has already been replaced with genre name
        // I look around for a little weight solution instead of loop or something else
        // Note: Found this post while I was looking
        // http://stackoverflow.com/questions/14206768/how-to-check-if-a-string-is-numeric
        if(!genres[0].matches("[-+]?\\d*\\.?\\d+")){
            return;
        }

        // Create an instance of the framework that creates the Uri and converter the json to gson
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(movieRoot)
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

                // Content Value Array that I will pass to bulk insert genre
                ContentValues[] bulkMovieGenre = new ContentValues[movieGenres.size()];

                // Blank the genreName field to start
                String genreName = "";

                // Loop through the movie's genre names
                for(int i = 0; i <= genres.length-1;i++) {
                    // Get all the Genre Name and update Genre id in the Movie
                    if(genreName == ""){
                        genreName = movieGenres.get(Integer.parseInt(genres[i]));
                    }else{
                        genreName = genreName + "|" + movieGenres.get(Integer.parseInt(genres[i]));
                    }

                    // Content that holds all the genres information
                    // retrieved from the Movie DB API
                    ContentValues genreContent = new ContentValues();

                    // Set the value of each column and inserts the genre property
                    genreContent.put(MovieContract.GenreEntry.COLUMN_GENRE_ID,genres[i]);
                    genreContent.put(MovieContract.GenreEntry.COLUMN_NAME,movieGenres.get(Integer.parseInt(genres[i])));

                    // Add genre details to the contentValue array
                    bulkMovieGenre[i] = genreContent;
                }
                // Content that holds all the genre name information
                // retrieved from the Movie DB API
                ContentValues movieIdNameContent = new ContentValues();
                movieIdNameContent.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS,genreName);

                // Bulk Insert the movie's Genre information
                mContentResolver.bulkInsert(MovieContract.GenreEntry.CONTENT_URI,bulkMovieGenre);

                // Update the Genre ids field in movie from genre id to genre name plus | in-between
                mContentResolver.update(MovieContract.MovieEntry.CONTENT_URI,
                        movieIdNameContent,
                        "movie.movie_id = ?",
                        new String[]{String.valueOf(movieId)});
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

    /**
     * Populate the reivew table
     * @param movie_id - Movie id for the specific reviews
     */
    public void getReviewInfor(final int movie_id){
        // Delete already existing data
        deleteAllReview();

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
                    ContentValues reviewContent = new ContentValues();

                    // Set the value of each column and insert the movie property
                    reviewContent.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,movie_id);
                    reviewContent.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID,review.getId());
                    reviewContent.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,review.getAuthor());
                    reviewContent.put(MovieContract.ReviewEntry.COLUMN_CONTENT,review.getContent());
                    reviewContent.put(MovieContract.ReviewEntry.COLUMN_URL,review.getUrl());

                    // Add review detail to the ContentValue aray
                    bulkMovieReview[i] = reviewContent;

                    // Increment index
                    i++;
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
     * Populate the trailer table
     * @param movie_id - Movie id for the specific trailers
     */
    public void getTrailerInfor(final int movie_id){
        // Deleted already existing data
        deleteAllTrailer();

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
                    ContentValues trailerContent = new ContentValues();

                    // Set the value of each column and insert the movie property
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID,trailer.getId());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID,movie_id);
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_ISO_6391,trailer.getIso6391());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_ISO_31661,trailer.getIso31661());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_KEY,trailer.getKey());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_NAME,trailer.getName());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_SITE,trailer.getSite());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_SIZE,trailer.getSize());
                    trailerContent.put(MovieContract.TrailerEntry.COLUMN_TYPE,trailer.getType());

                    // Add trailer details to the ContentValue array
                    bulkMovieTrailer[i] = trailerContent;

                    // Increment index
                    i++;
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

    /**
     * Delete Review before populate the table
     */
    public void deleteAllReview(){
        mContentResolver.delete(MovieContract.ReviewEntry.CONTENT_URI,"", new String[]{});
    }

    /**
     * Delete Trailer before populate the table
     */
    public void deleteAllTrailer(){
        mContentResolver.delete(MovieContract.TrailerEntry.CONTENT_URI,"", new String[]{});
    }
}
