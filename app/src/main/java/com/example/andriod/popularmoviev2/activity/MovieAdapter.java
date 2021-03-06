package com.example.andriod.popularmoviev2.activity;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.andriod.popularmoviev2.R;

/**
 * MovieAdapter that will handle and display the cursor data on the screen
 * Created by StandleyEugene on 6/30/2016.
 */
public class MovieAdapter extends CursorAdapter {

    /**
     * MovieAdapter constructor the set-up outside stuff inside
     *
     * @param context - The current app context
     * @param cursor - The current app cursor
     * @param flags - The current flag
     */
    public MovieAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);
    }

    /**
     * Class the holds the View elements
     */
    public static class ViewHolder{
        public final ImageView movieView;
        public final TextView titleView;

        /**
         * ViewHolder Constructor the binds the public layout elements to the ViewHolder object
         * @param view - The current view for the layout elements
         */
        public ViewHolder(View view){
            movieView = (ImageView) view.findViewById(R.id.moviePoster_image);
            titleView = (TextView) view.findViewById(R.id.movie_description);
        }
    }

    /**
     * Inflaters the movie_item layout and setting the viewHolder to the current view tags
     *
     * @param context - Current context for view
     * @param cursor - Current cursor information
     * @param parent - ViewGroup of the view
     * @return = Inflated view with viewholder tags
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate the movie_item (view in the grid
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);

        // Create an stance of View Holder and get the tags for the display items
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        // Return the view with all its stuff
        return view;
    }

    /**
     * Populate the view with Cursor retrieved data
     *
     * @param view - Current view information
     * @param context - Current context for view
     * @param cursor - Current cursor information
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor){
        // Get a viewHolder instance and set it to the already declared display items
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Create an instance of AQuery and set it to the movieView item
        AQuery aq = new AQuery(viewHolder.movieView);

        // Get the post information from the curse (get the row/column of information
        // from the db)
        String poster = cursor.getString(MovieFragment.COL_MOVIE_POSTER_PATH);

        // Take the ImageView and add an Image from the post location and
        // make it visible too
        aq.id(viewHolder.movieView).image(poster).visible();

        // Set the title description for the movie title
        viewHolder.titleView.setText(cursor.getString(MovieFragment.COL_MOVIE_TITLE));
    }
}
