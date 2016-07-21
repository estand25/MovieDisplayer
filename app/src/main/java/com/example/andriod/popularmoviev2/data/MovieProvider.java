package com.example.andriod.popularmoviev2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Note: Base on SunShine App
 * MovieViewerProvider handle the SQLite query create
 * insertion, deletion, and updates)
 * Created by Standley Eugene on 7/10/2016.
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

    private static final SQLiteQueryBuilder sMovieWithReview;
    private static final SQLiteQueryBuilder sMovieWithTrailer;
    private static final SQLiteQueryBuilder sFavoriteMovie;

    static{
        sMovieWithReview = new SQLiteQueryBuilder();
        sMovieWithTrailer = new SQLiteQueryBuilder();
        sFavoriteMovie = new SQLiteQueryBuilder();

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
        // favorite_movie INNER JOIN movie on movie_id = favorite_movie.movie_id
        sFavoriteMovie.setTables(
                MovieContract.FavoriteMovies.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.FavoriteMovies.TABLE_NAME +
                        "." + MovieContract.FavoriteMovies.COLUMN_MOVIE_ID
        );
    }

    // movie.movie_id = ?
    public static final String sMovieIdSettingSelection =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";

    // movie._ID = ?
    public static final String sIdSettingSelection =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry._ID + " = ? ";

    // movie.movie_type = ?
    public static final String sMovieTypeSettingSelection =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_MOVIE_TYPE + " = ?";

    /**
     * Get a Cursor using SQLiteQueryBuilder for a join between Review and movie
     * @param uri - The Uri location for Review
     * @return - Returns a Cursor with review trailer
     */
    private Cursor getMovieReview(Uri uri){
        return sMovieWithReview.query(mOpenHelper.getReadableDatabase(),
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
     * Get a Cusor using SQLiteQueryBuilder for a join between Favorite and movie
     * @param uri - The Uri location for Favorite
     * @return - Returns a Cursor with favorite movie
     */
    private Cursor getFavoriteMovie(Uri uri){
        return sFavoriteMovie.query(
                mOpenHelper.getReadableDatabase(),
                null,
                null,
                null,
                null,
                null,
                null);
    }

    /**
     * Rri Matcher that determine how the Uri is handling inputs
     * @return - Returns a UriMatcher
     */
    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_MOVIE_ID);
        matcher.addURI(authority, MovieContract.PATH_GENRE, GENRE);
        matcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILER);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_GENRE + "/*",GENRE_FOR_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIES + "/*", FAVORITE_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/*",REVIEW_FOR_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/*",TRAILER_FOR_MOVIE);

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
            case GENRE:
                return MovieContract.GenreEntry.CONTENT_TYPE;
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
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri );
        }
    }


    public String getGenreName(int id){
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MovieContract.GenreEntry.TABLE_NAME,
                new String[]{"name"},
                "genre_id = ?",
                new String[]{Integer.toString(id)},
                null,
                null,
                null);

        String genreName = cursor.getString(0);

        return genreName;
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
                        MovieProvider.sMovieIdSettingSelection,
                        new String[]{MovieContract.MovieEntry.getMovieID(uri)},
                        null,
                        null,
                        sortOrder
                );
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
                        sMovieIdSettingSelection,
                        new String[]{"movie.genre_ids"},
                        null,
                        null,
                        sortOrder
                );

            }
            case REVIEW_FOR_MOVIE: {
                retCursor = getMovieReview(uri);
                break;
            }
            case TRAILER_FOR_MOVIE:{
                retCursor = getMovieTrailer(uri);
                break;
            }
            case FAVORITE_MOVIES:{
                retCursor = getFavoriteMovie(uri);
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
            case FAVORITE_MOVIES:{
                long _id = db.insert(MovieContract.FavoriteMovies.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.FavoriteMovies.buildFavoriteMovieUri(_id);
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
            case FAVORITE_MOVIES:{
                rowsDeleted = db.delete(
                        MovieContract.FavoriteMovies.TABLE_NAME,
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
            case FAVORITE_MOVIES:{
                rowsUpdated = db.update(MovieContract.FavoriteMovies.TABLE_NAME,
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
                    default:
                        return super.bulkInsert(uri, values);
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