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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.other.Constants;
import com.example.andriod.popularmoviev2.other.Utility;

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
     *
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Create new variable holder for bundle
        // set the new bundle to the current arguments
        Bundle arguments = getArguments();

        // Check if new bundle is populated or null
        // then set class level Uri or do nothing
        if(arguments != null){
            mUri = arguments.getParcelable(Constants.MOVIE_DETAIL_URI);
        }
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
        //movieListView.setDescendantFocusability(ListView.FOCUS_AFTER_DESCENDANTS);
        // Set the adapter and ListView
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
     *
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
    }

    /**
     * Get the table data information from the content provider
     *
     * @param id - Loader number for the individual movie detail elements
     * @param args - Bundle elements
     * @return - Data for the individual cursors in loader
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (null != mUri) {
            // Initials Uri for Cursor Loader
            Uri allDetail;

            // Check the Uri either pointing to the favorite_movie table based on the preferred option
            // Or points to the movie table
            if(Utility.getPreferredMovieType(getContext()).equals("favorite_movie")){
                // Cursor Loader that point to MOVIE_DETAILS_FAVORITE which includes selected
                // favorite movie's, favorite  movie's review, favorite movie's trailer information
                allDetail = MovieContract.FavoriteMovies.buildFavoriteMovieDetailAllSection(
                        MovieContract.FavoriteMovies.getIntegerFavoriteMovieID(mUri));
            }else {
                // Cursor Loader that point to MOVIE_DETAILS_NORMAL which includes selection
                // movie, movie's review, and movie's trailer information
                allDetail = MovieContract.MovieEntry.buildMovieDetailAllSection(
                        MovieContract.MovieEntry.getIntegerMovieID(mUri));
            }

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
     * Populates the individual movie adapter (movie details, trailer, and review)
     *  from the table cursor data
     *
     * @param loader - Current crsor loader
     * @param data - Retrieved table data in the cursor
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            // Add the new cursor data to the adapter
            mDetailMovieAdapter.swapCursor(data);
            mDetailMovieAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Reset the movie adapter for the individual adapter elements
     *
     * @param loader - Current cursor loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDetailMovieAdapter.swapCursor(null);
    }
}
