package com.evernote.android.job.sample.sync

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.evernote.android.job.sample.R
import java.io.IOException
import java.util.Random

/**
 * @author rwondratschek
 */
class SyncEngine(private val context: Context) {

    fun syncReminders() {
        // Just kidding, nothing happens actually
        SystemClock.sleep(3_000L)

        val error = Math.random() > 0.5
        val channelId = "demo-channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Job Demo", NotificationManager.IMPORTANCE_LOW)
            channel.description = "Sample app"
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Sync")
            .setContentText(if (error) "Sync failed" else "Sync successful")
            .setAutoCancel(true)
            .setChannelId(channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setShowWhen(true)
            .setColor(if (error) Color.RED else Color.GREEN)
            .setLocalOnly(true)
            .build()

        NotificationManagerCompat.from(context).notify(Random().nextInt(), notification)

        if (error) {
            throw IOException("Dummy exception")
        }
    }
}
