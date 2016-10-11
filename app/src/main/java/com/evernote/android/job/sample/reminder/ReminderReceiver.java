package com.evernote.android.job.sample.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * @author rwondratschek
 */
public class ReminderReceiver extends BroadcastReceiver {

    private static final String EXTRA_ID = "EXTRA_ID";

    public static void schedule(Context context, Reminder reminder) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_ID, reminder.getId());

        int requestCode = reminder.getId();
        int flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, reminder.getTimestamp(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.RTC_WAKEUP, reminder.getTimestamp(), pendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, reminder.getTimestamp(), pendingIntent);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(EXTRA_ID, -1);
        if (id < 0) {
            return;
        }

        Reminder reminder = ReminderEngine.instance().getReminderById(id);
        if (reminder != null) {
            ReminderEngine.instance().showReminder(reminder);
        }
    }
}
