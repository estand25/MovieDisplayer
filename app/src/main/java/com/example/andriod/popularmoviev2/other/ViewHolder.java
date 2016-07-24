package com.example.andriod.popularmoviev2.other;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andriod.popularmoviev2.R;

/**
 * ViewHolder for the individual section of the movie detailed
 * Created by StandleyEugene on 7/22/2016.
 */
public class ViewHolder {

    // Set the local Movie Detail elements
    public final ImageView mDetail_imageView;
    public final TextView mDetail_titleTextView;
    public final TextView mDetail_synopsisTextView;
    public final TextView mDetail_userRateingTextView;
    public final TextView mDetail_releaseDateTextView;
    public final TextView mDetail_genreTextView;
    public final LinearLayout mUserRatingLayout;

    // Set the local Movie Detail Review elements
    public final TextView mReview_Author;
    public final TextView mReview_Content;

    // Set the local Movie Detail Trailer elements
    public final RelativeLayout mTrailerLayout;
    public final ImageView mTrailerIcon;
    public final TextView mTrailerName;

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

        //Local variable for review screen elements
        mReview_Author = (TextView) view.findViewById(R.id.review_author);
        mReview_Content = (TextView) view.findViewById(R.id.review_content);

        // Local variable for trailer screen elements
        mTrailerLayout = (RelativeLayout) view.findViewById(R.id.detail_trailerList);
        mTrailerIcon = (ImageView) view.findViewById(R.id.trailer_icon);
        mTrailerName = (TextView) view.findViewById(R.id.trailer_name);
    }
}
