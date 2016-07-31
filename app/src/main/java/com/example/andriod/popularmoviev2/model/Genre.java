package com.example.andriod.popularmoviev2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Movie object used by retrofit library to represent one movie
 *
 * Created by StandleyEugene on 7/6/2016.
 */
public class Genre {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * Returns the genre id in integer
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the Genre id
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *  Get the Genre Name
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the Genre name
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getNameFromId(Integer id){
        return name;
    }
}
