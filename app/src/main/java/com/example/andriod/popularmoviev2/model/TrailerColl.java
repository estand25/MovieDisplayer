package com.example.andriod.popularmoviev2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StandleyEugene on 7/6/2016.
 */
public class TrailerColl {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> Trailers = new ArrayList<Trailer>();

    /**
     * Get the movie id for this list of trailers
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the movie id for this list of trailers
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the list of trailer for a movie
     * @return
     * The results
     */
    public List<Trailer> getTrailers() {
        return Trailers;
    }

    /**
     *  Sets the list of trailers for a movie
     * @param results
     * The results
     */
    public void setTrailers(List<Trailer> results) {
        this.Trailers = results;
    }

}