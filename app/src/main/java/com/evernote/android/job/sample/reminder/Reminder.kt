package com.evernote.android.job.sample.reminder

/**
 * @author rwondratschek
 */
data class Reminder(
    val id: Int,
    val timestamp: Long,
    var jobId: Int
) : Comparable<Reminder> {

    fun toPersistableString(): String = "$id/$timestamp/$jobId"
    override fun compareTo(other: Reminder): Int = timestamp.compareTo(other.timestamp)

    companion object {
        fun fromString(value: String): Reminder {
            val split = value.split("/")
            if (split.size != 3) throw IllegalArgumentException()

            return Reminder(split[0].toInt(), split[1].toLong(), split[2].toInt())
        }
    }
}
