package app.salvatop.simplealarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.core.app.NotificationCompat;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;


public class AlarmReceiver extends BroadcastReceiver {

    private  MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        //create a date reference
        Calendar calendar;
        SimpleDateFormat formatDate, formatTime;
        calendar = Calendar.getInstance();
        formatDate = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.CANADA);
        formatTime = new SimpleDateFormat("HH:mm", Locale.CANADA);
        String date = formatDate.format(calendar.getTime());
        String time = formatTime.format(calendar.getTime());

        // create a snooze intent
        Intent snoozeIntent = new Intent(context, AlarmReceiver.class);
        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

        // create a notification
        String CHANNEL_ID = "1";
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Notifications: It's time!")
                        .setContentText(date + "  " +  time)
                        .addAction(R.drawable.snooze_icon, "SNOOZE", snoozePendingIntent);

        // create the intent for lunch the notification view
        Intent notificationIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // add the notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.notify(0, builder.build());

        //add a alarm tone
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

    }

    public void stopAlarmSound(){
        mediaPlayer.stop();
    }
}
