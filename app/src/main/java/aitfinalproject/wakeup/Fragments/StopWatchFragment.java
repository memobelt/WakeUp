package aitfinalproject.wakeup.Fragments;

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
    private boolean isRunning = false;
    private boolean isLap = false;
    ToggleButton btnStart;
    Button btnReset;
    private boolean isPaused = false;
    private RunTimeTimerTask timerTask;

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

        btnReset = (Button) view.findViewById(R.id.btnReset);
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
                btnReset.setEnabled(true);
                if(btnStart.isChecked()){
                    timerTask = new RunTimeTimerTask();
                    startTime = System.currentTimeMillis();
                    if(timerStopWatch != null){
                        timerStopWatch.cancel();
                    }
                    layoutContainer.removeAllViews();
                    timerStopWatch = new Timer();
                    timerStopWatch.schedule(timerTask,0,10);
                }else if (!btnStart.isChecked()) {
                    btnReset.setEnabled(false);
                    startTime = System.currentTimeMillis();

                    if(timerStopWatch != null){
                        timerStopWatch.cancel();
                    }
                    layoutContainer.removeAllViews();
                    timerStopWatch = new Timer();
                    timerTask.run();
                    timerStopWatch.schedule(timerTask,0,10);
                }
            }
        });
        return view;
    }
//    private class RunTimeTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    long currentTime = System.currentTimeMillis();
//                    long diff = currentTime - startTime;
//                    long min = diff / 60000;
//                    long sec = diff / 1000;
//                    long msec = (diff % 1000) / 10;
//
//
//                    //fix the time so that when it hits 60 the sec resets to 00
//                    String time = String.format("%1$02d:%2$02d:%3$02d",
//                            min, sec, msec);
//                    tvRunTime.setText(time);
//                }
//            });
//        }
//    }

    private class RunTimeTimerTask extends TimerTask {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(isPaused) {
                        long currentTime = System.currentTimeMillis();
                        long diff = currentTime - startTime;
                        long min = diff / 60000;
                        long sec = diff / 1000;
                        long msec = (diff % 1000) / 10;


                        //fix the time so that when it hits 60 the sec resets to 00
                        String time = String.format("%1$02d:%2$02d:%3$02d",
                                min, sec, msec);
                        tvRunTime.setText(time);
                    }else if(!isPaused){

                    }
                }
            });
        }
    }




}
