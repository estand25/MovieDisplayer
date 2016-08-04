package com.example.andriod.popularmoviev2.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.other.Constants;

/**
 * DetailActivity that associated to DetailMovieFragment
 */
public class DetailActivity extends AppCompatActivity{

    /**
     * On the creation of DetailActivity
     *
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     */
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

            // Create a new instance of the DetailMovieFragment and set the Bundle value
            DetailMovieFragment fragment = new DetailMovieFragment();
            fragment.setArguments(arguments);

            // Get the current fragment and add the layout and fragment to it and commit it
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container,fragment)
                    .commit();
        }

        // Set Constants.cContext from applicationContext
        Constants.cConetext = getApplicationContext();
    }
}
