package com.example.andriod.popularmoviev2.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * The service which allows the sync adapter framework to access the authenticator.
 * Note: Taken from Sunshine
 * Created by StandleyEugene on 7/29/2016.
 */
public class MovieAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private MovieAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new MovieAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
