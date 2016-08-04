package com.example.andriod.popularmoviev2.sync;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
    // Alarm Manager, which provides access to the system alarm services
    private AlarmManager alarmManager;

    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;

    /**
     * onReceive of the Broadcast I trigger the PopulateAllMovie IntentService
     *
     * @param context - The current app context
     * @param intent - The current app intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // Start the PopulateAllMovie Intent
        Intent service = new Intent(context, PopulateAllMovies.class);
        startWakefulService(context, service);
    }

    /**
     * Set a repeating alarm that run ever 30 to 60 minutes
     *
     * @param context - The current app context
     */
    public void setAlarm(Context context){
        // Get the Alarm Manager from the context Alarm Service
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Create an Intent for the PopulateAllMovie class
        Intent intent = new Intent(context, PopulateAllMovies.class);

        // Create a PendingIntent for the broadcast and intent
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        // Wake up the device to fire the alarm in 30-60 minutes
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HOUR,
                alarmIntent);
    }
}
