package aitfinalproject.wakeup.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Timer;
import java.util.TimerTask;

import aitfinalproject.wakeup.R;


public class StopWatchFragment extends Fragment {
    public static String TAG = "StopWatchFragment";
    private TextView tvRunTime;
    private Timer timerStopWatch;
    private long startTime = 0;
    private ToggleButton btnStart;
    private Button btnReset;
    private Button btnLap;
    private RunTimeTimerTask timerTask;
    private int checkClicked = 0;
    private int counter = 0;
    private LinearLayout layoutContainer;

    public StopWatchFragment() {

    }

    public static StopWatchFragment newInstance() {
        StopWatchFragment fragment = new StopWatchFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_stop_watch, container, false);
        tvRunTime = (TextView) view.findViewById(R.id.tvStopWatch);


        layoutContainer = (LinearLayout) view.findViewById(
                R.id.layoutContainer);

      LayoutInflater inflateMe = (LayoutInflater) getLayoutInflater(savedInstanceState);

        btnLap = (Button) view.findViewById(R.id.btnLap);
        btnLap.setEnabled(false);
        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    long diff = System.currentTimeMillis() - startTime;
                    long diffInSec = diff / 1000;
                    long diffMsMod = diff % 1000;

                    LayoutInflater inflaterMe = LayoutInflater.from(getActivity());
                    counter++;
                    View viewer = inflaterMe.inflate(R.layout.stop_watch_lap_time, layoutContainer, false);
                    TextView tvLapTime = (TextView) viewer.findViewById(R.id.tvLapTime);
                    tvLapTime.setText("Lap " + counter + " : " + String.format("%1$02d:%2$02d", diffInSec, diffMsMod));

                    layoutContainer.addView(viewer);

            }
        });

        btnReset = (Button) view.findViewById(R.id.btnReset);
        btnReset.setEnabled(false);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvRunTime.setText("00:00:00");
                checkClicked = 0;
                counter = 0;
                btnReset.setEnabled(false);
                btnStart.setChecked(false);
                btnLap.setEnabled(false);
                layoutContainer.removeAllViews();
            }
        });

        btnStart =
                (ToggleButton) view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(btnStart.isChecked() && checkClicked == 0) {
                        startTime = System.currentTimeMillis();
                        startTime();
                        btnLap.setEnabled(true);
                        checkClicked++;
                    }else if(!btnStart.isChecked()){
                        btnReset.setEnabled(true);
                        btnLap.setEnabled(false);
                        pauseTime();
                    }else if(btnStart.isChecked()){
                       startTime();
                        btnLap.setEnabled(true);
                        btnReset.setEnabled(false);
                    }
                }
        });
        return view;
    }

    public void startTime(){
        if (timerStopWatch != null) {
            timerStopWatch.cancel();
        }
        timerStopWatch = new Timer();
        timerTask = new RunTimeTimerTask();
        timerStopWatch.schedule(timerTask, 0, 10);
    }

    public void pauseTime(){
        timerStopWatch.cancel();
        timerTask.cancel();
    }

    private class RunTimeTimerTask extends TimerTask {
        private long currentTime;
        private long diff ;
        private long min;
        private long sec;
        private long msec;

        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                        currentTime = System.currentTimeMillis();
                        diff = currentTime - startTime;
                         min = diff / 60000;
                    //TODO: fix so that it doesn't go over 60 seconds
                         sec = diff / 1000;
                         msec = (diff % 1000) / 10;

                        //fix the time so that when it hits 60 the sec resets to 00
                        String time = String.format("%1$02d:%2$02d:%3$02d",
                                min, sec, msec);
                        tvRunTime.setText(time);
                }
            });
        }
    }

}
