package com.example.andriod.popularmoviev2.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.andriod.popularmoviev2.R;

public class MainActivity extends AppCompatActivity implements MovieFragment.Callback{
    private static final String MOVIEDETAILFRAGMENT_TAG = "DFTAG";
    public boolean mTwoPane;

    /**
     * On Create set-up the Toolbar and determine if we using 1 or 2 pane for the app
     * @param savedInstanceState - saveInstanceState Bundle that live for the lifetime of activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailMovieFragment(), MOVIEDETAILFRAGMENT_TAG)
                        .commit();
            }

            // Create new instance of the MovieFragment, but get the MovieFragment from
            // the getSupportFragmentManager found fragment
            MovieFragment movieFragment = ((MovieFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.container));
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);

            // Create new instance of the MovieFragment, but get the MovieFragment from
            // the getSupportFragmentManager found fragment
            MovieFragment movieFragment = ((MovieFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment));
        }
    }

    /**
     * Inflates the menu options and return if that happens
     * @param menu - menu object
     * @return - Returns boolean if menu was created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Does something when the associated menu option is selected
     * @param item - The selected menu item
     * @return - Returns boolean if menu item is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * OnItemSelected is what happens after an movie poster is selected
     * @param contentUri - The Uri used by the app to query/insert/delete table data
     */
    @Override
    public void onItemSelected(Uri contentUri){
        if(mTwoPane){
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment usig a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailMovieFragment.MOVIE_DETAIL_URI,contentUri);

            DetailMovieFragment fragment = new DetailMovieFragment();
                fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                        .setData(contentUri);
            startActivity(intent);
        }
    }
}
