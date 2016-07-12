package com.example.andriod.popularmoviev2.data;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Bound my SyncAdapter (MovieSyncUploader) to the Framework
 *
 * Note: tutorial like below:
 * https://developer.android.com/training/sync-adapters/creating-sync-adapter.html#CreateSyncAdapterService
 *
 * Created by StandleyEugene on 7/11/2016.
 */
public class SyncMovieService extends Service{

    // Storage for an instance of the sync adapter
    private static MovieSyncUploader sMovieSyncUploader = null;

    // Object to use as a thread-safe lock
    private static final Object sMovieSyncUploaderLock = new Object();

    /*
        Instantiate the sync adapter object.
     */
    @Override
    public void onCreate(){
        /*
            Create the movieSynUploader as a singleton.
            Set the sync adapter as synceable
            Disallow parallel syncs
         */
        synchronized (sMovieSyncUploaderLock){
            if(sMovieSyncUploader == null){
                sMovieSyncUploader = new MovieSyncUploader(getApplicationContext(),true);
            }
        }
    }

    /*
        Return an object that allows the system to invoke
        the sync adapter.
     */
    @Override
    public IBinder onBind(Intent intent){
        /*
            Get the object that allows external processess
            to call onPerformSync(). The object is created in the base class
            code when the SyncAdapter constructors call super()
         */
        return sMovieSyncUploader.getSyncAdapterBinder();
    }

}
