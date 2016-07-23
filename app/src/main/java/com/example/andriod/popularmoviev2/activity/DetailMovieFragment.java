package com.example.andriod.popularmoviev2.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
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
    private DetailMovieAdapter mDetailMovieAdapter;

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
            MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieEntry.COLUMN_MOVIE_TYPE
    };

    // Trailer String[] for Detail Trailer Fragment
    private static final String[] TRAILER_MOVIE_COLUMNS = {
            TrailerEntry.TABLE_NAME + "." + TrailerEntry._ID,
            TrailerEntry.COLUMN_TRAILER_ID,
            MovieEntry.TABLE_NAME + "." + MovieEntry.COLUMN_MOVIE_ID,
            MovieEntry.TABLE_NAME + "." + MovieEntry.COLUMN_TITLE,
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

    /**
     * Empty construction
     */
    public DetailMovieFragment(){}

    /**
     * Set the mUri (local variable) with the Bundle argument data
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(DetailMovieFragment.MOVIE_DETAIL_URI);
        }

        getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, this);
        Log.v("Loader Name "," Detail Movie");
        getLoaderManager().initLoader(TRAILER_MOVIE_LOADER, null, this);
        Log.v("Loader Name "," Detail Movie Trailers");
        getLoaderManager().initLoader(REVIEW_MOVIE_LOADER, null, this);
        Log.v("Loader Name "," Detail Movie Reviews");
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
        // Initializing the curstom movie detail, review, and trailer adapters
        // Got the reference for this post:
        // http://stackoverflow.com/questions/22888233/set-multiple-cursor-loaders-with-multiple-adapters-android
        mDetailMovieAdapter = new DetailMovieAdapter(getActivity(),null,0);

        // Get the current DetailActivityFragment view layout
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Find the ListViews on the fragment_detail layout
        ListView movieListView = (ListView) rootView.findViewById(R.id.detail_MovieDetaiListView);

        // Set the ListView to there specific adapters
        movieListView.setAdapter(mDetailMovieAdapter);
        return rootView;
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
     * Get the table data information from the content provider
     * @param id - Loader number for the individual movie detail elements
     * @param args - Bundle elements
     * @return - Data for the individual cursors in loader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            switch (id) {
                case DETAIL_MOVIE_LOADER: {
                    Log.v("DetailMovieFragment ", MovieContract.MovieEntry.CONTENT_URI.toString());
                    Log.v("Movie URI ", mUri.toString());

                    return new CursorLoader(
                            getActivity(),
                            mUri,
                            DETAIL_MOVIE_COLUMNS,
                            null,
                            null,
                            null);
                }
                case TRAILER_MOVIE_LOADER: {
                    Log.v("Trailer before URI ", TrailerEntry.CONTENT_URI.toString());
                    Log.v("Trailer URI ", mUri.toString());

                    Uri trailerUri = MovieContract.TrailerEntry.buildTrailerMovieIDUri(MovieContract.MovieEntry.getIntegerMovieID(mUri));

                    Log.v("Trailer after URI ", trailerUri.toString());

                    return new CursorLoader(
                            getActivity(),
                            trailerUri,
                            TRAILER_MOVIE_COLUMNS,
                            null,
                            null,
                            null);
                }
                case REVIEW_MOVIE_LOADER: {
                    Log.v("Review Content URI ", MovieContract.ReviewEntry.CONTENT_URI.toString());
                    Log.v("Review URI ", mUri.toString());

                    Uri reviewUri = MovieContract.ReviewEntry.buildReviewMovieIDUri(MovieContract.MovieEntry.getIntegerMovieID(mUri));

                    Log.v("Review after URI ", reviewUri.toString());

                    return new CursorLoader(
                            getActivity(),
                            reviewUri,
                            REVIEW_MOVIE_COLUMNS,
                            null,
                            null,
                            null);
                }
            }
        }
        return null;
    }

    /**
     * Populates the individual movie adapter (movie details, trailer, and review) from
     * the table cursor data
     * @param loader - Current crsor loader
     * @param data - Retrieved table data in the cursor
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            Bundle arg = new Bundle();

            switch (loader.getId()) {
                case DETAIL_MOVIE_LOADER: {
                    Log.v("Movie onLoadFinished ", " populate");

                    // Set the argument for the Detail Movie Loader Section of adapter
                    // Key is the movie detail section constant and value is 0
                    arg.putInt(DetailMovieAdapter.MOVIE_DETAIL,0);

                    // Set the cursor extra bundle argument
                    data.setExtras(arg);

                    // Add the new cursor data to the adapter
                    mDetailMovieAdapter.swapCursor(data);
                }
                break;
                case TRAILER_MOVIE_LOADER: {
                    Log.v("Trailer onLoadFinished ", " populate");

                    // Set the argument for the Detail Movie Loader Section of adapter
                    // Key is the movie detail section constant and value is 0
                    arg.putInt(DetailMovieAdapter.TRAILER_DETAIL,1);

                    // Set the cursor extra bundle argument
                    data.setExtras(arg);

                    // Add the new cursor data to the adapter
                    mDetailMovieAdapter.swapCursor(data);

                }
                break;
                case REVIEW_MOVIE_LOADER: {
                    Log.v("Review onLoadFinished ", " populate");

                    // Set the argument for the Detail Movie Loader Section of adapter
                    // Key is the movie detail section constant and value is 0
                    arg.putInt(DetailMovieAdapter.REVIEW_DETAIL,2);

                    // Set the cursor extra bundle argument
                    data.setExtras(arg);

                    // Add the new cursor data to the adapter
                    mDetailMovieAdapter.swapCursor(data);
                }
                break;
            }
        }
    }

    /**
     * Reset the movie adapter for the individual adapter elements
     * @param loader - Current cursor loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDetailMovieAdapter.swapCursor(null);
        //mDetailTrailerAdapter.swapCursor(null);
        //mDetailReviewAdapter.swapCursor(null);
    }
}
