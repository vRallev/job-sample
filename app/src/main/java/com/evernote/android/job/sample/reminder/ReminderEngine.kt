package com.evernote.android.job.sample.reminder

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import com.evernote.android.job.JobManager
import com.evernote.android.job.sample.App
import com.evernote.android.job.sample.MainActivity
import com.evernote.android.job.sample.R
import java.util.Random

/**
 * @author rwondratschek
 */

private const val REMINDER_ID = "REMINDER_ID"
private const val REMINDERS = "REMINDERS"

@SuppressLint("StaticFieldLeak")
object ReminderEngine {

    private val context: Context = App.instance

    private val preferences = context.getSharedPreferences("reminders", Context.MODE_PRIVATE) // poor-man's storage
    private var reminderId = preferences.getInt(REMINDER_ID, 0)
    private val reminders: MutableList<Reminder>

    private val listeners = mutableListOf<ReminderChangeListener>()

    init {
        reminders = preferences.getStringSet(REMINDERS, null)
            ?.map { Reminder.fromString(it) }
            ?.toMutableList() ?: mutableListOf()

        reminders.sort()
    }

    fun addListener(listener: ReminderChangeListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ReminderChangeListener) {
        listeners.remove(listener)
    }

    fun getReminderById(id: Int): Reminder? = reminders.firstOrNull { it.id == id }
    fun getReminders(): List<Reminder> = reminders.toList()

    fun createNewReminder(timestamp: Long): Reminder {
        reminderId++

        val reminder = Reminder(reminderId, timestamp, 0)
        reminders.add(reminder)
        reminders.sort()

        val jobId = ReminderJob.schedule(reminder)
        reminder.jobId = jobId

        preferences.edit().putInt(REMINDER_ID, reminderId).apply()
        saveReminders()

        val position = reminders.indexOf(reminder)
        listeners.forEach { it.onReminderAdded(position, reminder) }

        return reminder
    }

    fun removeReminder(position: Int) {
        removeReminder(position, true)
    }

    fun removeReminder(position: Int, cancelJob: Boolean) {
        val reminder = reminders.removeAt(position)

        if (cancelJob) {
            JobManager.instance().cancel(reminder.jobId)
        }

        saveReminders()

        listeners.forEach { it.onReminderRemoved(position, reminder) }
    }

    fun showReminder(reminder: Reminder) {
        val pendingIntent = PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)

        val channelId = "reminder"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Job Sample", NotificationManager.IMPORTANCE_LOW)
            channel.description = "Job sample"
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Reminder " + reminder.id)
            .setContentText("Hello Droidcon")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notification)
            .setShowWhen(true)
            .setColor(ContextCompat.getColor(context, R.color.accent))
            .setLocalOnly(true)
            .build()

        NotificationManagerCompat.from(context).notify(Random().nextInt(), notification)
    }

    private fun saveReminders() {
        preferences.edit().putStringSet(REMINDERS, reminders.map { it.toPersistableString() }.toHashSet()).apply()
    }

    interface ReminderChangeListener {
        fun onReminderAdded(position: Int, reminder: Reminder)

        fun onReminderRemoved(position: Int, reminder: Reminder)
    }
}