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
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ShareActionProvider;

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

    // String constent for the MovieDetailFragment
    public static final String MOVIE_DETAIL = "MOVIE_DETAIL";

    // Movie String[] for Detail Fragment
    public static final String[] DETAIL_MOVIE_COLUMNS = {
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
    public static final String[] TRAILER_MOVIE_COLUMNS = {
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
    public static final String[] REVIEW_MOVIE_COLUMNS = {
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
        // Create new variable holder for bundle
        // set the new bundle to the current arguments
        Bundle arguments = getArguments();

        // Check if new bundle is populated or null
        // then set class level Uri or do nothing
        if(arguments != null){
            mUri = arguments.getParcelable(DetailMovieFragment.MOVIE_DETAIL_URI);
        }

        // Get the loader manager start for this calls
        getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, this);
        Log.v("Loader Name "," Detail Movie, trailers, & reviews");
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
        // Initializing the custom movie adapter with (detail, review, and trailer section layout)
        mDetailMovieAdapter = new DetailMovieAdapter(getActivity(),null,0);

        // Get the current DetailMovieFragment view layout
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Find the ListViews on the fragment_detail layout
        ListView movieListView = (ListView) rootView.findViewById(R.id.detail_MovieDetaiListView);

        // Set the ListView to the specific adapter
        movieListView.setAdapter(mDetailMovieAdapter);

        // Returns the view with all the information
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
        if (null != mUri) {
            // Cursor Loader that point to MovieDetail
            // which includes selection movie, movie's review, and movie's trailer information
            Uri allDetail = MovieContract.MovieEntry.buildMovieDetailAllSection(MovieContract.MovieEntry.getIntegerMovieID(mUri));
            Log.v("All Section Detail ",allDetail.toString());

            return new CursorLoader(
                    getActivity(),
                    allDetail,
                    null,
                    null,
                    null,
                    null);
        }

        // Return null if bundle argument has not been populated
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
            // Add the new cursor data to the adapter
            mDetailMovieAdapter.swapCursor(data);
        }
    }

    /**
     * Reset the movie adapter for the individual adapter elements
     * @param loader - Current cursor loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDetailMovieAdapter.swapCursor(null);
    }
}
