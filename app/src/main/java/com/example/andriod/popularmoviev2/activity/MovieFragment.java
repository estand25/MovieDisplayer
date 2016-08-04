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
import com.example.andriod.popularmoviev2.model.Movie;
import com.example.andriod.popularmoviev2.other.Constants;
import com.example.andriod.popularmoviev2.other.LastSelectedMovieType;
import com.example.andriod.popularmoviev2.other.Utility;
import com.example.andriod.popularmoviev2.service.ReviewInfoService;
import com.example.andriod.popularmoviev2.service.TrailerInfoService;

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
    public SwipeRefreshLayout mSwipeRefreshLayout;

    // These indices are tied to MOVIE_COLUMNS. If MOVIE_COLUMNS change, these need change too
    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_MOVIE_POSTER_PATH = 2;
    static final int COL_MOVIE_GENRE_IDS = 6;
    static final int COL_MOVIE_TITLE = 9;

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
     *
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.v("Create ","MovieFragment");

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            movieList = new ArrayList<>(new ArrayList<Movie>());
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("movies");
        }
    }

    /**
     * Sets the onSaveInstanceState with the movieList
     *
     * @param outState  - outState Bundle that live for the lifetime of activity
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.v("Create ","MovieFragment - onSaveInstanceState");

        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(Constants.SELECTED_KEY, mPosition);
            onMovieChanged();
        }

        // Add the movieList Array to the outState Bundle
        outState.putParcelableArrayList("movies",movieList);

        Log.v("Create ","MovieFragment - onSaveInstanceState Utility " + Utility.getPreferredMovieType(getContext()));
        Log.v("Create ","MovieFragment - onSaveInstanceState instance " + LastSelectedMovieType.getInstance().getStringKey());
        // Check if LastActivity is SettingActivity then refreshContent
        if(!LastSelectedMovieType.getInstance().getStringKey().equals(Utility.getPreferredMovieType(getContext()))){

            // Set the LastMovieType String
            LastSelectedMovieType.getInstance().setStringKey(Utility.getPreferredMovieType(getContext()));

            // Refresh using the SwipeRefreshLayout
            refreshContent();
        }
    }

    /**
     * Initial the gridView with the movieAdapter then update column number based on
     * size of the screen, and finally add onClickListener for individual gridView.
     * Also inflated main fragment layout
     *
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

        // Find the SwipeRefreshLayout and associate to local one
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);

        // Find the GridView on the fragment_main layout and set it to the
        // local representation
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        // Set-up gridView & gridView column
        setupAdapter();

        // When one of the view on the GridView is click the below will happen
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l){
                Log.v("Create"," OnItemClick");
                // Set the local movie object to the item that was
                // selected on the GridView screen
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(pos);

                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position
                if(cursor != null){

                    // Only get the Movie's reviews & trailers if the movie isn't in the favorites table
                    if(!Utility.getPreferredMovieType(getContext()).equals("favorite_movie")){
                        // Review Information Service for movie from The Movie DB API
                        getActivity().startService(new Intent(getActivity(), ReviewInfoService.class)
                                .putExtra(Constants.REVIEW,cursor.getInt(COL_MOVIE_ID)));

                        // Trailer Information Service for movie from the Movie DB API
                        getActivity().startService(new Intent(getContext(), TrailerInfoService.class)
                                .putExtra(Constants.TRAILER,cursor.getInt(COL_MOVIE_ID)));
                    }

                    // Route to onItemSelect in main activity
                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.MovieEntry.buildMovieIDUri(cursor.getInt(COL_MOVIE_ID)));
                }

                // Set the position of the gridView
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
            // swap out in onLoadFinished.
            mPosition = savedInstanceState.getInt(Constants.SELECTED_KEY);
        }

        // Set-up the SwipeRefreshLayout color order
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorBlack,R.color.colorGray,R.color.colorDarkGray);

        // Set-up the SwipeRefreshLayout pull-down response
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        // Return the view with the update stuff on it
        return  rootView;
    }

    /**
     * Handles the refresh functionality for the SwipRefreshLayout
     * I wasn't sure I could do this so I look around for a couple of examples
     * I found this one
     *
     * https://www.bignerdranch.com/blog/implementing-swipe-to-refresh/
     */
    public void refreshContent(){
        // The refresh handler for the SwipeRefreshLayout
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Reset the gridView location
                mPosition = 0;

                // Sync the Content Provide data with internal SQL db's
                onMovieChanged();

                // re-connect the adapter to the gridView
                setupAdapter();

                // Show the progress of the refresh per the color scheme above
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },250);// wait 25 seconds
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
        super.onActivityCreated(savedInstanceState);

        // Initials the loader
        getLoaderManager().initLoader(Constants.MOVIE_LOADER, null, this);
    }

    /**
     * On Movie change run the syncAdapter immediately
     */
    public void onMovieChanged(){
        // Add the adapter to the gridView and set-up the column depending on screen orientation
        setupAdapter();

        // Restart the loader
        getLoaderManager().restartLoader(Constants.MOVIE_LOADER, null, this);
    }

    /**
     * Get the Cursor from the loader
     *
     * @param i -
     * @param bundle - Bundle for fragment
     * @return - Return a loader specific cursor
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Initials the Uri & selection
        Uri uri;
        String selection = null;

        // Check the Uri either pointing to the favorite_movie table based on the preferred option
        // Or points to the movie table
        if(Utility.getPreferredMovieType(getContext()).equals("favorite_movie")){
            uri = MovieContract.FavoriteMovies.CONTENT_URI;
            selection = "favorite_movies.movie_type = ?";

        }else {
            uri = MovieContract.MovieEntry.CONTENT_URI;
            selection = "movie.movie_type = ?";
        }

        // return Cursor loader with the specific type of movie
        return new CursorLoader(
                getActivity(),
                uri,
                null,
                selection,
                new String[] {Utility.getPreferredMovieType(getContext())},
                null);
    }

    /**
     * When the retrieving of the movie is finished updating
     * the XML layout with the information
     *
     * @param loader - The loader the queries the movie content resolver
     * @param data - The cursor that is return from the loader and content resolver
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("Create ","onLoadFinished");
        Log.v("Create ","onLoadFinished - Utility " + Utility.getPreferredMovieType(getContext()));
        Log.v("Create ","onLoadFinished - instance " + LastSelectedMovieType.getInstance().getStringKey());
        // Check if we selected another movie type by checking our current preference against the
        // LastSelectedMovieType singleton string value while the LastSelectedMovieType is not empty
        if(!LastSelectedMovieType.getInstance().getStringKey().equals(Utility.getPreferredMovieType(getContext())) &&
                !LastSelectedMovieType.getInstance().getStringKey().isEmpty()){
            // setAdapter again & restart the loader for current preference
            onMovieChanged();

            // Reset the gridView location
            // mPosition = 0;

            // Set the LastSelectedMovieType after move to the new movie type
            LastSelectedMovieType.getInstance().setStringKey(Utility.getPreferredMovieType(getContext()));
        }

        // Add data to the adapter
        movieAdapter.swapCursor(data);

        // Notify the adapter about Data set change
        movieAdapter.notifyDataSetChanged();

        if (mPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            gridView.smoothScrollToPosition(mPosition);
        }
    }

    /**
     * When the Loader is reset the movieAdapter with populate null
     *
     * @param loader - the Cursor specific loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }

    /**
     * Set the layout for the gridView True - for tablet and false - for phone
     *
     * @param layout - the layout of the gridView
     */
    public void setLayout(boolean layout){
        Log.v("Create ","MovieFragment - setLayout");
        mTablet = layout;
    }
}
