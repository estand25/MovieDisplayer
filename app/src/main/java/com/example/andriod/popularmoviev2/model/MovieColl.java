package com.example.andriod.popularmoviev2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Movie Collection object (list of Movies) used by retrofit library to group reviews
 *
 * Created by StandleyEugene on 7/6/2016.
 */
public class MovieColl {

    @SerializedName("results")
    @Expose
    private List<Movie> Movies = new ArrayList<Movie>();

    /**
     * Get the list of movies
     * @return
     * The results
     */
    public List<Movie> getMovies() {
        return Movies;
    }

    /**
     * Set the List of movies
     * @param Movies
     * The results
     */
    public void setMovies(List<Movie> Movies) {
        this.Movies = Movies;
    }

}
