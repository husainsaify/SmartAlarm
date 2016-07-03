package com.hackerkernel.smartalarm.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.hackerkernel.smartalarm.R;
import com.hackerkernel.smartalarm.activity.MainActivity;
import com.hackerkernel.smartalarm.storage.Database;
import com.hackerkernel.smartalarm.storage.MySharedPreferences;

/**
 * Created by husain on 6/24/2016.
 */
public class AlarmService extends IntentService {
    private static final String TAG = AlarmService.class.getSimpleName();
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sendNotification("Wake up! Wake Up!");
    }

    private void sendNotification(String msg) {
        Log.d(TAG,"HUS: Sending notification");
        alarmNotificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this,0,
                new Intent(this, MainActivity.class),0);

        NotificationCompat.Builder alarmBuilder = new NotificationCompat.Builder(this);
        alarmBuilder.setContentTitle("Smart Alarm")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alarmBuilder.build());

        //cancel alarm from sp & empty alarm message
        MySharedPreferences.getInstance(this).setAlarm(false);
        MySharedPreferences.getInstance(this).setAlarmMessage(" ");
    }
}
