package com.example.andriod.popularmoviev2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Trailer object used by retrofit library to represent one trailer
 *
 * Created by StandleyEugene on 7/6/2016.
 */
public class Trailer {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     * Gets the unique Trailer id
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *  Sets the unique Trailer id
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the language for Trailer
     * @return
     * The iso6391
     */
    public String getIso6391() {
        return iso6391;
    }

    /**
     * Sets the language for Trailer
     * @param iso6391
     * The iso_639_1
     */
    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    /**
     * Get the Trailer production region
     * @return
     * The iso31661
     */
    public String getIso31661() {
        return iso31661;
    }

    /**
     * Sets the Trailer production region
     * @param iso31661
     * The iso_3166_1
     */
    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    /**
     * Gets the Trailer key
     * @return
     * The key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the Trailer key
     * @param key
     * The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Get the Trailer name
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the Trailer name
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the Trailer holding site
     * @return
     * The site
     */
    public String getSite() {
        return site;
    }

    /**
     * Set the Trailers holding site
     * @param site
     * The site
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * Get the Trailer size
     * @return
     * The size
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Set the Trailer size
     * @param size
     * The size
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * Get the type of Trailer
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of Trailer
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

}