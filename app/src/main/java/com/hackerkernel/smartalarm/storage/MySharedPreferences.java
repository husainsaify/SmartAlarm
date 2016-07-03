package com.hackerkernel.smartalarm.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by husain on 6/24/2016.
 */
public class MySharedPreferences {
    //instance field
    private static SharedPreferences mSharedPreference;
    private static MySharedPreferences mInstance = null;
    private static Context mContext;


    //Shared Preference key
    private String KEY_PREFERENCE_NAME = "smartAlarm";

    //private keyS
    private String KEY_DEFAULT = null;

    //user details keys
    private String KEY_ALARM = "id",
                KEY_ALARM_MESSAGE = "message",
                KEY_WAKE_UP_PHASE = "wake_up_phase";

    public MySharedPreferences() {
        mSharedPreference = mContext.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static MySharedPreferences getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new MySharedPreferences();
        }
        return mInstance;
    }



    //Method to store user Mobile number
    public void setAlarm(boolean status) {
        mSharedPreference.edit().putBoolean(KEY_ALARM, status).apply();
    }

    //Method to get User mobile number
    public boolean isAlarmSet() {
        return mSharedPreference.getBoolean(KEY_ALARM, false);
    }

    //Method to store user Mobile number
    public void setAlarmMessage(String message) {
        mSharedPreference.edit().putString(KEY_ALARM_MESSAGE, message).apply();
    }

    //Method to get User mobile number
    public String getAlarmMessage() {
        return mSharedPreference.getString(KEY_ALARM_MESSAGE, "");
    }

    public void setWakeUpPhase(int size){
        mSharedPreference.edit().putInt(KEY_WAKE_UP_PHASE,size).apply();
    }

    public int getWakeUpPhase(){
        return mSharedPreference.getInt(KEY_WAKE_UP_PHASE,30);
    }
}
