package com.example.andriod.popularmoviev2.activity;

import android.database.Cursor;
import android.net.Uri;
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
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.data.MovieContract.MovieEntry;

/**
 * DetailMovieFragment (Detail Movie Fragment) shows general information
 * about the movie (DETAIL_MOVIE_COLUMNS)
 */
public class DetailMovieFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Local class Log tag variable
    private final String LOG_TAG = DetailMovieFragment.class.getSimpleName();

    // String constent for the MovieDetailFragment
    static final String MOVIE_DETAILS_URI = "MOVIE_DETAILS_URI";

    // String constent for movie share hashtag
    private static final String MOVIE_SHARE_HASHTAG = " #MovieDisplayerApp";

    // ShareActionProvider for Movie Detail App
    private ShareActionProvider mShareActionProvider;

    // String that will display in share tag
    private String mMovieDetail;

    // Local Uri identify
    private Uri mUri;

    //private DetailMovieAdapter detailMovieAdapter;

    // Movie Detail Loader for DetailMovieFragment
    private static final int DETAIL_MOVIE_LOADER = 0;

    // LinearLayout for User Rating for Star
    LinearLayout mUserRatingLayout;

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

    // Set the local Movie Detail elements
    private ImageView mDetail_imageView;
    private TextView mDetail_titleTextView;
    private TextView mDetail_synopsisTextView;
    private TextView mDetail_userRateingTextView;
    private TextView mDetail_releaseDateTextView;
    private TextView mDetail_genreTextView;

    // DetailMovieFragment Construction
    public DetailMovieFragment() {}

    /**
     * When the View is created I get the Bundle argument with the movie Uri
     *
     * I inflate the fragment layout and bind local layout elements to the fragment elements
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
            mUri = arguments.getParcelable(DetailMovieFragment.MOVIE_DETAILS_URI);
        }

        // Get the current DetailActivityFragment view layout
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //
        // Return newly set view
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
     * Start the loading on the Movie Detail records
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Get the Cursor from the loader
     * @param id -
     * @param args - Bundle for fragment
     * @return - Retrun a loader specific cursor
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.

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
        return null;
    }

    /**
     * When the retrieving of the movie details are finished updating
     * the XML layout with the movie detailed information
     * @param loader - The loader the queries the movie content resolver
     * @param data - The cursor that is retrun from the loader and content resolver
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    /**
     * Reset the loader cursor (I do not with it)
     * @param loader - The loader the queries the movie content resolver
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    /**
     * Get the movie detail fragments Bundle string identiry
     * @return - Return the string used to find Uri information in bundle
     */
    public static String getMOVIE_DETAIL_URI(){
        return MOVIE_DETAILS_URI;
    }
}
