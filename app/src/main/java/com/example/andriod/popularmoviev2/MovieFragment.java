package com.example.andriod.popularmoviev2;


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
import android.widget.GridView;

import com.example.andriod.popularmoviev2.data.MovieContract;
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

    // Unique identify for they specific load
    private static final int MOVIE_LOADER = 0;

    // String[] of movie columns
    private static final String[] MOVIE_COLUMNS ={
            //MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            //MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            //MovieContract.MovieEntry.COLUMN_MOVIE_TYPE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH
    };

    // These indices are tied to MOVIE_COLUMNS. If MOVIE_COLUMNS change, these need change too
    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_IDp = 1;
    static final int COL_MOVIE_TYPE = 2;
    static final int COL_MOVIE_POSTER_PATH = 3;

    // Empty construction
    public MovieFragment() {}

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
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate all the items on the fragmen_main layout
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Initialize the custom movie adapter with necessary Curse adapter information
        movieAdapter = new MovieAdapter(getActivity(),null,0);

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

        /*
        // When one of the view on the GridView is click the below will happen
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l){
                // Set the local movie object to the item that was
                // selected on the GridView screen
                Movie onMovieClick  = movieAdapter.getItem(pos);

                // Create a specific Intent that will go to the DetailActivity
                // with the movie and string array of movie parts
                Intent detailIntent = new Intent(getActivity(),DetailActivity.class)
                        .putExtra("movie",onMovieClick);

                // Start the new activity (and pass the information along)
                startActivity(detailIntent);
            }
        });*/
        return  rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onStart(){
        MovieSyncUploader popularUpload = new MovieSyncUploader(getContext(),true);
        popularUpload.getPopularMovieColl();

        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Get an Uri for the current app setting either Popular Movie or Top Rate Movies
        Uri movieTypelistUri = MovieContract.MovieEntry.
                buildMovieList(Utility.getPreferredMovieType(getContext()));

        return new CursorLoader(getActivity(),
                movieTypelistUri,
                MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        for(int i = 0; i<data.getColumnCount();i++) {
            Log.v("Curse ", " _ID " + data.getColumnName(i));
        }

        movieAdapter.swapCursor(data);
        /*if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }*/
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }

}
