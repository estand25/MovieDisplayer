package com.example.andriod.popularmoviev2.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.example.andriod.popularmoviev2.R;
import com.example.andriod.popularmoviev2.data.MovieTableSync;
import com.example.andriod.popularmoviev2.other.Utility;
import com.example.andriod.popularmoviev2.service.FavoriteMovieService;
import com.example.andriod.popularmoviev2.service.PopularMovieService;
import com.example.andriod.popularmoviev2.service.TopRatedMovieService;

/**
 * Created by StandleyEugene on 7/29/2016.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = MovieSyncAdapter.class.getSimpleName();
    // Create the local copy of movieSyncUploader
    MovieTableSync movieTableSync;

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.v(LOG_TAG,"Starting Sync");

        // Set up the MovieSyncUploader to populate the information
        movieTableSync = new MovieTableSync(getContext());

        // Check which display option is being used and display the information
        // and populate the database with the selections information
        if (Utility.getPreferredMovieType(getContext()).equals("movie/popular")) {
            // Popular Movie Service from The Movie DB API
            getContext().startService(new Intent(getContext(),PopularMovieService.class));
        } else if (Utility.getPreferredMovieType(getContext()).equals("movie/top_rated")) {
            // Top Rated Movie Service from The Movie DB API
            getContext().startService(new Intent(getContext(), TopRatedMovieService.class));
        } else if (Utility.getPreferredMovieType(getContext()).equals("favorite_movie")) {
            // Favorite Movie Service that populate the movie table from the favorite_movie table
            getContext().startService(new Intent(getContext()
                    , FavoriteMovieService.class));
        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, false);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            //onAccountCreated(newAccount, context);
            ContentResolver.setIsSyncable(newAccount,context.getString(R.string.content_authority),1);

        }
        return newAccount;
    }
}
