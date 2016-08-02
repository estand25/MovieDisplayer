package com.example.andriod.popularmoviev2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.andriod.popularmoviev2.service.PopulateAllMovies;

/**
 * Handle the wakefulness and start my populate movie IntentService
 *
 * Note: Got the idea from the post
 * http://stackoverflow.com/questions/25601973/how-to-start-an-intentservice-from-a-wakefulbroadcastreceiver
 *
 * Created by StandleyEugene on 8/1/2016.
 */
public class StarterReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Start the PopulateAllMovie Intent
        Intent service = new Intent(context, PopulateAllMovies.class);
        startWakefulService(context, service);
    }
}
