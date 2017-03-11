package com.evernote.android.job.sample.reminder;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import com.evernote.android.job.JobManager;
import com.evernote.android.job.sample.MainActivity;
import com.evernote.android.job.sample.R;

import net.vrallev.android.context.AppContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author rwondratschek
 */
public class ReminderEngine {

    private static final String REMINDER_ID = "REMINDER_ID";
    private static final String REMINDERS = "REMINDERS";

    @SuppressLint("StaticFieldLeak")
    private static final ReminderEngine INSTANCE = new ReminderEngine();

    public static ReminderEngine instance() {
        return INSTANCE;
    }

    private final Context mContext;
    private final SharedPreferences mPreferences;

    private final List<Reminder> mReminders;

    private final List<ReminderChangeListener> mListeners;

    private int mReminderId;

    private ReminderEngine() {
        mContext = AppContext.get();
        mPreferences = mContext.getSharedPreferences("reminders", Context.MODE_PRIVATE); // poor-man's storage
        mReminderId = mPreferences.getInt(REMINDER_ID, 0);

        mReminders = new ArrayList<>();
        mListeners = new ArrayList<>();

        Set<String> reminders = mPreferences.getStringSet(REMINDERS, null);
        if (reminders != null) {
            for (String value : reminders) {
                mReminders.add(Reminder.fromString(value));
            }
        }
        Collections.sort(mReminders);
    }

    public void addListener(ReminderChangeListener listener) {
        mListeners.add(listener);
    }

    public void removeListener(ReminderChangeListener listener) {
        mListeners.remove(listener);
    }

    public List<Reminder> getReminders() {
        return Collections.unmodifiableList(mReminders);
    }

    @Nullable
    public Reminder getReminderById(int id) {
        for (Reminder reminder : mReminders) {
            if (reminder.getId() == id) {
                return reminder;
            }
        }
        return null;
    }

    public Reminder createNewReminder(long timestamp) {
        mReminderId++;

        Reminder reminder = new Reminder(mReminderId, timestamp);
        mReminders.add(reminder);
        Collections.sort(mReminders);

        int jobId = ReminderJob.schedule(reminder);
        reminder.setJobId(jobId);

        mPreferences.edit().putInt(REMINDER_ID, mReminderId).apply();
        saveReminders();

        int position = mReminders.indexOf(reminder);
        for (ReminderChangeListener listener : mListeners) {
            listener.onReminderAdded(position, reminder);
        }

        return reminder;
    }

    public void removeReminder(int position) {
        removeReminder(position, true);
    }

    /*package*/ void removeReminder(int position, boolean cancelJob) {
        Reminder reminder = mReminders.remove(position);

        if (cancelJob) {
            JobManager.instance().cancel(reminder.getJobId());
        }

        saveReminders();

        for (ReminderChangeListener listener : mListeners) {
            listener.onReminderRemoved(position, reminder);
        }
    }

    public void showReminder(Reminder reminder) {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(mContext)
                .setContentTitle("Reminder " + reminder.getId())
                .setContentText("Attend MTC Munich!")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification)
                .setShowWhen(true)
                .setColor(ContextCompat.getColor(mContext, R.color.accent))
                .setLocalOnly(true)
                .build();

        NotificationManagerCompat.from(mContext).notify(new Random().nextInt(), notification);
    }

    private void saveReminders() {
        Set<String> reminders = new HashSet<>();
        for (Reminder item : mReminders) {
            reminders.add(item.toPersistableString());
        }

        mPreferences.edit().putStringSet(REMINDERS, reminders).apply();
    }

    public interface ReminderChangeListener {
        void onReminderAdded(int position, Reminder reminder);

        void onReminderRemoved(int position, Reminder reminder);
    }
}
