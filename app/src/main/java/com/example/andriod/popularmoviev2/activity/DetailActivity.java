package com.example.andriod.popularmoviev2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.other.Constants;

/**
 * DetailActivity that holds the tablayout for the three detail
 *
 * fragement
 */
public class DetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null){
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.MOVIE_DETAIL_URI,getIntent().getData());

            DetailMovieFragment fragment = new DetailMovieFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container,fragment)
                    .commit();
        }
    }
}
