package com.example.andriod.popularmoviev2.activity;

import android.view.View;
import android.widget.TextView;
import com.example.andriod.popularmoviev2.R;

/**
 * Class that holds the View elements for the Review section
 *
 * Created by StandleyEugene on 7/24/2016.
 */
public class ReviewViewHolder {
    //public final LinearLayout mDetail_reviewList;
    public final TextView mReview_Author;
    public final TextView mReview_Content;

    /**
     * ViewHolder Constructor the binds the public layout elements to the ViewHolder object
     * @param view - The current view for the layout elements
     */
    public ReviewViewHolder(View view){
        //mDetail_reviewList = (LinearLayout) view.findViewById(R.id.detail_reviewList);
        mReview_Author = (TextView) view.findViewById(R.id.review_author);
        mReview_Content = (TextView) view.findViewById(R.id.review_content);
    }
}