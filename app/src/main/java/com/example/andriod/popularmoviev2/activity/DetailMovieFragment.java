package com.example.andriod.popularmoviev2.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.data.MovieContract.MovieEntry;
import com.example.andriod.popularmoviev2.data.MovieContract.TrailerEntry;
import com.example.andriod.popularmoviev2.data.MovieContract.ReviewEntry;
import com.example.andriod.popularmoviev2.data.MovieTableSync;
import com.example.andriod.popularmoviev2.other.Constants;
import com.example.andriod.popularmoviev2.service.GenreInfoService;
import com.example.andriod.popularmoviev2.sync.MovieSyncAdapter;

/**
 * DetailMovieFragment (Detail Movie Fragment) shows general information
 * about the movie (DETAIL_MOVIE_COLUMNS)
 */
public class DetailMovieFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Local Uri identify
    private Uri mUri;

    // Three section adapters (Movie details, Trailers, and Reviews)
    private DetailMovieAdapter mDetailMovieAdapter;

    // ListView holder the movie detail adapter
    private ListView movieListView;

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
            mUri = arguments.getParcelable(Constants.MOVIE_DETAIL_URI);
        }
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
        movieListView = (ListView) rootView.findViewById(R.id.detail_MovieDetaiListView);

        setupAdapter();

        // Returns the view with all the information
        return rootView;
    }

    /**
     * Set-up Adapter to GridView and set-up column number based on device rotation
     */
    private void setupAdapter(){
        // Set the ListView to the specific adapter
        movieListView.setAdapter(mDetailMovieAdapter);
    }

    /**
     * Start the loadManager after the activity has been created
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Get the loader manager start for this calls
        getLoaderManager().initLoader(Constants.DETAIL_MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * On Movie change run the syncAdapter immediately
     */
    void onMovieChanged(String movieType){
        /*Log.v("Create ","MovieFragment - onMovieChanged");
        Uri uri = mUri;
        if(mUri != null) {
            Uri updateUri = uri;
            if(movieType.contains("popular")){
                updateUri = MovieContract.MovieEntry.buildMovieIDUri(MovieEntry.getIntegerMovieID(uri));
            }else if(movieType.contains("top_rated")){
                updateUri = MovieContract.MovieEntry.buildMovieIDUri(MovieEntry.getIntegerMovieID(uri));
            }else {
                updateUri = MovieContract.FavoriteMovies.buildFavoriteMovieIDUri(Integer.parseInt(MovieContract.FavoriteMovies.getFavoriteMovieID(uri)));
            }
            mUri = updateUri;
            MovieSyncAdapter.syncImmediately(getActivity());
            getLoaderManager().restartLoader(Constants.DETAIL_MOVIE_LOADER, null, this);
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

            // Cursor Loader that point to MovieDetail which includes selection
            // movie, movie's review, and movie's trailer information
            Uri allDetail = MovieContract.MovieEntry.buildMovieDetailAllSection(MovieContract.MovieEntry.getIntegerMovieID(mUri));

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
            //setNotificationUri(ContentResolver cr, Uri uri)
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
