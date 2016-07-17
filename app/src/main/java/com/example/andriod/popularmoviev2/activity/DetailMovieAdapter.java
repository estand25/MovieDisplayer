package com.example.andriod.popularmoviev2.activity;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.andriod.popularmoviev2.R;

/**
 * Created by StandleyEugene on 7/17/2016.
 */
public class DetailMovieAdapter extends CursorAdapter {
    private static final String LOG_TAG = DetailMovieAdapter.class.getSimpleName();

    public DetailMovieAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);
    }

    public static class ViewHolder{
        // Set the local Movie Detail elements
        public final ImageView mDetail_imageView;
        public final TextView mDetail_titleTextView;
        public final TextView mDetail_synopsisTextView;
        public final TextView mDetail_userRateingTextView;
        public final TextView mDetail_releaseDateTextView;
        public final TextView mDetail_genreTextView;
        public final LinearLayout mUserRatingLayout;

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

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        // Create an instance of AQuery and set it to the movieView item
        AQuery aq = new AQuery(viewHolder.mDetail_imageView);

        // Get the post information from the curse (get the row/column of information
        // from the db)
        String poster = cursor.getString(DetailMovieFragment.COL_DETAIL_MOVIE_POSTER_PATH);

        // Take the ImageView and add an Image from the post location and
        // make it visible too
        // Replaced Picassa with AQery per the below form post. The image were loading to slow
        //so I looked and found a soluation (https://discussions.udacity.com/t/picassa-image-caching-and-loading/175512)
        aq.id(viewHolder.mDetail_imageView).image(poster).visible();

        /// Get the TextView from the current layout and set the text
        // to what appears at position X in the column layout
        viewHolder.mDetail_titleTextView.setText(cursor.getString(DetailMovieFragment.COL_DETAIL_MOVIE_TITLE));
        viewHolder.mDetail_synopsisTextView.setText(cursor.getString(DetailMovieFragment.COL_DETAIL_MOVIE_OVERVIEW));
        viewHolder.mDetail_userRateingTextView.setText(cursor.getString(DetailMovieFragment.COL_DETAIL_MOVIE_VOTE_AVERAGE));

        // Loop through and populate the start images
        for(int i = 0; i < cursor.getDouble(DetailMovieFragment.COL_DETAIL_MOVIE_VOTE_AVERAGE);i++){
            ImageView starImages = new ImageView(context);
            starImages.setImageResource(R.drawable.ic_full_star);
            viewHolder.mUserRatingLayout.addView(starImages);
        }

        viewHolder.mDetail_releaseDateTextView.setText(cursor.getString(DetailMovieFragment.COL_DETAIL_MOVIE_RELEASE_DATE));
        viewHolder.mDetail_genreTextView.setText(cursor.getString(DetailMovieFragment.COL_DETAIL_MOVIE_GENRE_IDS));

        //viewHolder.mDetail_genreTextView.setText(getGenreName(cursor.getString(DetailMovieFragment.COL_DETAIL_MOVIE_GENRE_IDS)));
    }
}
