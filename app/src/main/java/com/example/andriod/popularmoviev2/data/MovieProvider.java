package com.example.andriod.popularmoviev2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import com.example.andriod.popularmoviev2.other.Constants;

/**
 * Note: Base on SunShine App
 * MovieViewerProvider handle the SQLite query create (insertion, deletion, and updates)
 *
 * Created by StandleyEugene on 7/10/2016.
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_MOVIE_ID = 101;
    static final int GENRE = 102;
    static final int REVIEW = 103;
    static final int TRAILER = 104;
    static final int GENRE_FOR_MOVIE = 105;
    static final int REVIEW_FOR_MOVIE = 106;
    static final int TRAILER_FOR_MOVIE = 107;
    static final int FAVORITE_MOVIES = 108;
    static final int FAVORITE_MOVIES_ID = 109;
    static final int MOVIE_DETAILS_NORMAL = 110;
    static final int MOVIE_DETAILS_FAVORITE = 111;
    static final int FAVORITE_REVIEW = 112;
    static final int FAVORITE_REVIEW_ID = 113;
    static final int FAVORITE_TRAILER = 114;
    static final int FAVORITE_TRAILER_ID = 115;

    private static final SQLiteQueryBuilder sMovieWithReview;
    private static final SQLiteQueryBuilder sMovieWithTrailer;
    private static final SQLiteQueryBuilder sFavoriteMovie;
    private static final SQLiteQueryBuilder sFavoriteReviewMovie;
    private static final SQLiteQueryBuilder sFavoriteTrailerMovie;

    static{
        sMovieWithReview = new SQLiteQueryBuilder();
        sMovieWithTrailer = new SQLiteQueryBuilder();
        sFavoriteMovie = new SQLiteQueryBuilder();
        sFavoriteReviewMovie = new SQLiteQueryBuilder();
        sFavoriteTrailerMovie = new SQLiteQueryBuilder();

        // This is an inner join which looks at
        // review INNER JOIN movie on movie.movie_id = review.movie_id
        sMovieWithReview.setTables(
                MovieContract.ReviewEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.ReviewEntry.TABLE_NAME +
                        "." + MovieContract.ReviewEntry.COLUMN_MOVIE_ID
        );

        // This is an inner join which looks at
        // trailer INNER JOIN movie on movie.movie_id = trailer.movie_id
        sMovieWithTrailer.setTables(
                MovieContract.TrailerEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.TrailerEntry.TABLE_NAME +
                        "." + MovieContract.TrailerEntry.COLUMN_MOVIE_ID
        );

        // This is an inner join which looks a
        // favorite_movie INNER JOIN movie on movie.movie_id = favorite_movie.movie_id
        sFavoriteMovie.setTables(
                MovieContract.FavoriteMovies.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.FavoriteMovies.TABLE_NAME +
                        "." + MovieContract.FavoriteMovies.COLUMN_MOVIE_ID
        );

        // This is an inner join which looks at
        // favorite_review INNER JOIN movie on favorite_movie.movie_id = favorite_review.movie_id
        sFavoriteReviewMovie.setTables(
                MovieContract.FavoriteReviewEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.FavoriteMovies.TABLE_NAME +
                        " ON " + MovieContract.FavoriteMovies.TABLE_NAME +
                        "." + MovieContract.FavoriteMovies.COLUMN_MOVIE_ID +
                        " = " + MovieContract.FavoriteReviewEntry.TABLE_NAME +
                        "." + MovieContract.FavoriteReviewEntry.COLUMN_MOVIE_ID
        );

        // This is an inner join which looks at
        // favorite_trailer INNER JOIN movie on favorite_movie.movie_id = favorite_trailer.movie_id
        sFavoriteTrailerMovie.setTables(
                MovieContract.FavoriteTrailerEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.FavoriteMovies.TABLE_NAME +
                        " ON " + MovieContract.FavoriteMovies.TABLE_NAME +
                        "." + MovieContract.FavoriteMovies.COLUMN_MOVIE_ID +
                        " = " + MovieContract.FavoriteTrailerEntry.TABLE_NAME +
                        "." + MovieContract.FavoriteTrailerEntry.COLUMN_MOVIE_ID
        );
    }

    // movie.movie_id = ?
    public static final String sMovieIdSettingSelection =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";

    // genre.genre_id = ?
    public static final String sGenreIdSettingSelection =
            MovieContract.GenreEntry.TABLE_NAME +
                    "." + MovieContract.GenreEntry.COLUMN_GENRE_ID + " = ?";

    // favorite_movie.movie_id = ?
    public static final String sFavoriteSettingSelection =
            MovieContract.FavoriteMovies.TABLE_NAME +
                    "." + MovieContract.FavoriteMovies.COLUMN_MOVIE_ID + " = ? ";

    /**
     * Get a Cursor using SQLiteQueryBuilder for a join between Review and movie
     * @param uri - The Uri location for Review
     * @return - Returns a Cursor with review trailer
     */
    private Cursor getMovieReview(Uri uri){
        return sMovieWithReview.query(
                mOpenHelper.getReadableDatabase(),
                new String[]{"review._ID,review_id","movie.movie_id","author","content","url"},
                sMovieIdSettingSelection,
                new String[]{MovieContract.MovieEntry.getMovieID(uri)},
                null,
                null,
                null);
    }

    /**
     * Get a Cursor using SQLiteQueryBuilder for a join between Trailer and movie
     * @param uri - The Uri location for Trailer
     * @return - Returns a Cursor with movie trailer
     */
    private Cursor getMovieTrailer(Uri uri){
        return sMovieWithTrailer.query(
                mOpenHelper.getReadableDatabase(),
                new String[]{"trailer._ID","trailer_id","movie.movie_id","movie.title","iso_6391","iso_3166_1","key","name","site","size","type"},
                sMovieIdSettingSelection,
                new String[]{MovieContract.MovieEntry.getMovieID(uri)},
                null,
                null,
                null);
    }

    /**
     * Get a Cursor using SQLiteQueryBuilder for a join between Favorite and movie
     * @param uri - The Uri location for Favorite
     * @return - Returns a Cursor with favorite movie
     */
    private Cursor getFavoriteMovie(Uri uri){
        return sFavoriteMovie.query(
                mOpenHelper.getReadableDatabase(),
                null,
                sFavoriteSettingSelection,
                new String[]{MovieContract.MovieEntry.getMovieID(uri)},
                null,
                null,
                null);
    }

    /**
     * Get a Cursor using SQLiteQueryBuilder for a join between Review and movie
     * @param uri - The Uri location for Review
     * @return - Returns a Cursor with review trailer
     */
    private Cursor getFavoriteMovieReview(Uri uri){
        return sFavoriteReviewMovie.query(
                mOpenHelper.getReadableDatabase(),
                new String[]{"favorite_review._ID,review_id","favorite_movies.movie_id","author","content","url"},
                sFavoriteSettingSelection,
                new String[]{MovieContract.MovieEntry.getMovieID(uri)},
                null,
                null,
                null);
    }

    /**
     * Get a Cursor using SQLiteQueryBuilder for a join between Trailer and movie
     * @param uri - The Uri location for Trailer
     * @return - Returns a Cursor with movie trailer
     */
    private Cursor getFavoriteMovieTrailer(Uri uri){
        return sFavoriteTrailerMovie.query(
                mOpenHelper.getReadableDatabase(),
                new String[]{"favorite_trailer._ID","trailer_id","favorite_movies.movie_id","favorite_movies.title","iso_6391","iso_3166_1","key","name","site","size","type"},
                sFavoriteSettingSelection,
                new String[]{MovieContract.MovieEntry.getMovieID(uri)},
                null,
                null,
                null);
    }

    /**
     * Uri Matcher that determine how the Uri is handling inputs
     * @return - Returns a UriMatcher
     */
    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_MOVIE_ID);
        matcher.addURI(authority, "movie_detail/*", MOVIE_DETAILS_NORMAL);
        matcher.addURI(authority, "movie_detail_favor/*", MOVIE_DETAILS_FAVORITE);
        matcher.addURI(authority, MovieContract.PATH_GENRE, GENRE);
        matcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILER);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_GENRE + "/*",GENRE_FOR_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/*",REVIEW_FOR_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/*",TRAILER_FOR_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIES + "/*", FAVORITE_MOVIES_ID);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_REVIEW,FAVORITE_REVIEW);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_REVIEW + "/*",FAVORITE_REVIEW_ID);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_TRAILER, FAVORITE_TRAILER);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_TRAILER + "/*", FAVORITE_TRAILER_ID);

        return matcher;
    }

    /**
     * Set the mOpenHelper to local variable
     * @return - Return the boolean result
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    /**
     * Determine the Uri type based on the Uri
     * @param uri - Pass in the Uri
     * @return - Returns the string for the content name
     */
    @Override
    public String getType(Uri uri){

        // Use the Uri Matcher to determine what kind of URI this is
        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_DETAILS_NORMAL:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_DETAILS_FAVORITE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case GENRE:
                return MovieContract.GenreEntry.CONTENT_TYPE;
            case GENRE_FOR_MOVIE:
                return MovieContract.GenreEntry.CONTENT_ITEM_TYPE;
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case REVIEW_FOR_MOVIE:
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            case TRAILER_FOR_MOVIE:
                return MovieContract.TrailerEntry.CONTENT_ITEM_TYPE;
            case FAVORITE_MOVIES:
                return MovieContract.FavoriteMovies.CONTENT_ITEM_TYPE;
            case FAVORITE_MOVIES_ID:
                return MovieContract.FavoriteMovies.CONTENT_ITEM_TYPE;
            case FAVORITE_REVIEW:
                return MovieContract.FavoriteReviewEntry.CONTENT_ITEM_TYPE;
            case FAVORITE_REVIEW_ID:
                return MovieContract.FavoriteReviewEntry.CONTENT_ITEM_TYPE;
            case FAVORITE_TRAILER:
                return MovieContract.FavoriteTrailerEntry.CONTENT_ITEM_TYPE;
            case FAVORITE_TRAILER_ID:
                return MovieContract.FavoriteTrailerEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri );
        }
    }

    /**
     * Query the specified table using specific project, selection, selectionArgs, and SortOrder
     * @param uri - Uri that content the path of the table
     * @param projection - The individual column to select
     * @param selection - The fields that should be included in the Where criteria
     * @param selectionArgs - The field value that should be included in the Where criteria
     * @param sortOrder - The sort order for the query
     * @return - Returns the Cursor of the specified table
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder){
        Cursor retCursor = null;

        switch (sUriMatcher.match(uri)){
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_WITH_MOVIE_ID: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        sMovieIdSettingSelection,
                        new String[]{MovieContract.MovieEntry.getMovieID(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_DETAILS_NORMAL: {
                // Updated Movie Detail with this solution instead of using three adapter to one adapter
                // with multiply viewType layout
                // https://discussions.udacity.com/t/how-do-you-design-popularmovies-detail-view-in-terms-of-loaders-adapters-tables/37618/6

                // Set-up the number of Cursor
                Cursor[] cursors = new Cursor[3];

                // Set-up the CursorCount to the length of the Cursors array
                int[] cursorsCount = new int[cursors.length];

                // Set the Uri for the movie information
                uri = MovieContract.MovieEntry.buildMovieIDUri(MovieContract.MovieEntry.getIntegerMovieID(uri));

                // Set the first entry in the cursor with the movie details
                cursors[0]= query(
                        uri,
                        Constants.DETAIL_MOVIE_COLUMNS,
                        null,
                        null,
                        null);

                // Set the first entry cursor count of the columns DetailMovieFragment.DETAIL_MOVIE_COLUMNS.length ;//
                cursorsCount[0] = cursors[0].getColumnCount();

                // Set the Uri for the movie review information
                uri = MovieContract.ReviewEntry.buildReviewMovieIDUri(MovieContract.MovieEntry.getIntegerMovieID(uri));

                // Set the second entry in the cursor with the review details
                cursors[1] = query(
                        uri,
                        Constants.REVIEW_MOVIE_COLUMNS,
                        null,
                        null,
                        null);

                // Set the second entry cursor count of the columns DetailMovieFragment.REVIEW_MOVIE_COLUMNS.length; //
                cursorsCount[1] = cursors[1].getColumnCount();

                // Set the Uri for the movie trailer information
                uri = MovieContract.TrailerEntry.buildTrailerMovieIDUri(MovieContract.MovieEntry.getIntegerMovieID(uri));

                // Set the third entry in the cursor with the review
                cursors[2] = query(
                        uri,
                        Constants.TRAILER_MOVIE_COLUMNS,
                        null,
                        null,
                        null);

                // Set the third entry cursor count of the columns DetailMovieFragment.TRAILER_MOVIE_COLUMNS.length; //
                cursorsCount[2] = cursors[2].getColumnCount();

                // Merge the cursor array into one cursor using
                // the Merge cursor
                retCursor = new MergeCursor(cursors);

                // Created and initial a new bundle
                Bundle arg = new Bundle();

                // Add additional information with key and value pair
                arg.putIntArray(Constants.MOVIE_DETAIL,cursorsCount);

                // Set an extra bite of information into the cursor
                retCursor.setExtras(arg);
                break;
            }
            case MOVIE_DETAILS_FAVORITE: {
                // Updated Movie Detail with this solution instead of using three adapter to one adapter
                // with multiply viewType layout
                // https://discussions.udacity.com/t/how-do-you-design-popularmovies-detail-view-in-terms-of-loaders-adapters-tables/37618/6

                // Set-up the number of Cursor
                Cursor[] cursors = new Cursor[3];

                // Set-up the CursorCount to the length of the Cursors array
                int[] cursorsCount = new int[cursors.length];

                // Set the Uri for the favorite movie information
                uri = MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(MovieContract.MovieEntry.getIntegerMovieID(uri));

                // Set the first entry in the cursor with the movie details
                cursors[0]= query(
                        uri,
                        Constants.DETAIL_MOVIE_COLUMNS,
                        null,
                        null,
                        null);

                // Set the first entry cursor count of the columns DetailMovieFragment.DETAIL_MOVIE_COLUMNS.length ;//
                cursorsCount[0] = cursors[0].getColumnCount();

                // Set the Uri for the movie review information
                uri = MovieContract.FavoriteReviewEntry.buildFavoriteReviewMovieIDUri(MovieContract.MovieEntry.getIntegerMovieID(uri));

                // Set the second entry in the cursor with the review details
                cursors[1] = query(
                        uri,
                        Constants.REVIEW_MOVIE_COLUMNS,
                        null,
                        null,
                        null);

                // Set the second entry cursor count of the columns DetailMovieFragment.REVIEW_MOVIE_COLUMNS.length; //
                cursorsCount[1] = cursors[1].getColumnCount();

                // Set the Uri for the movie trailer information
                uri = MovieContract.FavoriteTrailerEntry.buildFavoriteTrailerMovieIDUri(MovieContract.MovieEntry.getIntegerMovieID(uri));

                // Set the third entry in the cursor with the review
                cursors[2] = query(
                        uri,
                        Constants.TRAILER_MOVIE_COLUMNS,
                        null,
                        null,
                        null);

                // Set the third entry cursor count of the columns DetailMovieFragment.TRAILER_MOVIE_COLUMNS.length; //
                cursorsCount[2] = cursors[2].getColumnCount();

                // Merge the cursor array into one cursor using
                // the Merge cursor
                retCursor = new MergeCursor(cursors);

                // Created and initial a new bundle
                Bundle arg = new Bundle();

                // Add additional information with key and value pair
                arg.putIntArray(Constants.MOVIE_DETAIL,cursorsCount);

                // Set an extra bite of information into the cursor
                retCursor.setExtras(arg);
                break;
            }
            case GENRE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.GenreEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEW: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILER:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case GENRE_FOR_MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.GenreEntry.TABLE_NAME,
                        new String[]{"genre_id","name"},
                        sGenreIdSettingSelection,
                        new String[]{MovieContract.GenreEntry.getGenreID(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEW_FOR_MOVIE: {
                retCursor = getMovieReview(uri);
                break;
            }
            case TRAILER_FOR_MOVIE:{
                retCursor = getMovieTrailer(uri);
                break;
            }
            case FAVORITE_MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovies.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVORITE_MOVIES_ID:{
                retCursor = getFavoriteMovie(uri);
                break;
            }
            case FAVORITE_REVIEW: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVORITE_REVIEW_ID:{
                retCursor = getFavoriteMovieReview(uri);
                break;
            }
            case FAVORITE_TRAILER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteTrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVORITE_TRAILER_ID:{
                retCursor = getFavoriteMovieTrailer(uri);
                break;
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /**
     * Insert the specified table using specific project, selection, selectionArgs, and SortOrder
     * @param uri - Uri that content the path of the table
     * @param values - Content value to pass into the table
     * @return - Return the Uri of the inserted table
     */
    @Override
    public Uri insert(Uri uri, ContentValues values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        switch (match){
            case MOVIE:{
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null,values);
                if(_id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case GENRE:{
                long _id = db.insert(MovieContract.GenreEntry.TABLE_NAME, null,values);
                if(_id > 0)
                    returnUri = MovieContract.GenreEntry.buildGenreUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILER:{
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW:{
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVORITE_MOVIES_ID:{
                long _id = db.insert(MovieContract.FavoriteMovies.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.FavoriteMovies.buildFavoriteMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVORITE_REVIEW_ID:{
                long _id = db.insert(MovieContract.FavoriteReviewEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.FavoriteReviewEntry.buildFavoriteReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FAVORITE_TRAILER_ID:{
                long _id = db.insert(MovieContract.FavoriteTrailerEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.FavoriteTrailerEntry.buildFavoriteTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    /**
     * Delete the specified table using specific project, selection, selectionArgs
     * @param uri - Uri that content the path of the table
     * @param selection - The fields that should be included in the Where criteria
     * @param selectionArgs - The field value that should be included in the Where criteria
     * @return - Returns the int number of row deleted
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;

        if(null == selection) selection = "1";

        switch (match){
            case MOVIE:{
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case MOVIE_WITH_MOVIE_ID:{
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case GENRE:{
                rowsDeleted = db.delete(
                        MovieContract.GenreEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case TRAILER:{
                rowsDeleted = db.delete(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case REVIEW:{
                rowsDeleted = db.delete(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case FAVORITE_MOVIES_ID:{
                rowsDeleted = db.delete(
                        MovieContract.FavoriteMovies.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case FAVORITE_MOVIES:{
                rowsDeleted = db.delete(
                        MovieContract.FavoriteMovies.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case FAVORITE_REVIEW:{
                rowsDeleted = db.delete(
                        MovieContract.FavoriteReviewEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case FAVORITE_REVIEW_ID:{
                rowsDeleted = db.delete(
                        MovieContract.FavoriteReviewEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case FAVORITE_TRAILER:{
                rowsDeleted = db.delete(
                        MovieContract.FavoriteTrailerEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case FAVORITE_TRAILER_ID:{
                rowsDeleted = db.delete(
                        MovieContract.FavoriteTrailerEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    /**
     *  Update the specified table using specific project, selection, selectionArgs
     * @param uri - Uri that content the path of the table
     * @param values - Content value to pass into the table
     * @param selection - The fields that should be included in the Where criteria
     * @param selectionArgs - The field value that should be included in the Where criteria
     * @return - Returns the int number of row update
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (match){
            case MOVIE:{
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIE_WITH_MOVIE_ID:{
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case GENRE:{
                rowsUpdated = db.update(MovieContract.GenreEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case REVIEW:{
                rowsUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case TRAILER:{
                rowsUpdated = db.update(MovieContract.TrailerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVORITE_MOVIES_ID:{
                rowsUpdated = db.update(MovieContract.FavoriteMovies.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVORITE_MOVIES:{
                rowsUpdated = db.update(MovieContract.FavoriteMovies.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVORITE_REVIEW_ID:{
                rowsUpdated = db.update(MovieContract.FavoriteReviewEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVORITE_REVIEW:{
                rowsUpdated = db.update(MovieContract.FavoriteReviewEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVORITE_TRAILER_ID:{
                rowsUpdated = db.update(MovieContract.FavoriteTrailerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVORITE_TRAILER:{
                rowsUpdated = db.update(MovieContract.FavoriteTrailerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    /**
     * Bulkd insert the specified table using specific project, selection, selectionArgs, and SortOrder
     * @param uri - Uri that content the path of the table
     * @param values - Content value to pass into the table
     * @return - Returns the int number of row inserted
     */
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        db.beginTransaction();
        int returnCount = 0;
        long _id;

        try {
            for (ContentValues value : values) {
                if(value != null) {
                    switch (match) {
                        case MOVIE: {
                            _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case MOVIE_WITH_MOVIE_ID: {
                            _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case GENRE: {
                            _id = db.insert(MovieContract.GenreEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case TRAILER: {
                            _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case REVIEW: {
                            _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case FAVORITE_MOVIES: {
                            _id = db.insert(MovieContract.FavoriteMovies.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case FAVORITE_MOVIES_ID: {
                            _id = db.insert(MovieContract.FavoriteMovies.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case FAVORITE_REVIEW: {
                            _id = db.insert(MovieContract.FavoriteReviewEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case FAVORITE_REVIEW_ID: {
                            _id = db.insert(MovieContract.FavoriteReviewEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case FAVORITE_TRAILER: {
                            _id = db.insert(MovieContract.FavoriteTrailerEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case FAVORITE_TRAILER_ID: {
                            _id = db.insert(MovieContract.FavoriteTrailerEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        default:
                            return super.bulkInsert(uri, values);
                    }
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }
}