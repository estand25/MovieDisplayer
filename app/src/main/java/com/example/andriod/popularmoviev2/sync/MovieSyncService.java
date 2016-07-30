package com.example.andriod.popularmoviev2.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by StandleyEugene on 7/29/2016.
 */
public class MovieSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static MovieSyncAdapter mMovieSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("MovieSyncService", "onCreate - MovieSyncService");
        synchronized (sSyncAdapterLock) {
            if (mMovieSyncAdapter == null) {
                mMovieSyncAdapter = new MovieSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMovieSyncAdapter.getSyncAdapterBinder();
    }
}