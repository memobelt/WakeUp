package aitfinalproject.wakeup.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private boolean isRunning = false;
    private boolean isLap = false;
    ToggleButton btnStart;
    ToggleButton btnReset;
    public StopWatchFragment() {

    }

    public static StopWatchFragment newInstance() {
        StopWatchFragment fragment = new StopWatchFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stop_watch, container, false);
        tvRunTime = (TextView) view.findViewById(R.id.tvStopWatch);


        final LinearLayout layoutContainer = (LinearLayout) view.findViewById(
                R.id.layoutContainer);

        btnReset = (ToggleButton) view.findViewById(R.id.btnReset);
        btnReset.setEnabled(false);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnStart =
                (ToggleButton) view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnStart.isChecked()){
                    startTime = System.currentTimeMillis();
                    if(timerStopWatch != null){
                        timerStopWatch.cancel();
                    }
                    layoutContainer.removeAllViews();
                    timerStopWatch = new Timer();
                    timerStopWatch.schedule(new RunTimeTimerTask(),0,10);
                }else{
                    timerStopWatch.cancel();
                }

            }
        });
        return view;
    }
    private class RunTimeTimerTask extends TimerTask {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    long currentTime = System.currentTimeMillis();
                    long diff = currentTime - startTime;
                    long min = diff / 60000;
                    long sec = diff / 1000;
                    long msec = (diff % 1000) / 10;


                    //fix the time so that when it hits 60 the sec resets to 00
                    tvRunTime.setText(String.format("%1$02d:%2$02d:%3$02d",
                            min, sec, msec));
                }
            });
        }
    }
}
