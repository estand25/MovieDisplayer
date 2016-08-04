package com.example.andriod.popularmoviev2.service;

import com.example.andriod.popularmoviev2.model.Genres;
import com.example.andriod.popularmoviev2.model.MovieColl;
import com.example.andriod.popularmoviev2.model.ReviewColl;
import com.example.andriod.popularmoviev2.model.TrailerColl;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The Movie DB API connect strings interface
 *
 * Created by StandleyEugene on 7/6/2016.
 */
public interface TheMovieDBAPIService {
    // List of the Popular Movie
    @GET("movie/popular")
    Call<MovieColl> getPopularMovie(
            @Query("api_key") String api_key
    );

    // List of the Top Rate Movies
    @GET("movie/top_rated")
    Call<MovieColl> getTopRateMovie(
            @Query("api_key") String api_key
    );

    // List of movie Genres
    @GET("genre/movie/list")
    Call<Genres> getMovieGenres(
            @Query("api_key") String api_key
    );

    // List of Specified movie Trailers
    @GET("movie/{id}/videos")
    Call<TrailerColl> getMovieTrailer(
            @Path("id") Integer id,
            @Query("api_key") String api_key
    );

    // List of Specified movie Reviews
    @GET("movie/{id}/reviews")
    Call<ReviewColl> getMovieReview(
            @Path("id") Integer id,
            @Query("api_key") String api_key
    );
}

