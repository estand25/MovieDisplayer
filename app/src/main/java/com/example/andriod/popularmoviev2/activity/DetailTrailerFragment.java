package com.example.andriod.popularmoviev2.activity;

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
    static final String TRAILER_DETAILER_URI = "TRAILER_DETAILS_URI";

    // Local Uri indentify
    private Uri mUri;

    // Trailer Loader for DetailTrailerFragment
    private static final int TRAILER_MOVIE_LOADER = 1;

    // LinearLayout for User Rating for Star
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

    // Set the local Trailer Detail elements
    private ImageView mMovie_trailer_image;
    private TextView mMovie_trailer_title;
    private Button mTrailer_button;

    public DetailTrailerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(DetailTrailerFragment.TRAILER_DETAILER_URI);
        }

        // Get the current DetailTrailerFragment view layout
        View rootView = inflater.inflate(R.layout.fragment_detail_trailer, container, false);

        // Local Variable for trailer screen elements
        mMovie_trailer_image = (ImageView) rootView.findViewById(R.id.movie_trailer_image);
        mMovie_trailer_title = (TextView) rootView.findViewById(R.id.movie_trailer_title);
        mTrailerLayout = (LinearLayout) rootView.findViewById(R.id.trailerLayout);
        mTrailer_button = (Button) rootView.findViewById(R.id.trailer_button);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(TRAILER_MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.

            Log.v("Content URI ", TrailerEntry.CONTENT_URI.toString());
            Log.v("URI ", mUri.toString());

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


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){

            Log.v("Row Count ",Integer.toString(data.getCount()));

            data.moveToPosition(-1);

            // Loop throught adding new trailers
            while (data.moveToNext()){
                String trailerLabel = data.getString(COL_MOVIE_TITLE) + " " + data.getString(COL_TRAILER_NAME);

                final String video_id = data.getString(COL_TRAILER_KEY);
                /*
                mMovie_trailer_image = new ImageView(getContext());
                mMovie_trailer_image.setImageResource(R.drawable.ic_entypo);

                mMovie_trailer_title = new TextView(getContext());
                mMovie_trailer_title.setText(trailerLabel);

                mTrailerLayout.addView(mMovie_trailer_title);
                mTrailerLayout.addView(mMovie_trailer_image);

                // Note: I got the hint on how to make this work from this post
                // http://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
                mMovie_trailer_image.setOnClickListener(new View.OnCreateContextMenuListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+video_id));
                        //intent.setAction(Intent.ACTION_VIEW);
                        //intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        //intent.setData(Uri.parse("http://casidiablo.net"));
                        startActivity(intent);
                    }
                }); */

                mTrailer_button = new Button(getContext());
                mTrailer_button.setBackgroundResource(R.drawable.ic_entypo);
                mTrailer_button.setText(trailerLabel);
                mTrailer_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+video_id));
                        startActivity(intent);
                    }
                });

                mTrailerLayout.addView(mTrailer_button);

            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    /**
     * Get the movie trailer detail fragments Bundle string identiry
     * @return - Return the string used to find Uri information in bundle
     */
    public static String getTRAILER_DETAILER_URI(){
        return TRAILER_DETAILER_URI;
    }

}
