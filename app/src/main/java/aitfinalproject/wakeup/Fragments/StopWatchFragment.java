package aitfinalproject.wakeup.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Timer;
import java.util.TimerTask;

import aitfinalproject.wakeup.R;

public class StopWatchFragment extends Activity{

    private TextView tvRunTime;
    private Timer timerStopWatch;
    private long startTime = 0;
    private boolean isRunning = false;
    private boolean isLap = false;
    final ToggleButton btnStart;
    final ToggleButton btnReset;

    private class RunTimeTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
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



    public StopWatchFragment(View view) {

        tvRunTime = (TextView) view.findViewById(R.id.tvStopWatch);


        final LinearLayout container = (LinearLayout) view.findViewById(
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
                    container.removeAllViews();
                   timerStopWatch = new Timer();
                    timerStopWatch.schedule(new RunTimeTimerTask(),0,10);
                }else{
                    timerStopWatch.cancel();
                }

            }
        });


    }




}
