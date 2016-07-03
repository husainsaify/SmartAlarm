package com.hackerkernel.smartalarm.reciever;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.hackerkernel.smartalarm.fragments.AlarmFragment;
import com.hackerkernel.smartalarm.service.AlarmService;

/**
 * Created by husain on 6/24/2016.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {
    private static Ringtone ringtone;

    @Override
    public void onReceive(Context context, Intent intent) {

        //this will update the UI with message
        AlarmFragment instance = AlarmFragment.getInstance();
        instance.setAlarmText("Alarm! Wake up! Wake up!");

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (alarmUri == null){
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        ringtone = RingtoneManager.getRingtone(context,alarmUri);
        ringtone.play();

        //This will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        startWakefulService(context,intent.setComponent(comp));
        setResultCode(Activity.RESULT_OK);
    }

    public static void stopRintone(){
        ringtone.stop();
    }
}
