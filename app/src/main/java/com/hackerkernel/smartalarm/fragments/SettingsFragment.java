package com.hackerkernel.smartalarm.fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hackerkernel.smartalarm.R;
import com.hackerkernel.smartalarm.adapter.SettingListAdapter;
import com.hackerkernel.smartalarm.pojo.SettingListPojo;
import com.hackerkernel.smartalarm.storage.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SettingsFragment extends Fragment {
    @Bind(R.id.listview) ListView mListView;
    private MySharedPreferences sp;
    private List<Uri> ringtonsUriList;
    private List<String> ringtonsNameList;

    List<SettingListPojo> list;
    int icons[] = {
            R.drawable.ic_alarm_white_24dp,
            R.drawable.ic_music_note_white_24dp,
            R.drawable.ic_volume_up_white_24dp,
            R.drawable.ic_snooze_white_24dp,
            R.drawable.ic_hearing_white_24dp
    };
    String[] title = new String[]{
            "Alarm",
            "Sound",
            "Volume",
            "Snooze",
            "Wake up phase"
    };

    public SettingsFragment() {
        list = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            SettingListPojo pojo = new SettingListPojo();
            pojo.setIcon(icons[i]);
            pojo.setName(title[i]);
            list.add(pojo);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = MySharedPreferences.getInstance(getActivity());
        ringtonsNameList = new ArrayList<>();
        ringtonsUriList = new ArrayList<>();

        //get list of ringtones & add to rington list
        RingtoneManager ringtoneManager = new RingtoneManager(getActivity());
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmCursor = ringtoneManager.getCursor();
        int alarmCount = alarmCursor.getCount();
        if (alarmCount == 0 && !alarmCursor.moveToFirst()){
            return;
        }
        Uri[] alarms = new Uri[alarmCount];
        while (!alarmCursor.isAfterLast() && alarmCursor.moveToNext()){
            int currentPosition = alarmCursor.getPosition();
            alarms[currentPosition] = ringtoneManager.getRingtoneUri(currentPosition);
        }
        alarmCursor.close();

        for (Uri alarm : alarms) {
            //add ringtone to ringtoneUriList
            ringtonsUriList.add(alarm);
            //get the name of ringtone
            Ringtone ringtone = RingtoneManager.getRingtone(getActivity(),alarm);
            String title = ringtone.getTitle(getActivity());
            ringtonsNameList.add(title);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        SettingListAdapter adapter = new SettingListAdapter(getActivity().getBaseContext(),0,list);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4){
                    showWakeupPhaseDialog(inflater);
                }else if (position == 1){
                    showSoundDialog();
                }
            }
        });

        return view;
    }

    private void showSoundDialog() {
        ArrayAdapter<String> itensAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.select_dialog_item, ringtonsNameList);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setAdapter(itensAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //set alarm ringtone
                RingtoneManager.setActualDefaultRingtoneUri(getActivity(),RingtoneManager.TYPE_ALARM,ringtonsUriList.get(which));
                Toast.makeText(getActivity(),"Success!! Alarm ringtone updated",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showWakeupPhaseDialog(LayoutInflater inflater){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = inflater.inflate(R.layout.wake_up_phase_row,null);
        final RadioGroup wakeUpRadioGroup = (RadioGroup) view.findViewById(R.id.minutes_group);
        //get wake up phase from sp and mark that radio button checked
        int wakeUpPhase = sp.getWakeUpPhase();
        switch (wakeUpPhase){
            case 10:
                wakeUpRadioGroup.check(R.id.minutes_10);
                break;
            case 20:
                wakeUpRadioGroup.check(R.id.minutes_20);
                break;
            case 30:
                wakeUpRadioGroup.check(R.id.minutes_30);
                break;
            case 40:
                wakeUpRadioGroup.check(R.id.minutes_40);
                break;
            case 50:
                wakeUpRadioGroup.check(R.id.minutes_50);
                break;
            case 60:
                wakeUpRadioGroup.check(R.id.minutes_60);
                break;
        }
        builder.setView(view)
                .setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get the check radio button id and save appropriate wakeupphase in sp
                        int checkedRadioButtonId = wakeUpRadioGroup.getCheckedRadioButtonId();
                        switch (checkedRadioButtonId){
                            case R.id.minutes_10:
                                sp.setWakeUpPhase(10);
                                break;
                            case R.id.minutes_20:
                                sp.setWakeUpPhase(20);
                                break;
                            case R.id.minutes_30:
                                sp.setWakeUpPhase(30);
                                break;
                            case R.id.minutes_40:
                                sp.setWakeUpPhase(40);
                                break;
                            case R.id.minutes_50:
                                sp.setWakeUpPhase(50);
                                break;
                            case R.id.minutes_60:
                                sp.setWakeUpPhase(60);
                                break;
                        }

                        Toast.makeText(getActivity(),"Wake up phase updated successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
