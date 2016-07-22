package com.example.andriod.popularmoviev2.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.model.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StandleyEugene on 7/17/2016.
 */
public class DetailTrailerAdapter extends CursorAdapter {
    private static final String LOG_TAG = DetailTrailerAdapter.class.getSimpleName();

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

    // Set the local Trailer Detail elements
    private TextView mTrailer_id;
    private TextView mTrailer_iso_639_1;
    private TextView mTrailer_iso_3166_1;
    private TextView mTrailer_key;
    private TextView mTrailer_name;
    private TextView mTrailer_site;
    private TextView mTrailer_size;
    private TextView mTrailer_type;
    private CardView mTrailer_CardHolder;
    private RelativeLayout mTrailer_RalativeLayout;

    /**
     * MovieAdapter constructor the set-up outside stuff inside
     * @param context - The current app context
     * @param cursor - The current app cursor
     * @param flags - The current flag
     */
    public DetailTrailerAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);
    }

    /**
     * Class the holds the View elements
     */
    public static class ViewHolder{
        // LinearLayout for Trailer dynamically create elements
        public final RelativeLayout mTrailerLayout;
        public final ImageView mTrailerIcon;
        public final TextView mTrailerName;

        /**
         * ViewHolder Constructor the binds the public layout elements to the ViewHolder object
         * @param view - The current view for the layout elements
         */
        public ViewHolder(View view){
            // Local variable for detail screen elements
            mTrailerLayout = (RelativeLayout) view.findViewById(R.id.detail_trailerList);
            mTrailerIcon = (ImageView) view.findViewById(R.id.trailer_icon);
            mTrailerName = (TextView) view.findViewById(R.id.trailer_name);
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
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_item, parent, false);

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
    public void bindView(View view, final Context context, Cursor cursor) {
        // Set the local viewHolder with the previous tag information
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Set the record local in table is less then zero
        // Note: So when we go into the loop we start at the
        //       top of the tables
        /*cursor.moveToPosition(-1);

        // Set the initial Text it
        int i = 1;

        // List of Trailers
        List<Trailer> trailerColl = new ArrayList<Trailer>();

        // Loop through adding new reviews
        while (cursor.moveToNext()) {
            // Create a new Trailer Object
            Trailer t = new Trailer();
            t.setId(cursor.getString(COL_TRAILER_ID));
            t.setIso6391(cursor.getString(COL_TRAILER_ISO_6391));
            t.setIso31661(cursor.getString(COL_TRAILER_ISO_31661));
            t.setKey(cursor.getString(COL_TRAILER_KEY));
            t.setName(cursor.getString(COL_TRAILER_NAME));
            t.setSite(cursor.getString(COL_TRAILER_SITE));
            t.setSize(cursor.getInt(COL_TRAILER_SIZE));
            t.setType(cursor.getString(COL_TRAILER_TYPE));
            String movieTitle = cursor.getString(COL_MOVIE_TITLE);

            // Add the new review to the list to check against to make sure
            // there are not repeats
            trailerColl.add(t);
            // Check if list already has trailer
            if(!trailerColl.contains(t)){

                // Set the button label text
                String trailerLabel = movieTitle + " - " + t.getName();

                // Create new instance of TextView for trailer link
                mTrailer_name = new TextView(context);
                // Set the link text color
                mTrailer_name.setLinkTextColor(context.getResources().getColor(R.color.colorPrimary));
                // Set the link text
                mTrailer_name.setText(trailerLabel);
                // Set the View id for this Text
                mTrailer_name.setId(i++);

                // Set the video key to pass to YouTube
                final String video_id = t.getKey();

                // Create new instance of the RelativeLayout.LayoutParams
                // set the width & height of the relativeLayout
                RelativeLayout.LayoutParams R1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                // Create new instance of the RelativeLayout
                mTrailer_RalativeLayout = new RelativeLayout(context);

                // Set-up the onClickListener for the TextView push key to
                // trigger youtube
                mTrailer_name.setOnClickListener(new View.OnClickListener() {
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

                // Create new instance of the CardViewer
                mTrailer_CardHolder = new CardView(context);

                // Add the Trailer text to the new layout
                mTrailer_RalativeLayout.addView(mTrailer_name);
                // Set the padding of the RelativeLayout
                mTrailer_RalativeLayout.setPadding(10,10,10,10);

                // Add the RelativeLayout to the cardView
                mTrailer_CardHolder.addView(mTrailer_RalativeLayout);
                // Set to true to ensure separate views have padding
                mTrailer_CardHolder.setUseCompatPadding(true);
                // Set the Elevation of the CardViewer
                mTrailer_CardHolder.setElevation(10);
                // Set the Radius of the individual CardViewers
                mTrailer_CardHolder.setRadius(3);
                // Set general padding space in the content
                mTrailer_CardHolder.setContentPadding(10,10,10,10);
                // Set the CardView Overlap to true to stop overlapping of the CardViewers
                mTrailer_CardHolder.setPreventCornerOverlap(true);
                // added the link to the layout
                viewHolder.mTrailerLayout.addView(mTrailer_name);
            }
        }*/
        viewHolder.mTrailerIcon.setImageResource(R.drawable.ic_entypo);
        viewHolder.mTrailerName.setText(cursor.getString(COL_MOVIE_TITLE) + " " +cursor.getString(COL_TRAILER_NAME));
    }
}
