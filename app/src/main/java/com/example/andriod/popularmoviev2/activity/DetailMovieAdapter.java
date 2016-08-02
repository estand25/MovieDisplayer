package com.example.andriod.popularmoviev2.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieContract;
import com.example.andriod.popularmoviev2.data.MovieTableSync;
import com.example.andriod.popularmoviev2.other.Constants;
import com.example.andriod.popularmoviev2.other.Utility;
import com.example.andriod.popularmoviev2.sync.MovieSyncAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Detail Movie Adapter for the individual general movie details
 *
 * Created by StandleyEugene on 7/17/2016.
 */
public class DetailMovieAdapter extends CursorAdapter {
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
    static MovieTableSync movieTableSync;

    // ListView adapter view type
    private static final int VIEW_TYPE_MOVIE_DETAIL = 0;
    private static final int VIEW_TYPE_MOVIE_REVIEWS = 1;
    private static final int VIEW_TYPE_MOVIE_TRAILER = 2;

    // ListView view count
    private static final int VIEW_TYPE_COUNT = 3;

    // Local version of context
    private Context myContext;

    /**
     * MovieAdapter constructor the set-up outside stuff inside
     * @param context - The current app context
     * @param cursor - The current app cursor
     * @param flags - The current flag
     */
    public DetailMovieAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);

        // Local context
        myContext = context;

        // Local movie sync uploader
        movieTableSync = new MovieTableSync(context);
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
        int viewType = getItemViewType(cursor.getColumnCount());

        // Set the default layout id value for layout
        int layoutid = -1;

        // Switch to the correct layout based on the view type
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
        // Switch to the correct layout based on the view type
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
        int viewType = getItemViewType(cursor.getColumnCount());

        // Switch to the correct viewHolder based on the view type
        switch(viewType){
            case VIEW_TYPE_MOVIE_DETAIL:{
                // Set the local viewHolder with the previous tag information
                DetailMovieViewHolder detailMovieViewHolder = (DetailMovieViewHolder) view.getTag();
                // Bind cursor data to the view
                detailMovieViewHolder.bindViews(myContext,cursor);
                break;
            }
            case VIEW_TYPE_MOVIE_REVIEWS:{
                // Set the local viewHolder with the previous tag information
                ReviewViewHolder reviewViewHolder = (ReviewViewHolder) view.getTag();
                // Bind cursor data to the view
                reviewViewHolder.bindViews(myContext,cursor);
                break;
            }
            case VIEW_TYPE_MOVIE_TRAILER:{

                // Set the local viewHolder with the previous tag information
                TrailerViewHolder trailerViewHolder = (TrailerViewHolder) view.getTag();
                // Bind cursor data to the view
                trailerViewHolder.bindViews(myContext,cursor);
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

        // Get the cursorCount from the passed in cursor
        int[] cursorCount = getCursor().getExtras().getIntArray(Constants.MOVIE_DETAIL);

        // Determine which viewType should be used based on unique columns in each of the table
        if(cursorCount[0] == position){
            ret = VIEW_TYPE_MOVIE_DETAIL;
        } else if(cursorCount[1] == position){
            ret = VIEW_TYPE_MOVIE_REVIEWS;
        } else if(cursorCount[2] == position){
            ret = VIEW_TYPE_MOVIE_TRAILER;
        }
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

    /**
     * Class that holds the View elements for the Detail Movie Section
     */
    static class DetailMovieViewHolder {
        // Set the static local Movie Detail elements
        @BindView(R.id.detail_imageView) ImageView mDetail_imageView;
        @BindView(R.id.detail_titleTextView) TextView mDetail_titleTextView;
        @BindView(R.id.detail_synopsisTextView) TextView mDetail_synopsisTextView;
        @BindView(R.id.detail_UserRateingTextView) TextView mDetail_userRatingTextView;
        @BindView(R.id.detail_releaseDateTextView) TextView mDetail_releaseDateTextView;
        @BindView(R.id.detail_genreTextView) TextView mDetail_genreTextView;
        @BindView(R.id.detail_UserRateingLayout) LinearLayout mUserRatingLayout;
        @BindView(R.id.detail_favoriteMovieLayout) LinearLayout mFavoriteMovieLayout;
        @BindView(R.id.detail_favorButton) ImageButton mFavorButton;

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
        public void bindViews(final Context context, final Cursor cursor) {
            // Create an instance of AQuery and set it to the movieView item
            // Take the ImageView and add an Image from the post location and make it visible too
            // Replaced Picassa with AQuery per the below form post. The image were loading to slow
            // so I looked and found a solution (https://discussions.udacity.com/t/picassa-image-caching-and-loading/175512)
            String poster = cursor.getString(COL_DETAIL_MOVIE_POSTER_PATH);
            AQuery aq = new AQuery(mDetail_imageView);
            aq.id(mDetail_imageView).image(poster).visible();

            // Get the TextView from the current layout and set the text
            // to what appears at position X in the column layout
            mDetail_titleTextView.setText(cursor.getString(COL_DETAIL_MOVIE_TITLE));
            mDetail_synopsisTextView.setText(cursor.getString(COL_DETAIL_MOVIE_OVERVIEW));

            // Create previous star display then add new rating number then stars
            mUserRatingLayout.removeAllViews();

            // Add the user rating scores to TextView element
            mDetail_userRatingTextView.setText(cursor.getString(COL_DETAIL_MOVIE_VOTE_AVERAGE));

            // Add the user rating TextView to the user rating layout
            mUserRatingLayout.addView(mDetail_userRatingTextView);

            // Loop through and populate the start images
            for (int i = 0; i < cursor.getInt(COL_DETAIL_MOVIE_VOTE_AVERAGE); i++) {
                final ImageView starImages = new ImageView(context);
                starImages.setImageResource(R.drawable.star);
                mUserRatingLayout.addView(starImages);
                //center_vertical 16 and Left 3
                mUserRatingLayout.setVerticalGravity(16 | 3);
            }

            // Populates the Release Date & Genre id
            mDetail_releaseDateTextView.setText(cursor.getString(COL_DETAIL_MOVIE_RELEASE_DATE));
            mDetail_genreTextView.setText(cursor.getString(COL_DETAIL_MOVIE_GENRE_IDS));

            // Check if movie is favorite or not
            final boolean favoriteSetting =cursor.getString(COL_DETAIL_MOVIE_TYPE).contains("movie");

            if(favoriteSetting){
                mFavorButton.setImageResource(R.drawable.unfavorite);
            }else{
                mFavorButton.setImageResource(R.drawable.favorite);
            }

            // Set local variable for movie id
            final int movieId = Integer.parseInt(cursor.getString(COL_DETAIL_MOVIE_ID));

            // Create String array of movie stuff
            final String[] movieStuff = new String[12];

            // Movie parts
            movieStuff[0] = cursor.getString(COL_DETAIL_MOVIE_ID);
            movieStuff[1] = cursor.getString(COL_DETAIL_MOVIE_POSTER_PATH);
            movieStuff[2] = cursor.getString(COL_DETAIL_MOVIE_OVERVIEW);
            movieStuff[3] = cursor.getString(COL_DETAIL_MOVIE_RELEASE_DATE);
            movieStuff[4] = cursor.getString(COL_DETAIL_MOVIE_GENRE_IDS);
            movieStuff[5] = cursor.getString(COL_DETAIL_MOVIE_ORIG_TITLE);
            movieStuff[6] = cursor.getString(COL_DETAIL_MOVIE_ORIG_LANGUAGE);
            movieStuff[7] = cursor.getString(COL_DETAIL_MOVIE_TITLE);
            movieStuff[8] = cursor.getString(COL_DETAIL_MOVIE_BACKDROP_PATH);
            movieStuff[9] = cursor.getString(COL_DETAIL_MOVIE_POPULARITY);
            movieStuff[10] = cursor.getString(COL_DETAIL_MOVIE_VOTE_COUNT);
            movieStuff[11] = cursor.getString(COL_DETAIL_MOVIE_VOTE_AVERAGE);

            // onClickListener for favorite button to update movie type
            // and add it to favorite_movie type
            mFavorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Check if movie is on favorite list then
                    //  change the button image and updates the movie_type to FAVOR
                    //  then check if movie is already in favorite table if not
                    //  added to the favorite table otherwise nothing happens
                    // Otherwise I change the button image then
                    //  update the movie_type to blank and delete the movie from the
                    //  favorite movie table
                    if(favoriteSetting){
                        mFavorButton.setImageResource(R.drawable.favorite);
                        movieTableSync.updateMovieFavorite(movieId,Utility.getPreferredMovieType(context));

                        // Check if movie already in favorite_movie table
                        if(!movieTableSync.queryFavoriteMovie(movieId)) {
                            movieTableSync.insertFavoriteMovie(movieStuff);
                        }
                    }else {
                        mFavorButton.setImageResource(R.drawable.unfavorite);
                        movieTableSync.updateMovieFavorite(movieId,"");
                        movieTableSync.deleteFavoriteMovie(movieId);
                    }
                }
            });
        }
    }

    /**
     * Class that holds the View elements for the Review section
     */
    static class ReviewViewHolder {
        // Set the static local Review elements
        @BindView(R.id.review_author) TextView mReview_Author;
        @BindView(R.id.review_content) TextView mReview_Content;
        @BindView(R.id.reviewCard) android.support.v7.widget.CardView mReviewCard;

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
        public void bindViews(final Context context,Cursor cursor) {
            // Create local string variable
            String author ="Review Author: " + cursor.getString(COL_REVIEW_AUTHOR);
            String content = cursor.getString(COL_REVIEW_CONTENT);
            final String reviewUri = cursor.getString(COL_REVIEW_URL);

            // Set review card elements text
            mReview_Author.setText(author);
            mReview_Content.setText(content);

            // onClickListener for the review card
            mReviewCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(reviewUri));
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * Class that holds the View elements for the Trailer Section
     */
    static class TrailerViewHolder {
        // Set the static local Trailer elements
        @BindView(R.id.trailer_icon) ImageView mTrailerIcon;
        @BindView(R.id.trailer_name) TextView mTrailerName;
        @BindView(R.id.trailer_site) TextView mTrailerSite;
        @BindView(R.id.trailer_type) TextView mTrailerType;
        @BindView(R.id.trailer_size) TextView mTrailerSize;
        @BindView(R.id.trailerCard) android.support.v7.widget.CardView mTrailerCard;

        /**
         * ViewHolder Constructor the binds the public layout elements to the ViewHolder object
         * @param view - The current view for the layout elements
         */
        public TrailerViewHolder(View view){
            ButterKnife.bind(this,view);
        }

        /**
         * Bind the view with the view data
         * @param context - The current context
         * @param cursor - The current cursor
         */
        public void bindViews(final Context context, Cursor cursor){
            // Get the video_id for the trailer
            final String video_id = cursor.getString(COL_TRAILER_KEY);

            // Add thumbnail image for specific trailer on youtube
            // Determine what type of screen is being used got it from this post
            //  (http://stackoverflow.com/questions/9279111/determine-if-the-device-is-a-smartphone-or-tablet)
            String poster;
            if (Utility.isTablet(context)) {
                poster = "http://i3.ytimg.com/vi/"+video_id+"/default.jpg";
            } else {
                poster = "http://i3.ytimg.com/vi/"+video_id+"/hqdefault.jpg";
            }
            AQuery aq = new AQuery(mTrailerIcon);
            aq.id(mTrailerIcon).image(poster).visible();

            // Create local string variable
            String trailerName = "Title: " + cursor.getString(COL_TRAILER_NAME);
            String trailerSite = "Site: " + cursor.getString(COL_TRAILER_SITE);
            String trailerType = "Type: " + cursor.getString(COL_TRAILER_TYPE);
            String trailerSize = "Size: " + cursor.getString(COL_TRAILER_SIZE);

            // Set trailer card elements text
            mTrailerName.setText(trailerName);
            mTrailerSite.setText(trailerSite);
            mTrailerSize.setText(trailerSize);
            mTrailerType.setText(trailerType);

            // onClickListener for the trailer card
            mTrailerCard.setOnClickListener(new View.OnClickListener() {
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