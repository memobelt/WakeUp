package aitfinalproject.wakeup.Fragments;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.concurrent.TimeUnit;

import aitfinalproject.wakeup.R;


public class TimerFragment extends Fragment{
    public static String TAG = "TimerFragment";
    private LinearLayout linearSpinner;
    private LinearLayout linearTimer;
    private Button btnTimeStart;
    private Button btnTimeReset;
    private ToggleButton btnPause;
    private Spinner spinnerHour;
    private Spinner spinnerMin;
    private TextView tvTimer;
    private long hour = 0;
    private long minute = 0;
    private long hourMs;
    private long minuteMs;
    private CountDownTimer timer;

    public TimerFragment(){
    }

    public static TimerFragment newInstance() {
        TimerFragment fragment = new TimerFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_timer, container, false);


        linearSpinner = (LinearLayout) view.findViewById(R.id.linearSpinner);
        linearTimer = (LinearLayout) view.findViewById(R.id.linearTimer);
        tvTimer = (TextView) view.findViewById(R.id.tvTimer);

        //Combo Box for which type of item
        spinnerHour = (Spinner) view.findViewById(R.id.spinnerHour);
        ArrayAdapter<CharSequence> adapterHour = ArrayAdapter.createFromResource(getContext(),
                R.array.item_array_hour, R.layout.text_view);
        adapterHour.setDropDownViewResource(R.layout.text_view);
        spinnerHour.setAdapter(adapterHour);

        //Combo Box for which type of item
        spinnerMin = (Spinner) view.findViewById(R.id.spinnerMin);
        ArrayAdapter<CharSequence> adapterMin = ArrayAdapter.createFromResource(getContext(),
                R.array.item_array_minute, R.layout.text_view);
        adapterMin.setDropDownViewResource(R.layout.text_view);
        spinnerMin.setAdapter(adapterMin);


        btnTimeStart = (Button) view.findViewById(R.id.btnTimeStart);
        btnTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearSpinner.setVisibility(View.GONE);
                linearTimer.setVisibility(View.VISIBLE);
                btnPause.setEnabled(true);
                btnTimeStart.setEnabled(false);

                hour = Integer.parseInt((String) spinnerHour.getSelectedItem());
                minute = Integer.parseInt((String) spinnerMin.getSelectedItem());
                hourMs = hour * 3600000;
                minuteMs = minute * 60000;
                long timeMs = hourMs + minuteMs;

                timer = new CountDownTimer(timeMs, 1000);
                timer.start();


            }
        });


        btnTimeReset = (Button) view.findViewById(R.id.btnTimeReset);
        btnTimeReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //stops timer
                timer.cancel();
                timer.onFinish();

                //resets spinners
                spinnerHour.setSelection(0);
                spinnerMin.setSelection(0);

                //btnTimeReset.setEnabled(false);
                btnPause.setEnabled(false);
                btnPause.setChecked(false);
                btnTimeStart.setEnabled(true);


                linearTimer.setVisibility(View.GONE);
                linearSpinner.setVisibility(View.VISIBLE);

                if(timer.isAlarmPlaying()){
                    timer.stopAlarm();
                }

            }
        });

        btnPause = (ToggleButton) view.findViewById(R.id.btnPause);
        btnPause.setEnabled(false);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(btnPause.isChecked()){
                   btnTimeReset.setEnabled(true);
                   btnTimeStart.setEnabled(false);
                   timer.cancel();
               }else if(!btnPause.isChecked()){
                   long newTime = timer.getTime();
                   timer = new CountDownTimer(newTime, 1000);
                   timer.start();
               }
            }
        });

        return view;
    }


    public class CountDownTimer extends android.os.CountDownTimer {
        public long millis;
        public Ringtone r;

        //constructor for CountDownTimer
        public CountDownTimer (long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);


        }

        public long getTime(){
            return millis;
        }

        public boolean isAlarmPlaying(){
            if (r == null){
                return false;
            }
            return r.isPlaying();
        }

        public void stopAlarm(){
            r.stop();
        }


        @Override
        public void onTick(long millisUntilFinished) {
            millis = millisUntilFinished;
            String countDown = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                 TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            tvTimer.setText(countDown);

        }

        @Override
        public void onFinish() {

            if (tvTimer.getText().toString().equals("00:00:01")) {
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    r = RingtoneManager.getRingtone(getContext(), notification);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "Timer Done.", Toast.LENGTH_LONG).show();
            }
            tvTimer.setText("00:00:00");
        }

    }


}
