package com.evernote.android.job.sample.reminder;

import android.support.annotation.NonNull;

/**
 * @author rwondratschek
 */
public class Reminder implements Comparable<Reminder> {

    /*package*/ static Reminder fromString(String value) {
        String[] split = value.split("/");
        if (split.length != 3) {
            return null;
        }
        return new Reminder(Integer.parseInt(split[0]), Long.parseLong(split[1]), Integer.parseInt(split[2]));
    }

    private final int mId;
    private final long mTimestamp;

    private int mJobId;

    /*package*/ Reminder(int id, long timestamp) {
        this(id, timestamp, -1);
    }

    private Reminder(int id, long timestamp, int jobId) {
        mId = id;
        mTimestamp = timestamp;
        mJobId = jobId;
    }

    public int getId() {
        return mId;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    /*package*/ int getJobId() {
        return mJobId;
    }

    /*package*/ void setJobId(int jobId) {
        mJobId = jobId;
    }

    /*package*/ String toPersistableString() {
        return mId + "/" + mTimestamp + "/" + mJobId;
    }

    @Override
    public int compareTo(@NonNull Reminder o) {
        return compare(mTimestamp, o.mTimestamp);
    }

    private static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}
