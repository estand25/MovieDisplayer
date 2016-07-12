package com.example.andriod.popularmoviev2.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Note: Base on SunShine App
 * MovieViewerProvider handle the SQLite query create
 * insertion, deletion, and updates)
 * Created by StandleyEugene on 7/10/2016.
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int MOVIE = 100;
    static final int GENRE = 101;
    static final int REVIEW = 102;
    static final int TRAILER = 103;
    static final int REVIEW_FOR_MOVIE = 104;
    static final int TRAILER_FOR_MOVIE = 105;
    static final int FAVORITE_MOVIES = 106;

    private static final SQLiteQueryBuilder sMovieWithReview;
    private static final SQLiteQueryBuilder sMovieWithTrailer;

    static{
        sMovieWithReview = new SQLiteQueryBuilder();
        sMovieWithTrailer = new SQLiteQueryBuilder();

        // This is an inner join which looks at
        // Review INNER JOIN Movie on Movie.id = Review.movie_id


        sMovieWithReview.setTables(
                MovieContract.ReviewEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.ReviewEntry.TABLE_NAME +
                        "." + MovieContract.ReviewEntry.COLUMN_MOVIE_ID
        );

        // This is an inner join which looks at
        // movie INNER JOIN Trailer on Trailer.movie_id = Review.movie_id
        // Trailer INNER JOIN Movie on Movie.id = Trailer.Movie_id
        sMovieWithTrailer.setTables(
                MovieContract.TrailerEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.TrailerEntry.TABLE_NAME +
                        "." + MovieContract.TrailerEntry.COLUMN_MOVIE_ID
        );
    }

    // Review.movie_id = ?
    private static final String sMovieIdSettingSelection =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";


    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code
        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_GENRE, GENRE);
        matcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILER);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/*",REVIEW_FOR_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/*",TRAILER_FOR_MOVIE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri){

        // Use the Uri Matcher to determine what kind of URI this is
        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case GENRE:
                return MovieContract.GenreEntry.CONTENT_TYPE;
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case REVIEW_FOR_MOVIE:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case TRAILER_FOR_MOVIE:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case FAVORITE_MOVIES:
                return MovieContract.FavoriteMovies.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri );
        }
    }

    /*
        Query the specified table using specific project, selection, selectionArgs, and SortOrder
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
            case REVIEW_FOR_MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        new String[]{"review_id","author","content","URL"},
                        sMovieIdSettingSelection,
                        new String[]{"movie_id"},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILER_FOR_MOVIE:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        new String[]{"trailer_id","iso_6391","iso_3166_1","key","name","site","size","type"},
                        sMovieIdSettingSelection,
                        new String[]{"movie_id"},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVORITE_MOVIES:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        new String[]{"movie_id"},
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Insert the specified table using specific project, selection, selectionArgs, and SortOrder
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

    /*
        Delete the specified table using specific project, selection, selectionArgs
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

    /*
        Update the specified table using specific project, selection, selectionArgs
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

    /*
        Bulkd insert the specified table using specific project, selection, selectionArgs, and SortOrder
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
