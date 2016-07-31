package com.example.andriod.popularmoviev2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Review Collection object (list of Reviews) used by retrofit library to group reviews
 *
 * Created by StandleyEugene on 7/6/2016.
 */
public class ReviewColl {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<Review> Reviews = new ArrayList<Review>();

    /**
     * Get the id for the movie assoicated to the review
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the id for the movie associated to the review
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the current page of the review
     * @return
     * The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Set the current page of the review
     * @param page
     * The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * Get the list of reviews for a specific movie
     * @return
     * The results
     */
    public List<Review> getReviews() {
        return Reviews;
    }

    /**
     * Set the list of review for a specific movie
     * @param Reviews
     * The results
     */
    public void setReviews(List<Review> Reviews) {
        this.Reviews = Reviews;
    }

}
