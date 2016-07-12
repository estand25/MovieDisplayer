package com.example.andriod.popularmoviev2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie implements Parcelable {

    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Movie() {
    }

    /**
     *
     * @param id
     * @param genreIds
     * @param title
     * @param releaseDate
     * @param overview
     * @param posterPath
     * @param originalTitle
     * @param voteAverage
     * @param originalLanguage
     * @param adult
     * @param backdropPath
     * @param voteCount
     * @param video
     * @param popularity
     */
    public Movie(String posterPath, Boolean adult, String overview, String releaseDate, List<Integer> genreIds,
                 Integer id, String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity,
                 Integer voteCount, Boolean video, Double voteAverage) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }

    // had to look this up online for the boolean field
    // found a soluation on StackOverflow.com
    // http://stackoverflow.com/questions/6201311/how-to-read-write-a-boolean-when-implementing-the-parcelable-interface
    public Movie(Parcel in){
        posterPath = in.readString();
        adult = in.readByte() !=0;
        overview = in.readString();
        releaseDate = in.readString();
        genreIds = (List<Integer>) in.readSerializable();
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteCount = in.readInt();
        video = in.readByte() !=0;
        voteAverage = in.readDouble();
    }

    @Override
    public int describeContents() {return 0;}

    // had to look this up online for the boolean field
    // found a soluation on StackOverflow.com
    // http://stackoverflow.com/questions/6201311/how-to-read-write-a-boolean-when-implementing-the-parcelable-interface
    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(posterPath);
        parcel.writeByte((byte) (adult ? 1:0));
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeSerializable((Serializable) getGenreIds());
        parcel.writeInt(id);
        parcel.writeString(originalTitle);
        parcel.writeString(originalLanguage);
        parcel.writeString(title);
        parcel.writeString(backdropPath);
        parcel.writeDouble(popularity);
        parcel.writeInt(voteCount);
        parcel.writeByte((byte) (video? 1:0));
        parcel.writeDouble(voteAverage);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Movie createFromParcel(Parcel parcel){
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i){
            return new Movie[i];
        }
    };

    /**
     * Get the Post path for the movie
     * @return
     * The posterPath
     */
    public String getPosterPath() {
        return "http://image.tmdb.org/t/p/w185/"+posterPath;
    }

    /**
     * Sets the post path for the movie
     * @param posterPath
     * The poster_path
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = "http://image.tmdb.org/t/p/w185/"+posterPath;
    }

    /*
        Set the poster path plus the pasted in poster path
     */
    public Movie withPosterPath(String posterPath) {
        this.posterPath = "http://image.tmdb.org/t/p/w185/"+posterPath;
        return this;
    }

    /**
     * Get the boolean value for the adult setting
     * @return
     * The adult
     */
    public Boolean getAdult() {
        return adult;
    }

    /**
     * Set the boolean value for the adult setting
     * @param adult
     * The adult
     */
    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    /*
        Set the boolean value with the addition of the new boolean
     */
    public Movie withAdult(Boolean adult) {
        this.adult = adult;
        return this;
    }

    /**
     * Get the overview
     * @return
     * The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Set the overview
     * @param overview
     * The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /*
        Set the overview value plus the past in value
     */
    public Movie withOverview(String overview) {
        this.overview = overview;
        return this;
    }

    /**
     * Get the release date for the movie
     * @return
     * The releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Set the release date for the movie
     * @param releaseDate
     * The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /*
        Set the release date for the movie plus new release date
     */
    public Movie withReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    /**
     * Get the list of Integer that represent the genre id
     * @return
     * The genreIds
     */
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    /**
     * Set the list of Integer that represent the genre id
     * @param genreIds
     * The genre_ids
     */
    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    /*
        Set the lsit of Integer that represent the genre id plus new genre
     */
    public Movie withGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
        return this;
    }

    /**
     * Get the movie id
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the movie id
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /*
        Set the movie id plus new movie id
     */
    public Movie withId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Get the original movie title
     * @return
     * The originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * Set the original movie title
     * @param originalTitle
     * The original_title
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /*
        Set the original movie title plus new title
     */
    public Movie withOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    /**
     * Get the original movie language
     * @return
     * The originalLanguage
     */
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * Set the original movie language
     * @param originalLanguage
     * The original_language
     */
    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    /*
        Get the original movie language plus new language
     */
    public Movie withOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    /**
     * Get the movie title
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the movie title
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /*
        Set the movie title plue new title
     */
    public Movie withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Get the movie backdrop path
     * @return
     * The backdropPath
     */
    public String getBackdropPath() {
        return "http://image.tmdb.org/t/p/w185/"+backdropPath;
    }

    /**
     * Set the movie backdrop path
     * @param backdropPath
     * The backdrop_path
     */
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = "http://image.tmdb.org/t/p/w185/"+backdropPath;
    }

    /*
        Set the movie backdrop path plus new backdrop
     */
    public Movie withBackdropPath(String backdropPath) {
        this.backdropPath = "http://image.tmdb.org/t/p/w185/"+backdropPath;
        return this;
    }

    /**
     * Get the popularity for the movie
     * @return
     * The popularity
     */
    public Double getPopularity() {
        return popularity;
    }

    /**
     * Set the popularity for the movie
     * @param popularity
     * The popularity
     */
    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    /*
        Set the popularity for the movie plus new popularity
     */
    public Movie withPopularity(Double popularity) {
        this.popularity = popularity;
        return this;
    }

    /**
     * Get the movie vote count
     * @return
     * The voteCount
     */
    public Integer getVoteCount() {
        return voteCount;
    }

    /**
     * Set the movie vote count
     * @param voteCount
     * The vote_count
     */
    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    /*
        Set the movie vote count plus new vote count
     */
    public Movie withVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    /**
     * Get the video boolean value
     * @return
     * The video
     */
    public Boolean getVideo() {
        return video;
    }

    /**
     * Set the video boolean value
     * @param video
     * The video
     */
    public void setVideo(Boolean video) {
        this.video = video;
    }

    /*
        Set the video boolean value plus new video
     */
    public Movie withVideo(Boolean video) {
        this.video = video;
        return this;
    }

    /**
     * Get the movie vote average
     * @return
     * The voteAverage
     */
    public Double getVoteAverage() {
        return voteAverage;
    }

    /**
     * Set the movie vote average
     * @param voteAverage
     * The vote_average
     */
    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    /*
        Set the movie vote average plus new vote average
     */
    public Movie withVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Movie withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}