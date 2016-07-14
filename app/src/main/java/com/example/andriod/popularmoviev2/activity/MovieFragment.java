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
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.data.MovieProvider;
import com.example.andriod.popularmoviev2.data.MovieSyncUploader;
import com.example.andriod.popularmoviev2.model.Movie;
import com.example.andriod.popularmoviev2.other.Utility;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    protected final String TAG = getClass().getSimpleName();
    private GridView gridView;

    // Declare the following class level variables
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList;
    private int mPosition = gridView.INVALID_POSITION;
    //private boolean mUseTodayLayout;

    private static final String SELECTED_KEY = "selected_position";

    // Unique identify for they specific load
    private static final int MOVIE_LOADER = 0;

    // String[] of movie columns
    private static final String[] MOVIE_COLUMNS ={
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_TYPE
    };

    // These indices are tied to MOVIE_COLUMNS. If MOVIE_COLUMNS change, these need change too
    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 1;
    static final int COL_MOVIE_POSTER_PATH = 2;
    static final int COL_MOVIE_TITLE = 3;
    static final int COL_MOVIE_TYPE = 4;

    // Empty construction
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

    // OnCreate I will use the saveInstanceState to check
    // if saveInstanceState already has something or if we are starting
    // anew
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){
            movieList = new ArrayList<Movie>(new ArrayList<Movie>());
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("movies");
        }
    }

    // Sets the onSaveInstanceState with the movieList
    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList("movies",movieList);

        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        /*if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }*/
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize the custom movie adapter with necessary Curse adapter information
        movieAdapter = new MovieAdapter(getActivity(),null,0);

        // Inflate all the items on the fragmen_main layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //  Find the GridView on the fragment_main layout and set it to the
        // local representize
        gridView = (GridView) rootView.findViewById(R.id.gridView);

        //  Populare the GridView with the custom adapter information
        gridView.setAdapter(movieAdapter);

        // Got this code section from
        // http://stackoverflow.com/questions/6912922/android-how-does-gridview-auto-fit-find-the-number-of-columns/7874011#7874011
        // I need a way to not alway default the column number to 2 or hard code it 3 or 4.
        // the below code does that for me
        // Note: This works even better because I'm using both the virtual phone & my tablet
        float scalefactor = getResources().getDisplayMetrics().density*100;
        int number = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int columns = (int) ((float) number /scalefactor)/2;
        if(columns == 0 || columns == 1){
            columns = 2;
        }

        // Set the number of columns
        gridView.setNumColumns(columns);

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

                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.MovieEntry.buildMovieIDUri(cursor.getInt(COL_MOVIE_ID)));
                }
                //mPosition = pos;
            }
        });
        /*
        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mForecastAdapter.setUseTodayLayout(mUseTodayLayout);*/

        return  rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onStart(){
        // Instance of Movie Provide
        //MovieProvider movieProvider = new MovieProvider();

        // Check if movie are already populated
        //if(!movieProvider.chkMovieExist()) {
            MovieSyncUploader populateUpload = new MovieSyncUploader(getContext(), true);

            // Check which display option is being used  and display the information
            // and populate the database with the selections information
            if (Utility.getPreferredMovieType(getContext()).equals("movie/popular")) {
                populateUpload.getPopularMovieColl();
            } else {
                populateUpload.getTopRateMovieColl();
            }
            super.onStart();
        //}
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Get an Uri for the current app setting either Popular Movie or Top Rate Movies
        Uri movieTypelistUri = MovieContract.MovieEntry.
                buildMovieList(Utility.getPreferredMovieType(getContext()));

        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieAdapter.swapCursor(data);

        /*if (mPosition != GridView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            gridView.smoothScrollToPosition(mPosition);
        }*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {movieAdapter.swapCursor(null);}

    /*public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
        if (movieAdapter != null) {
            movieAdapter.setUseTodayLayout(mUseTodayLayout);
        }
    }*/
}
