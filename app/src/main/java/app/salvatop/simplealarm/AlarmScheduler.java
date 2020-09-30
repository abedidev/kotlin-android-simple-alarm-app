package app.salvatop.simplealarm;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;


public class AlarmScheduler extends AppCompatActivity {

    private EditText editTextTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_scheduler);

        initialize();
    }

    private void initialize() {
        editTextTimePicker = findViewById(R.id.editTextTimePicker);
        Button close = findViewById(R.id.close);

        editTextTimePicker.setOnClickListener(view -> openTimePickerDialog());
        close.setOnClickListener(view -> finish());
    }

    private void openTimePickerDialog(){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.setTitle("Set Alarm Time");
        timePickerDialog.show();
    }
    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar now = Calendar.getInstance();
            Calendar schedule = (Calendar) now.clone();
            schedule.set(Calendar.HOUR_OF_DAY, hourOfDay);
            schedule.set(Calendar.MINUTE, minute);
            schedule.set(Calendar.SECOND, 0);
            schedule.set(Calendar.MILLISECOND, 0);
            if(schedule.compareTo(now) <= 0) schedule.add(Calendar.DATE, 1);

            editTextTimePicker.setText(schedule.getTime().toString());
            setAlarm(schedule);
        }};

    private void setAlarm(Calendar calendar){
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Alarm Scheduled " + calendar.getTime(), Toast.LENGTH_LONG).show();
    }
}
