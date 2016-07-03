package com.hackerkernel.smartalarm.fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hackerkernel.smartalarm.R;
import com.hackerkernel.smartalarm.reciever.AlarmReceiver;
import com.hackerkernel.smartalarm.storage.Database;
import com.hackerkernel.smartalarm.storage.MySharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmFragment extends Fragment {
    private static final String TAG = AlarmFragment.class.getSimpleName();
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static AlarmFragment inst;
    private MySharedPreferences sp;
    private Database db;

    @Bind(R.id.alarm_start_button) ToggleButton mStartBtn;
    @Bind(R.id.alarm_text) TextView mTextView;
    @Bind(R.id.alarm_timepicker) TimePicker mTimePicker;

    public AlarmFragment() {

    }

    public static AlarmFragment getInstance(){
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        sp = MySharedPreferences.getInstance(getActivity());
        db = new Database(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_alarm, container, false);
        ButterKnife.bind(this,view);

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        if (sp.isAlarmSet()){
            mStartBtn.setText("Stop Alarm");
            //store time in sp
            setAlarmText(sp.getAlarmMessage());
        }else {
            mStartBtn.setText("Start Alarm");
            setAlarmText(sp.getAlarmMessage());
        }

        return view;
    }

    private void setAlarm() {
        if (mStartBtn.isChecked()){
            //save alarm is sp
            sp.setAlarm(true);
            mStartBtn.setText("Stop Alarm");

            Log.d(TAG,"HUS: Alarm is on");
            Calendar calendar = Calendar.getInstance();
            if (Build.VERSION.SDK_INT >= 23){
                calendar.set(Calendar.HOUR_OF_DAY,mTimePicker.getHour());
                calendar.set(Calendar.MINUTE,mTimePicker.getMinute());
                String nowTime = calTimeFromTimePicker(mTimePicker.getHour(),mTimePicker.getMinute());
                String afterTime = calTimeFromTimePicker(mTimePicker.getHour(),mTimePicker.getMinute()+sp.getWakeUpPhase());
                setAlarmText("Wake up between "+nowTime+" - "+afterTime);
                //store time in sp
                sp.setAlarmMessage("Wake up between "+nowTime+" - "+afterTime);
            }else {
                calendar.set(Calendar.HOUR_OF_DAY,mTimePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE,mTimePicker.getCurrentMinute());
                String nowTime = calTimeFromTimePicker(mTimePicker.getCurrentHour(),mTimePicker.getCurrentMinute());
                String afterTime = calTimeFromTimePicker(mTimePicker.getCurrentHour(),mTimePicker.getCurrentMinute()+sp.getWakeUpPhase());
                setAlarmText("Wake up between "+nowTime+" - "+afterTime);
                //store time in sp
                sp.setAlarmMessage("Wake up between "+nowTime+" - "+afterTime);
            }

            //set time to alarm text
            setAlarmText(sp.getAlarmMessage());

            //get todays date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
            String todayDate = dateFormat.format(System.currentTimeMillis());

            //get from timestamp
            long fromTimeStamp = calendar.getTimeInMillis();

            //get to timestamp
            long toTimeStamp = System.currentTimeMillis();

            //store these bitches in database
            db.addSleepTime(String.valueOf(fromTimeStamp),String.valueOf(toTimeStamp),todayDate);

            //set an pending intent to run time its alarm time
            Intent myIntent = new Intent(getActivity(),AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getActivity(),0,myIntent,0);
            alarmManager.set(AlarmManager.RTC,calendar.getTimeInMillis(),pendingIntent);

        }else {
            //save alarm is sp
            sp.setAlarm(false);
            mStartBtn.setText("Start Alarm");
            alarmManager.cancel(pendingIntent);
            setAlarmText("Alarm Stopped!");
            Log.d(TAG,"HUS: stop alarm");
            sp.setAlarmMessage(" ");

            AlarmReceiver.stopRintone();
        }

    }

    public void setAlarmText(String alarmText) {
        if (mTextView != null){
            mTextView.setText(alarmText);
        }
    }

    private String calTimeFromTimePicker(int mhour,int mMinute){
        int hour = mhour;
        int minutes = mMinute;
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        if (minutes > 60){
            minutes -= 60;
            hour += 1;
        }

        String min = "";
        if (minutes < 10)
            min = "0" + minutes ;
        else
            min = String.valueOf(minutes);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hour).append(':')
                .append(min ).append(" ").append(timeSet).toString();
        return aTime;
    }


}
