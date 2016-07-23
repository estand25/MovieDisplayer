package com.example.andriod.popularmoviev2.activity;

import android.content.Context;
import android.database.Cursor;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.data.MovieProvider;
import com.example.andriod.popularmoviev2.other.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

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

    // There indices are tied to TRAILER_COLUMNS. If TRAILER_COLUMNS change, these need change too
    static final int COL_TRAILER__ID = 0;
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

    // There indices are tied to REVIEW_COLUMNS. If REVIEW_COLUMNS change, these need change too
    static final int COL_REVIEW__ID = 0;
    static final int COL_REVIEW_ID = 1;
    static final int COL_REVIEW_MOVIE_ID = 2;
    static final int COL_REVIEW_AUTHOR = 3;
    static final int COL_REVIEW_CONTENT = 4;
    static final int COL_REVIEW_URL = 5;

    private static final int VIEW_TYPE_MOVIE_DETAIL = 0;
    private static final int VIEW_TYPE_MOVIE_REVIEWS = 1;
    private static final int VIEW_TYPE_MOVIE_TRAILER = 2;
    private static final int VIEW_TYPE_COUNT = 3;

    private ArrayList<Object> mObject = new ArrayList<Object>();

    // String constent for the MovieDetailFragment
    static final String MOVIE_DETAIL = "MOVIE_DETAIL";
    static final String REVIEW_DETAIL = "REVIEW_DETAIL";
    static final String TRAILER_DETAIL = "TRAILER_DETAIL";

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
     * Inflators the movie_item layout and setting the viewHolder to the current view tags
     * @param context - Current context for view
     * @param cursor - Current cursor information
     * @param parent - ViewGroup of the view
     * @return = Inflated view with viewholder tags
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        // Choose the layout type
        int viewType = getItemViewType(getItemViewType(cursor));
        int layoutid = -1;

        // Switch to the correct layout based on the viewtype
        switch(viewType){
            case VIEW_TYPE_MOVIE_DETAIL:{
                layoutid = R.layout.movie_detail_item;
                break;
            }
            case VIEW_TYPE_MOVIE_REVIEWS:{
                layoutid = R.layout.review_item;
                break;
            }
            case VIEW_TYPE_MOVIE_TRAILER:{
                layoutid = R.layout.trailer_item;
                break;
            }
        }

        // Inflate the movie_item (view in the grid
        View view = LayoutInflater.from(context).inflate(layoutid, parent, false);

        // Switch to the correct viewHolder based on the viewtype
        switch(viewType){
            case VIEW_TYPE_MOVIE_DETAIL:{
                // Create new instance of Detail Movie Section Holder
                ViewHolder.DetailMovieViewHolder detailMovieViewHolder = new ViewHolder.DetailMovieViewHolder(view);

                // Get the Tags for the display items
                view.setTag(detailMovieViewHolder );
                break;
            }
            case VIEW_TYPE_MOVIE_REVIEWS:{
                // Create new instance of Detail Movie Section Holder
                ViewHolder.ReviewViewHolder reviewViewHolder = new ViewHolder.ReviewViewHolder(view);
                // Get the Tags for the display items
                view.setTag(reviewViewHolder);
                break;
            }
            case VIEW_TYPE_MOVIE_TRAILER:{
                // Create new instance of Detail Movie Section Holder
                ViewHolder.TrailerViewHolder trailerViewHolder = new ViewHolder.TrailerViewHolder(view);
                // Get the Tags for the display items
                view.setTag(trailerViewHolder);
                break;
            }
        }

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

        // Determine which ViewType I should be using
        int viewType = getItemViewType(getItemViewType(cursor));

        // Switch to the correct viewHolder based on the viewtype
        switch(viewType){
            case VIEW_TYPE_MOVIE_DETAIL:{

                // Set the local viewHolder with the previous tag information
                ViewHolder.DetailMovieViewHolder detailMovieHolder = (ViewHolder.DetailMovieViewHolder) view.getTag();

                if(cursor != null && detailMovieHolder.mDetail_imageView.getDrawable() == null) {
                    // Create an instance of AQuery and set it to the movieView item
                    AQuery aq = new AQuery(detailMovieHolder.mDetail_imageView);

                    // Get the post information from the curse (get the row/column of information
                    // from the db)
                    String poster = cursor.getString(COL_DETAIL_MOVIE_POSTER_PATH);

                    // Take the ImageView and add an Image from the post location and
                    // make it visible too
                    // Replaced Picassa with AQery per the below form post. The image were loading to slow
                    //so I looked and found a soluation (https://discussions.udacity.com/t/picassa-image-caching-and-loading/175512)
                    aq.id(detailMovieHolder.mDetail_imageView).image(poster).visible();

                    /// Get the TextView from the current layout and set the text
                    // to what appears at position X in the column layout
                    detailMovieHolder.mDetail_titleTextView.setText(cursor.getString(COL_DETAIL_MOVIE_TITLE));
                    detailMovieHolder.mDetail_synopsisTextView.setText(cursor.getString(COL_DETAIL_MOVIE_OVERVIEW));

                    Log.v("Stars ", cursor.getString(COL_DETAIL_MOVIE_VOTE_AVERAGE));

                    // Create previous star display then add new rating number then stars
                    detailMovieHolder.mUserRatingLayout.removeAllViews();

                    // Add the user rating scores to Textview element
                    detailMovieHolder.mDetail_userRateingTextView.setText(cursor.getString(COL_DETAIL_MOVIE_VOTE_AVERAGE));

                    // Add the user rating TextView to the user rating layout
                    detailMovieHolder.mUserRatingLayout.addView(detailMovieHolder.mDetail_userRateingTextView);

                    // Loop through and populate the start images
                    for (int i = 0; i < cursor.getInt(COL_DETAIL_MOVIE_VOTE_AVERAGE); i++) {
                        ImageView starImages = new ImageView(context);
                        starImages.setImageResource(R.drawable.star);
                        detailMovieHolder.mUserRatingLayout.addView(starImages);
                        //center_vertical 16 and Left 3
                        detailMovieHolder.mUserRatingLayout.setVerticalGravity(16 | 3);
                        Log.v("Stars created ", Integer.toString(i));
                    }
                    detailMovieHolder.mDetail_releaseDateTextView.setText(cursor.getString(COL_DETAIL_MOVIE_RELEASE_DATE));
                    detailMovieHolder.mDetail_genreTextView.setText(cursor.getString(COL_DETAIL_MOVIE_GENRE_IDS));
                }
            }
            case VIEW_TYPE_MOVIE_REVIEWS:{
                // Set the local viewholder with the previous tag information
                ViewHolder.ReviewViewHolder reviewHolder = (ViewHolder.ReviewViewHolder) view.getTag();

                // Set update the Review Card View with the Author and Content information
                reviewHolder.mReview_Authoer.setText(cursor.getString(COL_REVIEW_AUTHOR) + " ");
                reviewHolder.mReview_Content.setText(cursor.getString(COL_REVIEW_CONTENT));

            }
            case VIEW_TYPE_MOVIE_TRAILER:{
                // Set the local viewHolder with the previous tag information
                ViewHolder.TrailerViewHolder trailerHolder = (ViewHolder.TrailerViewHolder) view.getTag();

                // Set update the Trailer Card View with the Trailer Icon and Name
                trailerHolder.mTrailerIcon.setImageResource(R.drawable.ic_entypo);
                trailerHolder.mTrailerName.setText(cursor.getString(COL_MOVIE_TITLE) + " " +cursor.getString(COL_TRAILER_NAME));
            }
        }
    }

    /**
     * Get the item view type based on the position
     * @param position - The section location
     * @return - Returns the integer representation for the section
     */
    @Override
    public int getItemViewType(int position){
        if(position == 0){
            return VIEW_TYPE_MOVIE_DETAIL;
        }else if(position == 1){
            return VIEW_TYPE_MOVIE_REVIEWS;
        }else if(position == 2){
            return VIEW_TYPE_MOVIE_TRAILER;
        }else {
            return -1;
        }
    }

    /**
     * Get the Cursor Loader number for the associated Layout
     * @param cursor - The cursor with the record information
     * @return - Returns integer number for a section
     */
    public int getItemViewType(Cursor cursor){
        if(cursor.getColumnName(COL_DETAIL_MOVIE_ADULT).contains("adult")){
            //Bundle arg = cursor.getExtras().getBundle(MOVIE_DETAIL);
            //return arg.getInt(MOVIE_DETAIL);
            return cursor.getExtras().getInt(MOVIE_DETAIL);
        }else if(cursor.getColumnName(COL_REVIEW_ID).contains("review_id")){
            //Bundle arg = cursor.getExtras().getBundle(REVIEW_DETAIL);
            //return arg.getInt(REVIEW_DETAIL);
            return cursor.getExtras().getInt(REVIEW_DETAIL);
        }else if(cursor.getColumnName(COL_TRAILER_ID).contains("trailer_id")){
            //Bundle arg = cursor.getExtras().getBundle(TRAILER_DETAIL);
            //return arg.getInt(TRAILER_DETAIL);
            return cursor.getExtras().getInt(TRAILER_DETAIL);
        }else{
            return -1;
        }
    }

    //@Override
    //public View getView(int position, View convertView, ViewGroup parent){

    //}

    /**
     * The number of view type in the adapter
     * @return - Return integer of number of Views
     */
    @Override
    public int getViewTypeCount(){
        return VIEW_TYPE_COUNT;
    }
}
