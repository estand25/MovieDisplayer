package com.example.andriod.popularmoviev2.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.data.MovieDbHelper;
import com.example.andriod.popularmoviev2.data.MovieProvider;
import com.example.andriod.popularmoviev2.data.MovieSyncUploader;
import com.example.andriod.popularmoviev2.data.MovieContract.MovieEntry;
import com.example.andriod.popularmoviev2.data.MovieContract.TrailerEntry;
import com.example.andriod.popularmoviev2.data.MovieContract.ReviewEntry;

/**
 * DetailMovieFragment (Detail Movie Fragment) shows general information
 * about the movie (DETAIL_MOVIE_COLUMNS)
 */
public class DetailMovieFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Local class Log tag variable
    private final String LOG_TAG = DetailMovieFragment.class.getSimpleName();

    // String constent for the MovieDetailFragment
    static final String MOVIE_DETAIL_URI = "URI";

    // String constent for movie share hashtag
    private static final String MOVIE_SHARE_HASHTAG = " #MovieDisplayerApp";

    // ShareActionProvider for Movie Detail App
    private ShareActionProvider mShareActionProvider;

    // String that will display in share tag
    private String mMovieDetail;

    // Local Uri identify
    private Uri mUri;

    // Three detail adapters (Movie details, Trailers, and Reviews)
    private DetailMovieAdapter detailMovieAdapter;
    private DetailTrailerAdapter detailTrailerAdapter;
    private DetailReviewAdapter detailReviewAdapter;

    // Three detail ListViews
    private ListView movieListView;
    private ListView trailListView;
    private ListView reviewListView;

    // Movie Detail Loader for DetailMovieFragment
    private static final int DETAIL_MOVIE_LOADER = 0;

    // Trailer Loader for Detail Trailer
    private static final int TRAILER_MOVIE_LOADER = 1;

    // Review Loader for Detail Review
    private static final int REVIEW_MOVIE_LOADER = 2;

    // Movie String[] for Detail Fragment
    private static final String[] DETAIL_MOVIE_COLUMNS = {
            MovieEntry.TABLE_NAME + "." + MovieEntry._ID,
            MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.COLUMN_POSTER_PATH,
            MovieEntry.COLUMN_ADULT,
            MovieEntry.COLUMN_OVERVIEW,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_GENRE_IDS,
            MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieEntry.COLUMN_ORIGINAL_LANGUAGE,
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_BACKDROP_PATH,
            MovieEntry.COLUMN_POPULARITY,
            MovieEntry.COLUMN_VOTE_COUNT,
            MovieEntry.COLUMN_VIDEO,
            MovieEntry.COLUMN_VOTE_AVERAGE
    };

    // Trailer String[] for Detail Trailer Fragment
    private static final String[] TRAILER_MOVIE_COLUMNS = {
            TrailerEntry.TABLE_NAME + "." + TrailerEntry._ID,
            TrailerEntry.COLUMN_TRAILER_ID,
            TrailerEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            TrailerEntry.COLUMN_ISO_6391,
            TrailerEntry.COLUMN_ISO_31661,
            TrailerEntry.COLUMN_KEY,
            TrailerEntry.COLUMN_NAME,
            TrailerEntry.COLUMN_SITE,
            TrailerEntry.COLUMN_SIZE,
            TrailerEntry.COLUMN_TYPE
    };
    // Review String[] for Detail Review Fragment
    private static final String[] REVIEW_MOVIE_COLUMNS = {
            ReviewEntry.TABLE_NAME + "." + ReviewEntry._ID,
            ReviewEntry.COLUMN_REVIEW_ID,
            ReviewEntry.COLUMN_MOVIE_ID,
            ReviewEntry.COLUMN_AUTHOR,
            ReviewEntry.COLUMN_CONTENT,
            ReviewEntry.COLUMN_URL
    };

    // These indices are tied to MOVIE_COLUMNS. If MOVIE_COLUMNS change, these need change too
    static final int COL_DETAIL_ID = 0;
    static final int COL_DETAIL_MOVIE_ID = 1;
    static final int COL_DETAIL_MOVIE_POSTER_PATH = 2;
    static final int COL_DETAIL_MOVIE_ADULT = 3;
    static final int COL_DETAIL_MOVIE_OVERVIEW = 4;
    static final int COL_DETAIL_MOVIE_RELEASE_DATE = 5;
    static final int COL_DETAIL_MOVIE_GENRE_IDS = 6;
    static final int COL_DETAIL_MOVIE_ORIG_TITLE = 7;
    static final int COL_DETAIL_MOVIE_ORIG_LANGUAGE = 8;
    static final int COL_DETAIL_MOVIE_TITLE = 9;
    static final int COL_DETAIL_MOVIE_BACKDROP_PATH = 10;
    static final int COL_DETAIL_MOVIE_POPULARITY = 11;
    static final int COL_DETAIL_MOVIE_VOTE_COUNT = 12;
    static final int COL_DETAIL_MOVIE_VIDEO = 13;
    static final int COL_DETAIL_MOVIE_VOTE_AVERAGE = 14;
    static final int COL_DETAIL_MOVIE_TYPE = 15;

    // There indices are tied to TRAILER_COLUMNS. If TRAILER_COLUMNS change, these need change too
    static final int COL_TRAILER__ID = 16;
    static final int COL_TRAILER_ID = 17;
    static final int COL_TRAILER_MOVIE_ID = 18;
    static final int COL_MOVIE_TITLE = 19;
    static final int COL_TRAILER_ISO_6391 = 20;
    static final int COL_TRAILER_ISO_31661 = 21;
    static final int COL_TRAILER_KEY = 22;
    static final int COL_TRAILER_NAME = 23;
    static final int COL_TRAILER_SITE = 24;
    static final int COL_TRAILER_SIZE = 25;
    static final int COL_TRAILER_TYPE = 26;

    // There indices are tied to REVIEW_COLUMNS. If REVIEW_COLUMNS change, these need change too
    static final int COL_REVIEW__ID = 27;
    static final int COL_REVIEW_ID = 28;
    static final int COL_REVIEW_MOVIE_ID = 29;
    static final int COL_REVIEW_AUTHOR = 30;
    static final int COL_REVIEW_CONTENT = 31;
    static final int COL_REVIEW_URL = 32;

    public DetailMovieFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    /**
     * When the View is created I get the Bundle argument with the movie Uri
     *
     * I inflate the fragment layout and bind local layout elements to the fragment elements
     * also set the ListView elements to there adapters
     *
     * @param inflater - inflater the declare layout elements
     * @param container - Get the container for the inflater
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     * @return - Return the populate movie view to the app
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(DetailMovieFragment.MOVIE_DETAIL_URI);
        }

        // Initializing the curstom movie detail, review, and trailer adapters
        // Got the reference for this post:
        // http://stackoverflow.com/questions/22888233/set-multiple-cursor-loaders-with-multiple-adapters-android
        detailMovieAdapter = new DetailMovieAdapter(getActivity(),null,0);
        detailReviewAdapter = new DetailReviewAdapter(getActivity(),null,0);
        detailTrailerAdapter = new DetailTrailerAdapter(getActivity(),null,0);

        // Get the current DetailActivityFragment view layout
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Find the ListViews on the fragment_detail layout
        movieListView = (ListView) rootView.findViewById(R.id.detail_MovieDetaiListView);
        trailListView = (ListView) rootView.findViewById(R.id.detail_trailerListView);
        reviewListView = (ListView) rootView.findViewById(R.id.detail_reviewListView);

        // Set the ListView to there specific adapters
        movieListView.setAdapter(detailMovieAdapter);
        trailListView.setAdapter(detailTrailerAdapter);
        reviewListView.setAdapter(detailReviewAdapter);

        return rootView;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, this);
        getLoaderManager().initLoader(TRAILER_MOVIE_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /*inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }*/
    }

    /**
     * Remove first and end braskets
     * @param line - String of genre id with brasket
     * @return - String of genres id without braskets
     */
    public String getGenreName(String line){
        String result = "";
        result = line.substring(1, line.length()-1);
        return result;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case DETAIL_MOVIE_LOADER:{
                Log.v("Movie Content URI ", MovieContract.MovieEntry.CONTENT_URI.toString());
                Log.v("Movie URI ", mUri.toString());

                return new CursorLoader(
                        getActivity(),
                        mUri,
                        DETAIL_MOVIE_COLUMNS,
                        null,
                        null,
                        null);
            }
            case TRAILER_MOVIE_LOADER:{
                Log.v("Trailer Content URI ", TrailerEntry.CONTENT_URI.toString());
                Log.v("Trailer URI ", mUri.toString());

                return new CursorLoader(
                        getActivity(),
                        mUri,
                        TRAILER_MOVIE_COLUMNS,
                        null,
                        null,
                        null);

            }
            case REVIEW_MOVIE_LOADER:{
                Log.v("Review Content URI ", MovieContract.ReviewEntry.CONTENT_URI.toString());
                Log.v("Review URI ", mUri.toString());

                return new CursorLoader(
                        getActivity(),
                        mUri,
                        REVIEW_MOVIE_COLUMNS,
                        null,
                        null,
                        null);

            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case DETAIL_MOVIE_LOADER:{
                Log.v("Movie Content ", " populate");
                detailMovieAdapter.swapCursor(data);
            }
            break;
            case TRAILER_MOVIE_LOADER:{
                Log.v("Trailer Content ", " populate");
                detailTrailerAdapter.swapCursor(data);

            }
            break;
            case REVIEW_MOVIE_LOADER:{
                Log.v("Review Content ", " populate");
                detailReviewAdapter.swapCursor(data);
            }
            break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()){
            case DETAIL_MOVIE_LOADER:{
                Log.v("Movie Content ", " populate");
                detailMovieAdapter.swapCursor(null);
            }
            break;
            case TRAILER_MOVIE_LOADER:{
                Log.v("Trailer Content ", " populate");
                detailTrailerAdapter.swapCursor(null);
            }
            break;
            case REVIEW_MOVIE_LOADER:{
                Log.v("Review Content ", " populate");
                detailReviewAdapter.swapCursor(null);
            }
            break;
        }
    }

    public String getMovieDetailUri(){
        return MOVIE_DETAIL_URI;
    }
}
