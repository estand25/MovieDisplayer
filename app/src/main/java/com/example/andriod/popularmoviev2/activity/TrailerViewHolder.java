package com.example.andriod.popularmoviev2.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.activity.MovieAdapter;


/**
 * Class that holds the View elements for the Trailer Section
 */
public class TrailerViewHolder {
    // LinearLayout for Trailer dynamically create elements
    public final RelativeLayout mTrailerLayout;
    public final ImageView mTrailerIcon;
    public final TextView mTrailerName;

    /**
     * ViewHolder Constructor the binds the public layout elements to the ViewHolder object
     * @param view - The current view for the layout elements
     */
    public TrailerViewHolder(View view){
        // Local variable for detail screen elements
        mTrailerLayout = (RelativeLayout) view.findViewById(R.id.detail_trailerList);
        mTrailerIcon = (ImageView) view.findViewById(R.id.trailer_icon);
        mTrailerName = (TextView) view.findViewById(R.id.trailer_name);
    }
}