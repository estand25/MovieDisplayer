package com.example.andriod.popularmoviev2.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.data.MovieContract.TrailerEntry;
import com.example.andriod.popularmoviev2.data.MovieSyncUploader;

/**
 * DetailTrailerFragment (Detail Trailer Fragment) showing
 * trailer information and icon
 */
public class DetailTrailerFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Local class Log tag variable
    private final String LOG_TAG = DetailTrailerFragment.class.getSimpleName();

    // String constant for the MovieTrailerFragment
    static final String TRAILER_DETAILS_URI = "TRAILER_DETAILS_URI";

    // Local Uri identify
    private Uri mUri;

    // Trailer Loader for DetailTrailerFragment
    private static final int TRAILER_MOVIE_LOADER = 1;

    // LinearLayout for Trailer dynamically create elements
    LinearLayout mTrailerLayout;

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

    // There indices are tied to TRAILER_COLUMNS. If TRAILER_COLUMNS change, these need change too
    static final int COL_ID = 0;
    static final int COL_TRAILER_ID = 1;
    static final int COL_TRAILER_MOVIE_ID = 2;
    static final int COL_MOVIE_TITLE = 3;
    static final int COL_TRAILER_ISO_6391 = 4;
    static final int COL_TRAILER_ISO_31661 = 5;
    static final int COL_TRAILER_KEY = 6;
    static final int COL_TRAILER_NAME = 7;
    static final int COL_TRAILER_SITE = 8;
    static final int COL_TRAILER_SIZE = 9;
    static final int COL_TRAILER_TYPE = 10;

    // Set the local Trailer Detail element
    private Button mTrailer_button;

    // DetailTrailerFragment Construction
    public DetailTrailerFragment() {}

    /**
     * When the View is created I get the Bundle argument with the movie Uri
     *
     * I inflate the fragment layout
     *
     * @param inflater - inflater the declare layout elements
     * @param container - Get the container for the inflater
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     * @return - Return the populate movie view to the app
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(DetailTrailerFragment.TRAILER_DETAILS_URI);
        }

        // Get the current DetailTrailerFragment view layout
        View rootView = inflater.inflate(R.layout.fragment_detail_trailer, container, false);

        // Local variable for trailer Layout
        mTrailerLayout = (LinearLayout) rootView.findViewById(R.id.trailerLayout);

        // Return newly set view
        return rootView;
    }

    /**
     * Start the loading on the movie trailer records
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(TRAILER_MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Get the Cursor from the loader
     * @param id -
     * @param args - Bundle for fragment
     * @return - Retrun a loader specific cursor
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.

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
        return null;
    }

    /**
     * When the retrieving of the movie trailers are finished updating
     * the XML layout with the movie trailers information
     * @param loader - The loader the queries the movie content resolver
     * @param data - The cursor that is retrun from the loader and content resolver
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()) {

            Log.v("Row Count ", Integer.toString(data.getCount()));

            Log.v("Trailer Count ", Integer.toString(mTrailerLayout.getChildCount()));

            // Check if cursor data records is greater then or equal too
            // the number of trailer child elements
            if (data.getCount() >= mTrailerLayout.getChildCount()) {

                // Set the record local in table is less then zero
                // Note: So when we go into the loop we start at the
                //       top of the tables
                data.moveToPosition(-1);

                // Loop through adding new trailers
                while (data.moveToNext()) {
                    // Set the button label text
                    String trailerLabel = data.getString(COL_MOVIE_TITLE) + " " + data.getString(COL_TRAILER_NAME);

                    // Set the video key to pass to YouTube
                    final String video_id = data.getString(COL_TRAILER_KEY);

                    // Initize the class level button to a new button
                    mTrailer_button = new Button(getContext());

                    // Set the background image for the button
                    mTrailer_button.setBackgroundResource(R.drawable.ic_entypo);

                    // Set the text for the button
                    mTrailer_button.setText(trailerLabel);

                    // Set-up the onClickListener for the button push key to
                    // trigger youtube
                    mTrailer_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Try to open up youtube with trailer video directly, but if not able
                            // I open an internet browser
                            // Note: I embedded this code section based on the stackoverflow post
                            // it was a lot better then what I was thinking of doing
                            // http://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video_id));
                                startActivity(intent);
                            }catch (ActivityNotFoundException ex){
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://www.youtube.com/watch?v=" + video_id));
                                startActivity(intent);
                            }
                        }
                    });

                    // Add the new button to my trailer Layout
                    mTrailerLayout.addView(mTrailer_button);

                }
            }
        }
    }

    /**
     * Reset the loader cursor (I do not with it)
     * @param loader - The loader the queries the movie content resolver
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    /**
     * Get the movie trailer detail fragments Bundle string identiry
     * @return - Return the string used to find Uri information in bundle
     */
    public static String getTRAILER_DETAILS_URI(){
        return TRAILER_DETAILS_URI;
    }

}
