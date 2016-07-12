package com.example.andriod.popularmoviev2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by StandleyEugene on 7/6/2016.
 */
public class Review {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * Get the unique Review id
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the unique Review id
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the review author
     * @return
     * The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the review author
     * @param author
     * The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get the review content
     * @return
     * The content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the review content
     * @param content
     * The content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the Url for the review
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set the Url for the review
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}