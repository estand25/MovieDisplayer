package com.example.andriod.popularmoviev2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Genre Collection object (list of movie genres) used by retrofit library to group genre
 *
 * Created by StandleyEugene on 7/6/2016.
 */
public class Genres {

    @SerializedName("genres")
    @Expose
    private List<Genre> genres = new ArrayList<Genre>();

    /**
     *  Returns a List of Genres
     * @return
     * The genres
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /**
     * Set a list of Genres
     * @param genres
     * The genres
     */
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    /**
     * Return a Hashmap (Integer, String) pair for the genre list
     * @return
     */
    public HashMap<Integer,String> genreMap(){
        HashMap<Integer,String> genreList = new HashMap<Integer, String>();

        for(int i = 0; i<genres.size();i++){
            genreList.put(genres.get(i).getId(),genres.get(i).getName());
        }
        return genreList;
    }

}