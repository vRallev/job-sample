package com.evernote.android.job.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.evernote.android.job.sample.reminder.Reminder
import com.evernote.android.job.sample.reminder.ReminderEngine

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random
import java.util.concurrent.TimeUnit

/**
 * @author rwondratschek
 */
class MainActivity : AppCompatActivity(), ReminderEngine.ReminderChangeListener {

    private var reminders: List<Reminder> = ReminderEngine.getReminders()

    private lateinit var adapter: ReminderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ReminderEngine.addListener(this)

        adapter = ReminderAdapter()

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                ReminderEngine.removeReminder(viewHolder.adapterPosition)
            }
        }

        findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter
            ItemTouchHelper(swipeCallback).attachToRecyclerView(this)
        }


        findViewById<View>(R.id.fab).setOnClickListener {
            ReminderEngine.createNewReminder(
                timestamp = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10) + Random().nextLong() % REMINDER_LIMIT
            )
        }
    }

    override fun onDestroy() {
        ReminderEngine.removeListener(this)
        super.onDestroy()
    }

    override fun onReminderAdded(position: Int, reminder: Reminder) {
        runOnUiThread {
            reminders = ReminderEngine.getReminders()
            adapter.notifyItemInserted(position)
        }
    }

    override fun onReminderRemoved(position: Int, reminder: Reminder) {
        runOnUiThread {
            reminders = ReminderEngine.getReminders()
            adapter.notifyItemRemoved(position)
        }
    }

    private inner class ReminderAdapter : RecyclerView.Adapter<ReminderViewHolder>() {

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
            return ReminderViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
        }

        override fun getItemViewType(position: Int): Int = R.layout.reminder_item
        override fun getItemCount(): Int = reminders.size

        override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
            holder.textView.text = dateFormat.format(Date(reminders[position].timestamp))
            holder.textView.setOnClickListener { Toast.makeText(this@MainActivity, "bla", Toast.LENGTH_SHORT).show() }
        }
    }

    private class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text)
    }

    companion object {
        // private static final long REMINDER_LIMIT = TimeUnit.HOURS.toMillis(2);
        private val REMINDER_LIMIT = TimeUnit.MINUTES.toMillis(1)
    }
}
