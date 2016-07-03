package com.hackerkernel.smartalarm.adapter;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hackerkernel.smartalarm.R;
import com.hackerkernel.smartalarm.pojo.SettingListPojo;

import java.util.List;

/**
 * Created by husain on 6/26/2016.
 */
public class SettingListAdapter extends ArrayAdapter {
    private Context context;
    private List<SettingListPojo> list;
    private LayoutInflater inflater;
    private AudioManager audioManager;

    public SettingListAdapter(Context context, int resource, List<SettingListPojo> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (position == 2){
            view = inflater.inflate(R.layout.volume_row,parent,false);

            //change system volume when value of seekbar is changed
            SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekbar);
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            //set seekbar max
            seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
            //set seekbar according to the current vol
            seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM,progress,0);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }else {
            view = inflater.inflate(R.layout.setting_row,parent,false);
            TextView name = (TextView) view.findViewById(R.id.name);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            SettingListPojo pojo = list.get(position);
            name.setText(pojo.getName());
            icon.setImageResource(pojo.getIcon());
        }


        return view;
    }
}
