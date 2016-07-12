package com.example.andriod.popularmoviev2.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.example.andriod.popularmoviev2.model.Movie;
import com.example.andriod.popularmoviev2.R;

import java.util.List;

/**
 * Object that will take passed data and display it on the view
 * Created by StandleyEugene on 6/30/2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Activity context, List<Movie> movie){
        super(context, 0,movie);
    }

    /*
        Custom getview that handles the inflation of the movie item and
        populate of the ImageView and TextView on the GridView
     */
    @Override
    public View getView(int position, View converView, ViewGroup parent){
        Movie movie = getItem(position);

        if(converView == null){
            converView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item,parent,false);
        }

        ImageView posterView = (ImageView) converView.findViewById(R.id.moviePoster_image);
        AQuery aq = new AQuery(posterView);
        aq.id(R.id.moviePoster_image).image(movie.getPosterPath()).visible();

        return converView;
    }
}
