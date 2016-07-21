package com.example.andriod.popularmoviev2.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.model.Review;
import com.example.andriod.popularmoviev2.model.ReviewColl;

import java.util.ArrayList;
import java.util.List;

/**
 * DetailReviewAdapter that will handle and display the cursor data on the screen
 * Created by StandleyEugene on 7/17/2016.
 */
public class DetailReviewAdapter extends CursorAdapter{
    private static final String LOG_TAG = DetailReviewAdapter.class.getSimpleName();

    // There indices are tied to REVIEW_COLUMNS. If REVIEW_COLUMNS change, these need change too
    static final int COL_REVIEW__ID = 0;
    static final int COL_REVIEW_ID = 1;
    static final int COL_REVIEW_MOVIE_ID = 2;
    static final int COL_REVIEW_AUTHOR = 3;
    static final int COL_REVIEW_CONTENT = 4;
    static final int COL_REVIEW_URL = 5;

    // Set the local Review Detail elements
    private CardView mReview_CardHolder;
    private TextView mReview_Author;
    private TextView mReview_Content;
    private RelativeLayout mReview_RelativeLayout;

    /**
     * MovieAdapter constructor the set-up outside stuff inside
     * @param context - The current app context
     * @param cursor - The current app cursor
     * @param flags - The current flag
     */
    public DetailReviewAdapter(Context context, Cursor cursor, int flag){
        super(context,cursor,flag);
    }

    /**
     * Class the holds the View elements
     */
    public static class ViewHolder{
        public final LinearLayout mDetail_reviewList;

        /**
         * ViewHolder Constructor the binds the public layout elements to the ViewHolder object
         * @param view - The current view for the layout elements
         */
        public ViewHolder(View view){
            mDetail_reviewList = (LinearLayout) view.findViewById(R.id.detail_reviewList);
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
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);

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
    public void bindView(View view, final Context context, Cursor cursor){
        // Set the local viewholder with the previous tag information
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Log.v("Row Count ", Integer.toString(cursor.getCount()));

        // Set the record local in table is less then zero
        // Note: So when we go into the loop we start at the
        //       top of the tables
        cursor.moveToPosition(-1);

        // Set the initial Text it
        int i = 1;

        // List of Reviews
        List<Review> reviewColl = new ArrayList<Review>();

        // Loop through adding new reviews
        while(cursor.moveToNext()){
            // Create a new review object and set the values
            Review r = new Review();
            r.setAuthor(cursor.getString(COL_REVIEW_AUTHOR));
            r.setContent(cursor.getString(COL_REVIEW_CONTENT));
            r.setId(cursor.getString(COL_REVIEW_ID));
            r.setUrl(cursor.getString(COL_REVIEW_URL));
            // Add the new review to the list to check against to make sure
            // there are not repeats
            reviewColl.add(r);

            // Check if list already has review
            if(!reviewColl.contains(r)){

                // Set the Author to the new TextView
                mReview_Author = new TextView(context);
                // Set the Author textView with cursor information
                mReview_Author.setText("Author: " + r.getAuthor());
                // Set the View id for this TextView
                mReview_Author.setId(i++);

                // Set the Content to the new TextView
                mReview_Content = new TextView(context);
                // Set the Content textView with cursor information
                mReview_Content.setText(r.getContent());
                // Set the View id for this TextView
                mReview_Content.setId(i++);

                // Create new instance of the RelativeLayout.LayoutParams
                // set the width & height of the relativeLayout
                RelativeLayout.LayoutParams R1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                // Set the RelativeLayout rule location for the authoer
                R1.addRule(RelativeLayout.BELOW, mReview_Author.getId());

                // Create new instance of the RelativeLayout
                mReview_RelativeLayout = new RelativeLayout(context);

                // Create new instance of CardViewer
                mReview_CardHolder = new CardView(context);

                // Add the two textView to the new layout
                mReview_RelativeLayout.addView(mReview_Author);
                // Add the second textView to the RelativeLayout per the rule
                // specificed in R1
                mReview_RelativeLayout.addView(mReview_Content,R1);
                // Set the padding of the RelativeLayout
                //mReview_RelativeLayout.setPadding(10,10,10,10);

                // Add the RelativeLayout to the cardView
                mReview_CardHolder.addView(mReview_RelativeLayout);
                // Set to true to ensure separate views have padding
                mReview_CardHolder.setUseCompatPadding(true);
                // Set the Elevation of the CardViewer
                mReview_CardHolder.setElevation(10);
                // Set the Radius of the individual CardViewers
                mReview_CardHolder.setRadius(3);
                // Set general padding space in the content
                mReview_CardHolder.setContentPadding(10,10,10,10);
                // Set the CardView Overlap to true to stop overlapping of the CardViewers
                mReview_CardHolder.setPreventCornerOverlap(true);

                // Set local variable with Uri
                final String reviewUri = r.getUrl();

                // Set-up the onClickListener
                mReview_CardHolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(reviewUri));
                        context.startActivity(intent);
                    }
                });

                // Add the RelativeLayout to the detail Review list (LinearLayout)
                viewHolder.mDetail_reviewList.addView(mReview_CardHolder);
                // Add padding to the outside ot the relativeLayout
                viewHolder.mDetail_reviewList.setPadding(15,10,15,10);
            }
        }
    }
}
