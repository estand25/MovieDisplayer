package com.example.andriod.popularmoviev2.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.adapter.PageAdapter;
import com.example.andriod.popularmoviev2.data.MovieSyncUploader;

import java.net.URI;

/**
 * Note: Added fragment tab to DetailActivity I got it from here
 * https://www.simplifiedcoding.net/android-tablayout-example-using-viewpager-fragments/
 *
 * DetailActivity that holds the tablayout for the three detail
 * fragement
 */
public class DetailActivity extends AppCompatActivity{

    /**
     * OnCreate set-up the TabLayout with individual tab names and floating action button for mail
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailMovieFragment.MOVIE_DETAILS_URI, getIntent().getData());

            DetailMovieFragment fragment = new DetailMovieFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

        // Action button for Emails
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This should generate a email with the current movie's detail view information
                // Note: It isn't working, but I will figure it out sometime.
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                //emailIntent.setData(Uri.parse("mailto:")); // Only emails
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Popular Movie App -- Information");
                if(emailIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(emailIntent);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
