package app.salvatop.simplealarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import androidx.databinding.DataBindingUtil
import app.salvatop.simplealarm.databinding.ActivityAlarmSchedulerBinding

class AlarmScheduler : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmSchedulerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_scheduler)

        binding.editTextTimePicker.setOnClickListener(View.OnClickListener { view: View? -> openTimePickerDialog() })
        binding.close.setOnClickListener { view: View? -> finish() }
    }

    private fun openTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
                this,
                onTimeSetListener,
                calendar[Calendar.HOUR_OF_DAY],
                calendar[Calendar.MINUTE],
                true)
        timePickerDialog.setTitle("Set Alarm Time")
        timePickerDialog.show()
    }

    private var onTimeSetListener = OnTimeSetListener { view, hourOfDay, minute ->
        val now = Calendar.getInstance()
        val schedule = now.clone() as Calendar

        schedule[Calendar.HOUR_OF_DAY] = hourOfDay
        schedule[Calendar.MINUTE] = minute
        schedule[Calendar.SECOND] = 0
        schedule[Calendar.MILLISECOND] = 0

        if (schedule <= now) schedule.add(Calendar.DATE, 1)

        binding.editTextTimePicker.setText(schedule.time.toString())
        setAlarm(schedule)
    }

    private fun setAlarm(calendar: Calendar) {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = (getSystemService(ALARM_SERVICE) as AlarmManager)

        alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent

        Toast.makeText(this, "Alarm Scheduled " + calendar.time, Toast.LENGTH_LONG).show()
    }
}