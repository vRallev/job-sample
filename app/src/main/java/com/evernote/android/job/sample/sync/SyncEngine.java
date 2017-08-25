package com.evernote.android.job.sample.sync;

import android.app.Notification;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.evernote.android.job.sample.R;

import net.vrallev.android.context.AppContext;

import java.io.IOException;
import java.util.Random;

/**
 * @author rwondratschek
 */
@SuppressWarnings("WeakerAccess")
public class SyncEngine {

    public void syncReminders() throws IOException {
        // Just kidding, nothing happens actually
        SystemClock.sleep(3_000L);

        boolean error = Math.random() > 0.5;

        Notification notification = new NotificationCompat.Builder(AppContext.get())
                .setContentTitle("Sync")
                .setContentText(error ? "Sync failed" : "Sync successful")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setShowWhen(true)
                .setColor(error ? Color.RED : Color.GREEN)
                .setLocalOnly(true)
                .build();

        NotificationManagerCompat.from(AppContext.get()).notify(new Random().nextInt(), notification);

        if (error) {
            throw new IOException("Dummy exception");
        }
    }
}
