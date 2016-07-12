package com.example.andriod.popularmoviev2;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

/**
 * Object that will take passed data and display it on the view
 * Created by StandleyEugene on 6/30/2016.
 */
public class MovieAdapter extends CursorAdapter {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, Cursor cursor, int flags){
        super(context,cursor,flags);
    }

    /*
        Cache of the childer views on the movie grid moviePoster_image movie_description
     */
    public static class ViewHolder{
        public final ImageView movieView;
        public final TextView titleView;

        public ViewHolder(View view){
            movieView = (ImageView) view.findViewById(R.id.moviePoster_image);
            titleView = (TextView) view.findViewById(R.id.movie_description);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate the movie_item (view in the grid
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);

        // Create an stance of View Holder and get the Tags for the display items
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        // Return the view with all its stuff
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        // Get a viewhold instance and set it to the already declared display items
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Create an instance of AQuery and set it to the movieView item
        AQuery aq = new AQuery(viewHolder.movieView);

        // Get the post information from the curse (get the row/column of information
        // from the db)
        String poster = cursor.getString(MovieFragment.COL_MOVIE_POSTER_PATH);

        // Take the ImageView and add an Image from the post location and
        // make it visible too
        aq.id(viewHolder.movieView).image(poster).visible();

    }
}
