package com.example.andriod.popularmoviev2.activity;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.data.MovieContract.ReviewEntry;
import com.example.andriod.popularmoviev2.data.MovieSyncUploader;


/**
 * DetailReviewFragment (Detail Review Fragment) showing
 * review information and icon
 */
public class DetailReviewFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Local class Log tag variable
    private final String LOG_TAG = DetailReviewFragment.class.getSimpleName();

    // String constant for the MovieReviewFragment
    static final String REVIEW_DETAIL_URI = "REVIEW_DETAILS_URI";

    // Local Uri identify
    private Uri mUri;

    // Review Loader for DetailReviewFragment
    private static final int REVIEW_MOVIE_LOADER = 2;

    // LinearLayout for Review dynamically create elements
    LinearLayout mReviewLayout;
    LinearLayout mReviewColumnLayout;

    // Review String[] for Detail Review Fragment
    private static final String[] REVIEW_MOVIE_COLUMNS = {
            ReviewEntry.TABLE_NAME + "." + ReviewEntry._ID,
            ReviewEntry.COLUMN_REVIEW_ID,
            ReviewEntry.COLUMN_MOVIE_ID,
            ReviewEntry.COLUMN_AUTHOR,
            ReviewEntry.COLUMN_CONTENT,
            ReviewEntry.COLUMN_URL
    };

    // There indices are tied to REVIEW_COLUMNS. If REVIEW_COLUMNS change, these need change too
    static final int COL_ID = 0;
    static final int COL_REVIEW_ID = 1;
    static final int COL_REVIEW_MOVIE_ID = 2;
    static final int COL_REVIEW_AUTHOR = 3;
    static final int COL_REVIEW_CONTENT = 4;
    static final int COL_REVIEW_URL = 5;

    // Set the local Review Detail elements
    private Button mReview_button;
    private TextView mReview_Author;
    private TextView mReview_Content;
    private TextView mReview_URL;


    MovieSyncUploader movieSyncUploader;

    // DetailReviewFragment Construction
    public DetailReviewFragment() {}

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
            mUri = arguments.getParcelable(DetailReviewFragment.REVIEW_DETAIL_URI);
        }

        // Get the current DetailReviewFragment view layout
        View rootView = inflater.inflate(R.layout.fragment_detail_review, container, false);

        // Local variable for review Layout
        mReviewColumnLayout = (LinearLayout) rootView.findViewById(R.id.reviewColumnLayout);
        mReviewLayout = (LinearLayout) rootView.findViewById(R.id.reviewLayout);

        // Return newly set view
        return rootView;
    }

    /**
     * Start the loading on the movie review records
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(REVIEW_MOVIE_LOADER, null, this);
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

            Log.v("Review Content URI ", MovieContract.ReviewEntry.CONTENT_URI.toString());
            Log.v("Review URI ", mUri.toString());


            movieSyncUploader = new MovieSyncUploader(getContext(), false);
            movieSyncUploader.getReviewInfor(Integer.parseInt(mUri.getPathSegments().get(1)));

            return new CursorLoader(
                    getActivity(),
                    mUri,
                    REVIEW_MOVIE_COLUMNS,
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        if(data != null && data.moveToNext()){

            Log.v("Row Count ", Integer.toString(data.getCount()));

            Log.v("Review Count ", Integer.toString(mReviewLayout.getChildCount()));

            // Set the record local in table is less then zero
            // Note: So when we go into the loop we start at the
            //       top of the tables
            data.moveToPosition(-1);

            // Loop through adding new reviews
            while(data.moveToNext()){
                // Create new instance of TextView for author infor
                mReview_Author = new TextView(getContext());
                // Set the text label for author
                mReview_Author.setText(data.getString(COL_REVIEW_AUTHOR));
                mReview_Author.setBackgroundResource(R.drawable.ic_entypo);
                // Adding the Author name to the reviewColumnLayout
                mReviewColumnLayout.addView(mReview_Author);

                // Create new instance of TextView for content infor
                mReview_Content = new TextView(getContext());
                // Set the text label for content
                mReview_Content.setText(data.getString(COL_REVIEW_CONTENT));
                // Adding the Content to the reviewColumnLayout
                mReviewColumnLayout.addView(mReview_Content);

                // Add the new review entry into the outter layout reviewLayout
                mReviewLayout.addView(mReviewColumnLayout);
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
    public static String getREVIEW_DETAILS_URI(){
        return REVIEW_DETAIL_URI;
    }

}