package com.hackerkernel.smartalarm.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hackerkernel.smartalarm.R;
import com.hackerkernel.smartalarm.pojo.SleepTimePojo;
import com.hackerkernel.smartalarm.storage.Database;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {
    private Database db;
    @Bind(R.id.graph_view) GraphView mGraphView;
    @Bind(R.id.placeholder) TextView mPlaceholder;

    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new Database(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        ButterKnife.bind(this,view);

        List<SleepTimePojo> list = db.getSleepTime();

        if (list.size() >= 3){
            //show graph
            mPlaceholder.setVisibility(View.GONE);
            mGraphView.setVisibility(View.VISIBLE);
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

            String[] graphDateLabel = new String[5];
            String[] graphHourLabel = new String[5];
            for (int i = 0; i < list.size(); i++) {
                SleepTimePojo pojo = list.get(i);
                //get from and to timestamp
                long fromTimestamp = Long.parseLong(pojo.getFrom());
                long toTimestamp = Long.parseLong(pojo.getTo());
                String date = pojo.getDate();
                Log.d("HUS","HUS: "+date);

                //get the difference of from and to timestamp
                long sleepTimestamp = fromTimestamp - toTimestamp;

                series.appendData(new DataPoint(i,sleepTimestamp),true,5);

                //add date to graph vertical label
                graphDateLabel[i] = date;
                //add houre to graph horizontal lable
                graphHourLabel[i] = getHoursDifference(fromTimestamp,toTimestamp);
            }
            //add label(date) to graphs
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(mGraphView);
            staticLabelsFormatter.setHorizontalLabels(graphHourLabel);
            staticLabelsFormatter.setVerticalLabels(graphDateLabel);
            mGraphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

            //add series to graph
            mGraphView.addSeries(series);

        }else {
            //hide graph show placeholder
            mPlaceholder.setVisibility(View.VISIBLE);
            mGraphView.setVisibility(View.GONE);
        }

        return view;
    }

    /*
    * Function to get hourse between 2 timestamp
    * */
    public String getHoursDifference(long from,long to){
        Date fromDate = new Date(from);
        Date toDate = new Date(to);
        long dif = fromDate.getTime() - toDate.getTime();
        long difHour = dif / (60 * 60 * 1000);
        long difMinute = dif / (60 * 1000) % 60;
        //return hours only when it is greater then 1 else return min
        if (difHour >= 1){
            return difHour+" Hr";
        }else {
            return difMinute+" Min";
        }
    }

}
