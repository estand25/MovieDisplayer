package com.example.andriod.popularmoviev2.activity;

import android.content.Context;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.data.MovieProvider;

import java.util.Date;

/**
 * Detail Movie Adapter for the individual general movie details
 * 
 * Created by StandleyEugene on 7/17/2016.
 */
public class DetailMovieAdapter extends CursorAdapter {
    private static final String LOG_TAG = DetailMovieAdapter.class.getSimpleName();

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

    /**
     * MovieAdapter constructor the set-up outside stuff inside
     * @param context - The current app context
     * @param cursor - The current app cursor
     * @param flags - The current flag
     */
    public DetailMovieAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);
    }

    /**
     * Class the holds the View elements
     */
    public static class ViewHolder{
        // Set the local Movie Detail elements
        public final ImageView mDetail_imageView;
        public final TextView mDetail_titleTextView;
        public final TextView mDetail_synopsisTextView;
        public final TextView mDetail_userRateingTextView;
        public final TextView mDetail_releaseDateTextView;
        public final TextView mDetail_genreTextView;
        public final LinearLayout mUserRatingLayout;

        /**
         * ViewHolder Constructor the binds the public layout elements to the ViewHolder object
         * @param view - The current view for the layout elements
         */
        public ViewHolder(View view){
            // Local variable for detail screen elements
            mDetail_imageView = (ImageView) view.findViewById(R.id.detail_imageView);
            mDetail_titleTextView = (TextView) view.findViewById(R.id.detail_titleTextView);
            mDetail_synopsisTextView = (TextView) view.findViewById(R.id.detail_synopsisTextView);
            mDetail_userRateingTextView = (TextView) view.findViewById(R.id.detail_UserRateingTextView);
            mDetail_releaseDateTextView = (TextView) view.findViewById(R.id.detail_releaseDateTextView);
            mDetail_genreTextView = (TextView) view.findViewById(R.id.detail_genreTextView);
            mUserRatingLayout = (LinearLayout) view.findViewById(R.id.detail_UserRateingLayout);
        }
    }

    /**
     * Inflators the movie_item layout and setting the viewHolder to the current view tags
     * @param context - Current context for view
     * @param cursor - Current cursor information
     * @param parent - ViewGroup of the view
     * @return = Inflated view with viewholder tags
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        // Inflate the movie_item (view in the grid
        View view = LayoutInflater.from(context).inflate(R.layout.movie_detail_item, parent, false);

        // Create an stance of View Holder and get the Tags for the display items
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        // Return the view with all its stuff
        return view;
    }

    /**
     * Populate the view with Cursor retrieved data
     * @param view - Current view information
     * @param context - Current context for view
     * @param cursor - Current cursor information
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor){
        // Set the local viewHolder with the previous tag information
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Create an instance of AQuery and set it to the movieView item
        AQuery aq = new AQuery(viewHolder.mDetail_imageView);

        // Get the post information from the curse (get the row/column of information
        // from the db)
        String poster = cursor.getString(COL_DETAIL_MOVIE_POSTER_PATH);

        // Take the ImageView and add an Image from the post location and
        // make it visible too
        // Replaced Picassa with AQery per the below form post. The image were loading to slow
        //so I looked and found a soluation (https://discussions.udacity.com/t/picassa-image-caching-and-loading/175512)
        aq.id(viewHolder.mDetail_imageView).image(poster).visible();

        /// Get the TextView from the current layout and set the text
        // to what appears at position X in the column layout
        viewHolder.mDetail_titleTextView.setText(cursor.getString(COL_DETAIL_MOVIE_TITLE));
        viewHolder.mDetail_synopsisTextView.setText(cursor.getString(COL_DETAIL_MOVIE_OVERVIEW));

        Log.v("Stars ",cursor.getString(COL_DETAIL_MOVIE_VOTE_AVERAGE));

        // Create previous star display then add new rating number then stars
        viewHolder.mUserRatingLayout.removeAllViews();

        // Add the user rating scores to Textview element
        viewHolder.mDetail_userRateingTextView.setText(cursor.getString(COL_DETAIL_MOVIE_VOTE_AVERAGE));

        // Add the user rating TextView to the user rating layout
        viewHolder.mUserRatingLayout.addView(viewHolder.mDetail_userRateingTextView);

        // Loop through and populate the start images
        for(int i = 0; i < cursor.getInt(COL_DETAIL_MOVIE_VOTE_AVERAGE);i++){
            ImageView starImages = new ImageView(context);
            starImages.setImageResource(R.drawable.star);
            viewHolder.mUserRatingLayout.addView(starImages);
            //center_vertical 16 and Left 3
            viewHolder.mUserRatingLayout.setVerticalGravity(16|3);
            Log.v("Stars created ",Integer.toString(i));
        }

        viewHolder.mDetail_releaseDateTextView.setText(cursor.getString(COL_DETAIL_MOVIE_RELEASE_DATE));
        viewHolder.mDetail_genreTextView.setText(cursor.getString(COL_DETAIL_MOVIE_GENRE_IDS));
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
}
