package com.example.andriod.popularmoviev2.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.andriod.popularmoviev2.R;

public class MainActivity extends AppCompatActivity implements MovieFragment.Callback{
    // Constants
    // The authority for the sync adapter's (MovieSyncUploader) content provider
    public static final String AUTHORITY = "com.example.andriod.popularmoviev2";

    // An account type, in the form of a domin name
    public static final String ACCOUNT_TYPE = "example.com";

    private static final String MOVIEDETAILFRAGMENT_TAG = "DFTAG";

    // The account name
    public static final String ACCOUNT = "dummyaccount";

    // Instance fields
    Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This should generate a email with the current list of each
                // Most popular movie or Top Rated movie.
                // Note: It isn't working, but I will figure it out sometime.
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Popular Movie App -- Information");
                if(emailIntent.resolveActivity(getPackageManager()) != null){
                    startActivity(emailIntent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

    public static Account CreateMovieSyncAccount(Context context){
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);

        // Get an instace of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        /*
            Add the account and account type, no password and user data
            If successful, return the account object, otherwise report an error
         */
        if(accountManager.addAccountExplicitly(newAccount,null,null)){
            ContentResolver.setIsSyncable(newAccount,AUTHORITY,1);

            return newAccount;
        }else {
            Log.e("Error"," User dummy data not successfully created");
        }

        return null;
    }

    @Override
    public void onItemSelected(Uri contentUri){
        //if(mTwoPane){
        //    // In two-pane mode, show the detail view in this activity by
        //    // adding or replacing the detail fragment usig a
        //    // fragment transaction.
        //    Bundle args = new Bundle();
        //    args.putParcelable(DetailMovieFragment.MOVIE_DETAIL_URI,contentUri);
        //
        //    DetailMovieFragment fragment = new DetailMovieFragment();
        //        fragment.setArguments(args);

        //    getSupportFragmentManager().beginTransaction()
        //            .replace(R.id.fragment, fragment)
        //            .commit();
        //} else {
        Intent intent = new Intent(this, DetailActivity.class)
                .setData(contentUri);
        startActivity(intent);
        //}
    }
}