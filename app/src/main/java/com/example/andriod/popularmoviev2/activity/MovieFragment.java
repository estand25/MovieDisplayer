package com.example.andriod.popularmoviev2.activity;


import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.data.MovieTableSync;
import com.example.andriod.popularmoviev2.model.Movie;
import com.example.andriod.popularmoviev2.other.Constants;
import com.example.andriod.popularmoviev2.service.GenreInfoService;
import com.example.andriod.popularmoviev2.service.ReviewInfoService;
import com.example.andriod.popularmoviev2.service.TrailerInfoService;
import com.example.andriod.popularmoviev2.sync.MovieSyncAdapter;

import java.util.ArrayList;

/**
 * MovieFragment (Movie Fragment) shows individual movie poster
 * on gridView
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    protected final String TAG = getClass().getSimpleName();
    private GridView gridView;

    // Declare the following class level variables
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList;
    private int mPosition = gridView.INVALID_POSITION;
    private boolean mTablet;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    // These indices are tied to MOVIE_COLUMNS. If MOVIE_COLUMNS change, these need change too
    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_MOVIE_POSTER_PATH = 2;
    static final int COL_MOVIE_GENRE_IDS = 6;
    static final int COL_MOVIE_TITLE = 9;

    // Create the local copy of movieSyncUploader
    MovieTableSync movieTableSync;
    /**
     * Empty construction
     */
    public MovieFragment() {}

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailMovieFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    /**
     * OnCreate I will use the saveInstanceState to check
     * if saveInstanceState already has something or if we are starting anew
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            movieList = new ArrayList<>(new ArrayList<Movie>());
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("movies");
        }
        Log.v("Create ","MovieFragment");
    }

    /**
     * Sets the onSaveInstanceState with the movieList
     * @param outState  - outState Bundle that live for the lifetime of activity
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        Log.v("Create ","MovieFragment - onSaveInstanceState");
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(Constants.SELECTED_KEY, mPosition);
        }
        outState.putParcelableArrayList("movies",movieList);
        super.onSaveInstanceState(outState);
    }

    /**
     * Initiaze the gridView with the movieAdapter then update column number based on
     * size of the screen, and finally add onClickListner for individual gridView.
     * Also inflated main fragment layout
     * @param inflater - flater the declare layout elements
     * @param container - Get the container for the inflater
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     * @return - Return the populate movie view to the app
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("Create ","MovieFragment - onCreateView");

        // Initialize the custom movie adapter with necessary Curse adapter information
        movieAdapter = new MovieAdapter(getActivity(),null,0);

        // Inflate all the items on the fragment_main layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);

        // Find the GridView on the fragment_main layout and set it to the
        // local representation
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        // Set-up gridView & gridView column
        setupAdapter();

        // Set-up the SwipeRefreshLayout color order
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorDarkGray,R.color.colorBlack,R.color.colorLTGray);
        // Set-up the SwipeRefreshLayout pull-down response
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Sync the Content Provide data with internal SQL db's
                        MovieSyncAdapter.syncImmediately(getActivity());
                        // re-connect the adapter to the gridView
                        setupAdapter();
                        // Show the progress of the refresh per the color scheme above
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                },250);// wait 25 seconds
            }
        });

        // When one of the view on the GridView is click the below will happen
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l){
                // Set the local movie object to the item that was
                // selected on the GridView screen
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(pos);

                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position
                if(cursor != null){
                    // Set up the MovieSyncUploader to populate the information
                    movieTableSync = new MovieTableSync(getContext());

                    // Populate the movie detail information
                    movieTableSync.chkFavoriteMovie(cursor.getInt(COL_MOVIE_ID));

                    // Review Information Service for movie from The Movie DB API
                    getActivity().startService(new Intent(getActivity(), ReviewInfoService.class)
                            .putExtra(Constants.REVIEW,cursor.getInt(COL_MOVIE_ID)));

                    // Trailer Information Service for movie from the Movie DB API
                    getActivity().startService(new Intent(getContext(), TrailerInfoService.class)
                            .putExtra(Constants.TRAILER,cursor.getInt(COL_MOVIE_ID)));

                    // Route to onItemSelect in main activity
                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.MovieEntry.buildMovieIDUri(cursor.getInt(COL_MOVIE_ID)));
                }
                mPosition = pos;
            }
        });

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.SELECTED_KEY)) {
            // The gradView probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(Constants.SELECTED_KEY);
        }
        return  rootView;
    }

    /**
     * Set-up Adapter to GridView and set-up column number based on device rotation
     */
    private void setupAdapter(){
        //  Populate the GridView with the custom adapter information
        gridView.setAdapter(movieAdapter);

        // Check if device is landscape or portrait I got this working, but find a good post about it
        // so I add it as a reference just in case it can be used later
        // http://stackoverflow.com/questions/3674933/find-out-if-android-device-is-portrait-or-landscape-for-normal-usage
        if(Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation){
            gridView.setNumColumns(3);
        }else{
            gridView.setNumColumns(2);
        }
    }

    /**
     * Populate the movie table with either the populor or top rate movie from
     * The Movie DB API after the activity has been created
     *
     * Start the loading of movie items
     *
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v("Create ","MovieFragment - onActivityCreated");
        getLoaderManager().initLoader(Constants.MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * On Movie change run the syncAdapter immediately
     */
    void onMovieChanged(){
        Log.v("Create ","MovieFragment - onMovieChanged");
        MovieSyncAdapter.syncImmediately(getActivity());
        getLoaderManager().restartLoader(Constants.MOVIE_LOADER, null, this);
    }

    /**
     * Get the Cursor from the loader
     * @param i -
     * @param bundle - Bundle for fragment
     * @return - Retrun a loader specific cursor
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v("Create ","MovieFragment - onCreateLoader");
        // return Cursor loader with all th movie poster images
        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    /**
     * When the retrieving of the movie is finished updating
     * the XML layout with the information
     * @param loader - The loader the queries the movie content resolver
     * @param data - The cursor that is retrun from the loader and content resolver
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("Create ","MovieFragment - onLoadFinished");
        if (mPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            gridView.smoothScrollToPosition(mPosition);
        }
        movieAdapter.swapCursor(data);
    }

    /**
     * When the Loader is reset the movieAdapter with populate null
     * @param loader - the Cursor specific loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v("Create ","MovieFragment - onLoaderReset");
        movieAdapter.swapCursor(null);
    }

    /**
     * Set the layout for the gridview
     * True - for tablet and false - for phone
     * @param layout - the layout of the gridview
     */
    public void setLayout(boolean layout){
        Log.v("Create ","MovieFragment - setLayout");
        mTablet = layout;
    }
}
