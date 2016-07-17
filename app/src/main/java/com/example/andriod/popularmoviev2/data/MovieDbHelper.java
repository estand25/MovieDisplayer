package com.example.andriod.popularmoviev2.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.andriod.popularmoviev2.data.MovieContract.MovieEntry;
import com.example.andriod.popularmoviev2.data.MovieContract.GenreEntry;
import com.example.andriod.popularmoviev2.data.MovieContract.ReviewEntry;
import com.example.andriod.popularmoviev2.data.MovieContract.TrailerEntry;
import com.example.andriod.popularmoviev2.data.MovieContract.FavoriteMovies;

/**
 * Note: Base on SunShine App
 * MovieViewerDBHelper class create the Database and
 * create the individual tables (Movie, Review, Trailer, and Genre tables)
 * Created by StandleyEugene on 7/10/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movieviewer.db";

    static final String DB_PATH = "/data/data/com.example.android.popularmovie2/databases";

    /**
     * Construction for MovieDbHelper
     * @param context
     */
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create all the tables in the database on class creation
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        // Create the create string for the individual table
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " ( " +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ADULT + " BLOB NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_GENRE_IDS + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieEntry.COLUMN_VOTE_COUNT + " REAL NOT NULL, " +
                MovieEntry.COLUMN_VIDEO + " BLOB NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TYPE +" TEXT NOT NULL, " +

                // To assure the application have just one movie entry
                // it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);


        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME +" ( " +
                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReviewEntry.COLUMN_REVIEW_ID + " INTEGER NOT NULL, " +
                ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_URL + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + ")); ";
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);

        final String SQL_CREATE_GENRE_TABLE = "CREATE TABLE " + GenreEntry.TABLE_NAME + " ( " +
                GenreEntry.COLUMN_GENRE_ID + " INTEGER PRIMARY KEY, " +
                GenreEntry.COLUMN_NAME + " TEXT NOT NULL); ";
        sqLiteDatabase.execSQL(SQL_CREATE_GENRE_TABLE);

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " ( "+
                TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TrailerEntry.COLUMN_TRAILER_ID + " INTEGER NOT NULL, "+
                TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                TrailerEntry.COLUMN_ISO_6391 + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_ISO_31661 + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_SITE + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_SIZE + " INTEGER NOT NULL, " +
                TrailerEntry.COLUMN_TYPE + " TEXT NOT NULL, " +

                " FOREIGN KEY (" + TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + ")); ";
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);

        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + FavoriteMovies.TABLE_NAME + " ( "+
                FavoriteMovies.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "+
                FavoriteMovies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                " FOREIGN KEY (" + FavoriteMovies.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + ")); ";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }



    /**
     * Return Cursor with Genre name
     * Note: Final a example that error checks inital SQLiteDatabase before
     * doing anything so I used it.
     * https://gist.github.com/mikeplate/9173040
     * @param genreid
     * @return
     */
    public Cursor getIndGenre(String genreid){
        // Set a local SQLiteDatabase to use the rawQuery against
        SQLiteDatabase db = this.getReadableDatabase();

        // Check if local db has anything in there if it does not return null
        if(db == null){
            return null;
        }

        // Set-up query string to pass to local cursor
        String rawQuery = "SELECT name FROM genre WHERE genre_id = " +genreid;

        // Return the cursor with the results of the query
        return db.rawQuery(rawQuery,null);
    }

    /**
     * When new database is create Old database will be deleted
     * @param sqLiteDatabase - Database object
     * @param oldVersion - old Database version
     * @param newVersion - new Database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GenreEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
    }
}