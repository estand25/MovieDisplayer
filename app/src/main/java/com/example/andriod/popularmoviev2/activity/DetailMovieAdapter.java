package com.example.andriod.popularmoviev2.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
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

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private Context myContext;

    /**
     * MovieAdapter constructor the set-up outside stuff inside
     * @param context - The current app context
     * @param cursor - The current app cursor
     * @param flags - The current flag
     */
    public DetailMovieAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);

        myContext = context;
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
        int viewType = getItemViewType(cursor.getPosition());

        // Set the default layoutid value for layout
        int layoutid = -1;

        // Switch to the correct layout based on the viewtype
        switch(viewType){
            case VIEW_TYPE_MOVIE_DETAIL:{
                // Detail Movie layout
                layoutid = R.layout.movie_detail_item;
                break;
            }
            case VIEW_TYPE_MOVIE_REVIEWS:{
                // Review layout
                layoutid = R.layout.review_item;
                break;
            }
            case VIEW_TYPE_MOVIE_TRAILER:{
                // Trailer layout
                layoutid = R.layout.trailer_item;
                break;
            }
        }

        // Inflate the movie_item (view in the movie detail section)
        View view = LayoutInflater.from(context).inflate(layoutid, parent, false);

        // Create new instance of Detail Movie, Review, & Trailer Section Holder
        // Switch to the correct layout based on the viewtype
        switch(viewType){
            case VIEW_TYPE_MOVIE_DETAIL:{
                // Detail Movie ViewHolder
                DetailMovieViewHolder detailMovieViewHolder = new DetailMovieViewHolder(view);

                // Get the Tags for the display items
                view.setTag(detailMovieViewHolder);
                break;
            }
            case VIEW_TYPE_MOVIE_REVIEWS:{
                // Review ViewHolder
                ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);

                // Get the Tags for the display items
                view.setTag(reviewViewHolder);
                break;
            }
            case VIEW_TYPE_MOVIE_TRAILER:{
                // Trailer layout
                TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);

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
    public void bindView(View view, final Context context, Cursor cursor){
        // Determine which ViewType I should be using
        int viewType = getItemViewType(cursor.getPosition());

        // Switch to the correct viewHolder based on the viewtype
        switch(viewType){
            case VIEW_TYPE_MOVIE_DETAIL:{
                String[] DetailMovieColumne = getCursor().getColumnNames();
                String[] DetailMovieColumne1 = cursor.getColumnNames();

                // Set the local viewHolder with the previous tag information
                DetailMovieViewHolder detailMovieViewHolder = (DetailMovieViewHolder) view.getTag();

                // Bind the views to the bind
                detailMovieViewHolder.bindViews(context,cursor);
                break;
            }
            case VIEW_TYPE_MOVIE_REVIEWS:{
                String[] DetailMovieColumne = getCursor().getColumnNames();
                String[] DetailMovieColumne1 = cursor.getColumnNames();

                // Set the local viewHolder with the previous tag information
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) view.getTag();

                // Bind the views to the bind
                reviewViewHolder.bindViews(context,cursor);
                break;
            }
            case VIEW_TYPE_MOVIE_TRAILER:{
                String[] DetailMovieColumne = getCursor().getColumnNames();
                String[] DetailMovieColumne1 = cursor.getColumnNames();

                // Set the local viewHolder with the previous tag information
                TrailerViewHolder trailerViewHolder = (TrailerViewHolder) view.getTag();

                // Bind the views to the bind
                trailerViewHolder.bindViews(context,cursor);
                break;
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
        // Initialize and set the ret
        int ret = -1;
        boolean found = false;

        // Get the cursorCount from the passed in cursor
        int[] cursorCount = getCursor().getExtras().getIntArray(DetailMovieFragment.MOVIE_DETAIL);

        // Determine which viewType should be used based on unique columns in each of the table
        while (!found) {
            if ((getCursor().getColumnIndex("_id") <= position && getCursor().getColumnIndex("movie_type") >= position)&& (cursorCount[0] == getCursor().getColumnCount())) {
                ret = VIEW_TYPE_MOVIE_DETAIL;
                found = true;
            } else if ((getCursor().getColumnIndex("_id") <= position && getCursor().getColumnIndex("url") >= position) && (cursorCount[1] == getCursor().getColumnCount())) {
                ret = VIEW_TYPE_MOVIE_REVIEWS;
                found = true;
            } else if ((getCursor().getColumnIndex("_id") <= position && getCursor().getColumnIndex("type") >= position) && (cursorCount[2] == getCursor().getColumnCount())) {
                ret = VIEW_TYPE_MOVIE_TRAILER;
                found = true;
            }
            position++;
        }
        // Return the viewType
        return ret;
    }

    /**
     * The number of view type in the adapter
     * @return - Return integer of number of Views
     */
    @Override
    public int getViewTypeCount(){
        return VIEW_TYPE_COUNT;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case VIEW_TYPE_MOVIE_DETAIL:
                DetailMovieViewHolder detailMovieViewHolder;

                if (convertView == null) {
                    convertView = newView(myContext, getCursor(), parent);
                    detailMovieViewHolder = new DetailMovieViewHolder(convertView);
                    convertView.setTag(detailMovieViewHolder);
                } else {
                    detailMovieViewHolder = (DetailMovieViewHolder) convertView.getTag();
                }

                bindView(convertView, myContext, getCursor());
                return convertView;
            case VIEW_TYPE_MOVIE_REVIEWS:
                ReviewViewHolder reviewViewHolder;

                if (convertView == null) {
                    convertView = newView(myContext, getCursor(), parent);
                    reviewViewHolder = new ReviewViewHolder(convertView);
                    convertView.setTag(reviewViewHolder);
                } else {
                    reviewViewHolder = (ReviewViewHolder) convertView.getTag();
                }

                bindView(convertView, myContext, getCursor());
                return convertView;
            case VIEW_TYPE_MOVIE_TRAILER:
                TrailerViewHolder trailerViewHolder;

                if (convertView == null) {
                    convertView = newView(myContext, getCursor(), parent);
                    trailerViewHolder = new TrailerViewHolder(convertView);
                    convertView.setTag(trailerViewHolder);
                } else {
                    trailerViewHolder = (TrailerViewHolder) convertView.getTag();
                }

                bindView(convertView, myContext, getCursor());
                return convertView;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Class that holds the View elements for the Detail Movie Section
     */
    static class DetailMovieViewHolder {
        // Set the static local Movie Detail elements
        @BindView(R.id.detail_imageView) ImageView mDetail_imageView;
        @BindView(R.id.detail_titleTextView) TextView mDetail_titleTextView;
        @BindView(R.id.detail_synopsisTextView) TextView mDetail_synopsisTextView;
        @BindView(R.id.detail_UserRateingTextView) TextView mDetail_userRateingTextView;
        @BindView(R.id.detail_releaseDateTextView) TextView mDetail_releaseDateTextView;
        @BindView(R.id.detail_genreTextView) TextView mDetail_genreTextView;
        @BindView(R.id.detail_UserRateingLayout) LinearLayout mUserRatingLayout;

        /**
         * ViewHolder Constructor the binds the public layout elements to the ViewHolder object
         * @param view - The current view for the layout elements
         */
        public DetailMovieViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        /**
         * Bind the view with the view data
         * @param context - The current context
         * @param cursor - The current cursor
         */
        public void bindViews(Context context, Cursor cursor) {
            // Move to the cursor column location
            // cursor.move(cursor.getColumnIndex("_id"));
            cursor.moveToNext();

            // Create an instance of AQuery and set it to the movieView item
            AQuery aq = new AQuery(mDetail_imageView);

            // Get the post information from the curse (get the row/column of information
            // from the db)
            String poster = cursor.getString(COL_DETAIL_MOVIE_POSTER_PATH);

            // Take the ImageView and add an Image from the post location and
            // make it visible too
            // Replaced Picassa with AQery per the below form post. The image were loading to slow
            // so I looked and found a solution (https://discussions.udacity.com/t/picassa-image-caching-and-loading/175512)
            aq.id(mDetail_imageView).image(poster).visible();

            // Get the TextView from the current layout and set the text
            // to what appears at position X in the column layout
            String title = cursor.getString(COL_DETAIL_MOVIE_TITLE);
            mDetail_titleTextView.setText(title);
            String overView = cursor.getString(COL_DETAIL_MOVIE_OVERVIEW);
            mDetail_synopsisTextView.setText(overView);

            //Log.v("Stars ", cursor.getString(COL_DETAIL_MOVIE_VOTE_AVERAGE));
            //Log.v("Column Loc ",Integer.toString(COL_DETAIL_MOVIE_VOTE_AVERAGE));

            // Create previous star display then add new rating number then stars
            mUserRatingLayout.removeAllViews();

            // Add the user rating scores to TextView element
            String voteAverage = cursor.getString(COL_DETAIL_MOVIE_VOTE_AVERAGE);
            mDetail_userRateingTextView.setText(voteAverage);

            // Add the user rating TextView to the user rating layout
            mUserRatingLayout.addView(mDetail_userRateingTextView);

            // Loop through and populate the start images
            for (int i = 0; i < cursor.getInt(COL_DETAIL_MOVIE_VOTE_AVERAGE); i++) {
                ImageView starImages = new ImageView(context);
                starImages.setImageResource(R.drawable.star);
                mUserRatingLayout.addView(starImages);
                //center_vertical 16 and Left 3
                mUserRatingLayout.setVerticalGravity(16 | 3);
                Log.v("Stars created ", Integer.toString(i));
            }

            String releaseDate = cursor.getString(COL_DETAIL_MOVIE_RELEASE_DATE);
            mDetail_releaseDateTextView.setText(releaseDate);
            String movieGenere = cursor.getString(COL_DETAIL_MOVIE_GENRE_IDS);
            mDetail_genreTextView.setText(movieGenere);
        }
    }

    /**
     * Class that holds the View elements for the Review section
     */
    static class ReviewViewHolder {
        // Set the static local Review elements
        @BindView(R.id.review_author) TextView mReview_Author;
        @BindView(R.id.review_content) TextView mReview_Content;

        /**
         * ViewHolder Constructor the binds the public layout elements to the ViewHolder object
         * @param view - The current view for the layout elements
         */
        public ReviewViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        /**
         * Bind the view with the view data
         * @param context - The current context
         * @param cursor - The current cursor
         */
        public void bindViews(final Context context,Cursor cursor){
            // Move to the cursor column location
            cursor.move(cursor.getColumnIndex("_id"));

            // Loop through the records of review data until
            // we get to the next cursor's first column name
            for(int i = 0; i<cursor.getCount();i++){
                // Set local string variable
                String author = cursor.getString(COL_REVIEW_AUTHOR) + " ";
                String content = cursor.getString(COL_REVIEW_CONTENT);
                final String reviewUri = cursor.getString(COL_REVIEW_URL);

                // Check if author and content is populated
                if(author.isEmpty()){
                    break;
                }

                if(content.isEmpty()) {
                    break;
                }

                // Set update the Review Card View with the Author and Content information
                mReview_Author.setText(author);
                mReview_Content.setText(content);

                mReview_Content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(reviewUri));
                        context.startActivity(intent);
                    }
                });

                // Break out of the while loop if we move to the next table (trailer)
                if (cursor.getColumnName(COL_TRAILER__ID).contains("_id")) {
                    break;
                }
            }
        }
    }

    /**
     * Class that holds the View elements for the Trailer Section
     */
    static class TrailerViewHolder {
        // Set the static local Trailer elements
        @BindView(R.id.trailer_icon) ImageView mTrailerIcon;
        @BindView(R.id.trailer_name) TextView mTrailerName;

        /**
         * ViewHolder Constructor the binds the public layout elements to the ViewHolder object
         * @param view - The current view for the layout elements
         */
        public TrailerViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        public void bindViews(final Context context, Cursor cursor){
            // Move to the cursor column location
            cursor.move(cursor.getColumnIndex("_id"));

            // Loop through the records of review data until
            // we get to the next cursor's first column name
            for(int i = 0; i<cursor.getCount();i++){
                // Set local string variable
                String trailerName = cursor.getString(COL_MOVIE_TITLE) + " - " + cursor.getString(COL_TRAILER_NAME);
                final String video_id = cursor.getString(COL_TRAILER_KEY);

                Log.v("Trailer Name ",trailerName);

                // Check if trailerName is populated
                if(trailerName == null) {
                    break;
                }

                // Set update the Trailer Card View with the Trailer Icon and Name
                //trailerHolder.mTrailerIcon.setImageResource(R.drawable.ic_entypo);
                mTrailerName.setText(trailerName);
                mTrailerName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Try to open up youtube with trailer video directly, but if not able
                        // I open an internet browser
                        // Note: I embedded this code section based on the stackoverflow post
                        // it was a lot better then what I was thinking of doing
                        // http://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video_id));
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://www.youtube.com/watch?v=" + video_id));
                            context.startActivity(intent);
                        }
                    }
                });
            }
        }
    }
}
