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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.andriod.popularmoviev2.R;

/**
 * Created by StandleyEugene on 7/17/2016.
 */
public class DetailTrailerAdapter extends CursorAdapter {
    private static final String LOG_TAG = DetailTrailerAdapter.class.getSimpleName();

    // Set the local Trailer Detail elements
    private TextView mTrailer_link;

    public DetailTrailerAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);
    }

    public static class ViewHolder{
        // LinearLayout for Trailer dynamically create elements
        public final LinearLayout mTrailerLayout;

        public ViewHolder(View view){
            // Local variable for detail screen elements
            mTrailerLayout = (LinearLayout) view.findViewById(R.id.movieTrailerItem);
        }
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        // Inflate the movie_item (view in the grid
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_item, parent, false);

        // Create an stance of View Holder and get the Tags for the display items
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        // Return the view with all its stuff
        return view;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        // Set the record local in table is less then zero
        // Note: So when we go into the loop we start at the
        //       top of the tables
        cursor.moveToPosition(-1);

        // Loop through adding new reviews
        while(cursor.moveToNext()){

            // Set the button label text
            String trailerLabel = cursor.getString(DetailMovieFragment.COL_MOVIE_TITLE) + " " + cursor.getString(DetailMovieFragment.COL_TRAILER_NAME);

            // Create new instance of TextView for trailer link
            mTrailer_link = new TextView(context);

            // Set background image
            mTrailer_link.setBackgroundResource(R.drawable.ic_entypo);

            // Set the link text
            mTrailer_link.setText(trailerLabel);

            // Set the video key to pass to YouTube
            final String video_id = cursor.getString(DetailMovieFragment.COL_TRAILER_KEY);

            // Set-up the onClickListener for the button push key to
            // trigger youtube
            mTrailer_link.setOnClickListener(new View.OnClickListener() {
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
                    }catch (ActivityNotFoundException ex){
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + video_id));
                        context.startActivity(intent);
                    }
                }
            });

            // added the link to the layout
            viewHolder.mTrailerLayout.addView(mTrailer_link);
        }
    }
}
