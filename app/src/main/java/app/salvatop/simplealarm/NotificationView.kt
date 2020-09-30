package app.salvatop.simplealarm

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

class NotificationView : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification)
    }

    fun snoozeFiveMinutes() {
        val snooze = 300000

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        val alarmManager = (getSystemService(ALARM_SERVICE) as AlarmManager)
        alarmManager[AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + snooze] = pendingIntent

        Toast.makeText(this, "Alarm snoozed by 5 minutes", Toast.LENGTH_LONG).show()
    }
}