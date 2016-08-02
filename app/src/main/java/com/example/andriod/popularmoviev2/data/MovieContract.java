package com.example.andriod.popularmoviev2.data;

/**
 * Note: Base on SunShine App
 * MovieViewerContract class that handles the individual table
 * defainces
 * Created by StandleyEugene on 7/10/2016.
 */
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    // The base location where the tables will be location
    public static final String CONTENT_AUTHORITY = "com.example.andriod.popularmoviev2";

    // URI representation of the root table locations
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Individiaul table names
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_GENRE = "genre";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

    // Class that creates the Movie (MovieEntry) table
    public static final class MovieEntry implements BaseColumns{
        // Location for the specific table (used to acces table data)
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        // The type of data I will be send if retrieved
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieDetailAllSection(int MovieId){
            return BASE_CONTENT_URI.buildUpon().appendPath("movie_detail")
                    .appendPath(Integer.toString(MovieId)).build();
        }

        // URI for the specific Movie row in the movie table
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        // Uri for the specific Movie ID in the movie table
        public static Uri buildMovieIDUri(int MovieID){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(MovieID)).build();
        }

        // Uri for getting the movie id
        public static String getMovieID(Uri uri){
            return uri.getPathSegments().get(1);
        }

        // Uri for getting the movie id
        public static int getIntegerMovieID(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

        // String holds the table name
        public static final String TABLE_NAME = "movie";

        // Columns in the movie DB
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_GENRE_IDS = "genre_ids";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_TYPE = "movie_type";
    }

    // Class that creates the Review (ReviewEntry) table
    public static final class ReviewEntry implements BaseColumns{
        // Location for the specific table (used to access table data)
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        // The type of data I will be sending if retrieved
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        // Uri for the specific Review row in the review table
        public static Uri buildReviewUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        // Uri all review for a Movie in the movie table
        public static Uri buildReviewMovieIDUri(int MovieID){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(MovieID)).build();
        }

        // String holding the table name
        public static final String TABLE_NAME = "review";

        // Colums in the Review DB
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_MOVIE_TYPE = "movie_type";
    }

    // Class that creates the Genre (GenreEntry) table
    public static final class GenreEntry implements BaseColumns {

        // Location for the specific table (used to access table data)
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE).build();

        // The type of data I will be sending if retrieved
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRE;

        // Uri for the specific Genre row in the genre table
        public static Uri buildGenreUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        // Uri all review for a Movie in the movie table
        public static Uri buildGenreMovieIDUri(int GenreID){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(GenreID)).build();
        }

        // Uri for getting the genre id
        public static String getGenreID(Uri uri){
            return uri.getPathSegments().get(1);
        }


        // String holding the table name
        public static final String TABLE_NAME ="genre";

        // Columns in the Genre DB
        public static final String COLUMN_GENRE_ID = "genre_id";
        public static final String COLUMN_NAME = "name";

    }

    // Class that creates the Trailer (TrailerEntry) table
    public static final class TrailerEntry implements BaseColumns{
        // Location for the specific table (used to access table data)
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        // The type of data I will be sending if retrieved
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_TRAILER;

        // Uri for the specific Trailer row in the trailer Table
        public static Uri buildTrailerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        // Uri all trailer for a Movie in the movie table
        public static Uri buildTrailerMovieIDUri(int MovieID){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(MovieID)).build();
        }

        // String holding the table name
        public static final String TABLE_NAME = "trailer";

        // Columns in the Trailer DB
        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ISO_6391 = "iso_6391";
        public static final String COLUMN_ISO_31661 = "iso_3166_1";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_MOVIE_TYPE = "movie_type";
    }

    // Class that creates the Favorite Movie (FavoriteMovies) table
    public static final class FavoriteMovies implements BaseColumns{
        // Location for the specific table (used to access table data)
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        // The type of data I will be sending if retrieved
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIES;

        // Uri for the specific favorite movie row in the favorite movie table
        public static Uri buildFavoriteMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        // Uri for the specific Movie ID in the movie table
        public static Uri buildFavoriteMovieIDUri(int MovieID){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(MovieID)).build();
        }

        // Uri for getting the movie id
        public static String getFavoriteMovieID(Uri uri){
            return uri.getPathSegments().get(1);
        }

        // String hold the table name
        public static final String TABLE_NAME = "favorite_movies";

        // Columns in the favoriteMovies DB
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_GENRE_IDS = "genre_ids";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    }
}