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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.data.MovieSyncUploader;
import com.example.andriod.popularmoviev2.data.MovieContract.MovieEntry;

/**
 * DetailMovieFragment (Detail Movie Fragment) shows general information
 * about the movie (DETAIL_MOVIE_COLUMNS)
 */
public class DetailMovieFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // Local class Log tag variable
    private final String LOG_TAG = DetailMovieFragment.class.getSimpleName();

    // String constent for the MovieDetailFragment
    static final String MOVIE_DETAIL_URI = "MOVIE_DETAILS_URI";

    // String constent for movie share hashtag
    private static final String MOVIE_SHARE_HASHTAG = " #MovieDisplayerApp";

    // ShareActionProvider for Movie Detail App
    private ShareActionProvider mShareActionProvider;

    // String that will display in share tag
    private String mMovieDetail;

    // Local Uri identify
    private Uri mUri;

    // Movie Detail Loader for DetailMovieFragment
    private static final int DETAIL_MOVIE_LOADER = 0;

    // LinearLayout for User Rating for Star
    LinearLayout mUserRatingLayout;

    // Movie String[] for Detail Fragment
    private static final String[] DETAIL_MOVIE_COLUMNS = {
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
            MovieEntry.COLUMN_VOTE_AVERAGE
    };

    // These indices are tied to MOVIE_COLUMNS. If MOVIE_COLUMNS change, these need change too
    static final int COL_DETAIL_ID = 0;
    static final int COL_DETAIL_MOVIE_ID = 1;
    static final int COL_DETAIL_MOVIE_POSTER_PATH = 2;
    static final int COL_DETAIL_MOVIE_ADULT = 3;
    static final int COL_DETAIL_MOVIE_OVERVIEW = 4;
    static final int COL_DETAIL_MOVIE_RELEASE_DATE = 5;
    static final int COL_DETAIL_MOVIE_GENRE_IDS = 6;
    static final int COL_DETAIL_MOVIE_ORIG_TITLE = 7;
    static final int COL_DETAIL_MOVIE_ORIG_LANGUAGE = 8;
    static final int COL_DETAIL_MOVIE_TITLE = 9;
    static final int COL_DETAIL_MOVIE_BACKDROP_PATH = 10;
    static final int COL_DETAIL_MOVIE_POPULARITY = 11;
    static final int COL_DETAIL_MOVIE_VOTE_COUNT = 12;
    static final int COL_DETAIL_MOVIE_VIDEO = 13;
    static final int COL_DETAIL_MOVIE_VOTE_AVERAGE = 14;
    static final int COL_DETAIL_MOVIE_TYPE = 15;

    // Set the local Movie Detail elements
    private ImageView mDetail_imageView;
    private TextView mDetail_titleTextView;
    private TextView mDetail_synopsisTextView;
    private TextView mDetail_userRateingTextView;
    private TextView mDetail_releaseDateTextView;
    private TextView mDetail_genreTextView;

    public DetailMovieFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(DetailMovieFragment.MOVIE_DETAIL_URI);
        }

        // Get the current DetailActivityFragment view layout
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Local variable for detail screen elements
        mDetail_imageView = (ImageView) rootView.findViewById(R.id.detail_imageView);
        mDetail_titleTextView = (TextView) rootView.findViewById(R.id.detail_titleTextView);
        mDetail_synopsisTextView = (TextView) rootView.findViewById(R.id.detail_synopsisTextView);
        mDetail_userRateingTextView = (TextView) rootView.findViewById(R.id.detail_UserRateingTextView);
        mDetail_releaseDateTextView = (TextView) rootView.findViewById(R.id.detail_releaseDateTextView);
        mDetail_genreTextView = (TextView) rootView.findViewById(R.id.detail_genreTextView);
        mUserRatingLayout = (LinearLayout) rootView.findViewById(R.id.detail_UserRateingLayout);

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
     * Remove first and end braskets
     * @param line - String of genre id with brasket
     * @return - String of genres id without braskets
     */
    public String getGenreName(String line){
        String result = "";
        result = line.substring(1, line.length()-1);
        return result;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.

            Log.v("Content URI ", MovieContract.MovieEntry.CONTENT_URI.toString());
            Log.v("URI ", mUri.toString());


            // Below code populations Review and Trailer, but continually to populate move then
            // Once. I going to need to look at this.
            MovieSyncUploader movieSyncUploader = new MovieSyncUploader(getContext(),false);

            // Get the Review & Trailer Information for this movie
            movieSyncUploader.getReviewInfor(Integer.parseInt(MovieContract.MovieEntry.getMovieID(mUri)));
            movieSyncUploader.getTrailerInfor(Integer.parseInt(MovieContract.MovieEntry.getMovieID(mUri)));

            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_MOVIE_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            // Create an instance of AQuery and set it to the movieView item
            AQuery aq = new AQuery(mDetail_imageView);

            // Get the post information from the curse (get the row/column of information
            // from the db)
            String poster = data.getString(COL_DETAIL_MOVIE_POSTER_PATH);

            // Take the ImageView and add an Image from the post location and
            // make it visible too
            // Replaced Picassa with AQery per the below form post. The image were loading to slow
            //so I looked and found a soluation (https://discussions.udacity.com/t/picassa-image-caching-and-loading/175512)
            aq.id(mDetail_imageView).image(poster).visible();

            // Get the TextView from the current layout and set the text
            // to what appears at position X in the column layout
            mDetail_titleTextView.setText(data.getString(COL_DETAIL_MOVIE_TITLE));
            mDetail_synopsisTextView.setText(data.getString(COL_DETAIL_MOVIE_OVERVIEW));
            mDetail_userRateingTextView.setText(data.getString(COL_DETAIL_MOVIE_VOTE_AVERAGE));

            double value = 0;

            // Loop through and populate the start images
            for(int i = 0; i < data.getDouble(COL_DETAIL_MOVIE_VOTE_AVERAGE);i++){
                ImageView starImages = new ImageView(getContext());
                starImages.setImageResource(R.drawable.ic_full_star);
                mUserRatingLayout.addView(starImages);
                value = (double) i;
                Log.v("Value ",Double.toString(value));
            }

            mDetail_releaseDateTextView.setText(data.getString(COL_DETAIL_MOVIE_RELEASE_DATE));
            mDetail_genreTextView.setText(getGenreName(data.getString(COL_DETAIL_MOVIE_GENRE_IDS)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    /**
     * Get the movie detail fragments Bundle string identiry
     * @return - Return the string used to find Uri information in bundle
     */
    public static String getMOVIE_DETAIL_URI(){
        return MOVIE_DETAIL_URI;
    }
}
