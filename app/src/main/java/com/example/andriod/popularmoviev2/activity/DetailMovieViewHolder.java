package com.example.andriod.popularmoviev2.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andriod.popularmoviev2.R;

/**
 * Class that holds the View elements for the Detail Movie Section
 *
 * Created by StandleyEugene on 7/24/2016.
 */
public class DetailMovieViewHolder{
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
    public DetailMovieViewHolder(View view){
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
