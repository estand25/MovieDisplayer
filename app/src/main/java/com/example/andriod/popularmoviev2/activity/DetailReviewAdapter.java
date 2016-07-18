package com.example.andriod.popularmoviev2.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andriod.popularmoviev2.R;

/**
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
    private Button mReview_button;
    private TextView mReview_Author;
    private TextView mReview_Content;
    private TextView mReview_URL;

    public DetailReviewAdapter(Context context, Cursor cursor, int flag){
        super(context,cursor,flag);
    }

    public static class ViewHolder{
        public final LinearLayout reviewLinearLayout;

        public ViewHolder(View view){
            reviewLinearLayout = (LinearLayout) view.findViewById(R.id.movieReviewItem);
        }
    }

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

    @Override
    public void bindView(View view, final Context context, Cursor cursor){
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Log.v("Row Count ", Integer.toString(cursor.getCount()));

        Log.v("Review Count ", Integer.toString(viewHolder.reviewLinearLayout.getChildCount()));

        // Set the record local in table is less then zero
        // Note: So when we go into the loop we start at the
        //       top of the tables
        cursor.moveToPosition(-1);

        // Loop through adding new reviews
        while(cursor.moveToNext()){
            // Create new instance of TextView for author infor
            mReview_Author = new TextView(context);
            // Set the text label for author
            mReview_Author.setText(cursor.getString(COL_REVIEW_AUTHOR));

            // Create new instance of TextView for content infor
            mReview_Content = new TextView(context);
            // Set the text label for content
            mReview_Content.setText(cursor.getString(COL_REVIEW_CONTENT));

            // Create new instance of TextView for URL infor
            mReview_URL = new TextView(context);

            // Set the text label for content
            mReview_URL.setText(cursor.getString(COL_REVIEW_URL));
            mReview_URL.setLinkTextColor(context.getResources().getColor(R.color.colorPrimaryDark));

            // Set local variable with Uri link for review
            final String reviewUri = cursor.getString(COL_REVIEW_URL);

            // Set-up the onClickListener for the TextView push key to
            // trigger internet browser
            mReview_URL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(reviewUri));
                    context.startActivity(intent);
                }
            });

            // Add the new review entry into the outter layout reviewLayout
            viewHolder.reviewLinearLayout.addView(mReview_Author);
            viewHolder.reviewLinearLayout.addView(mReview_Content);
            viewHolder.reviewLinearLayout.addView(mReview_URL);
        }
    }
}
