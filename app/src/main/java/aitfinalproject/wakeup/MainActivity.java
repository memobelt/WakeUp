package aitfinalproject.wakeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.astuetz.PagerSlidingTabStrip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import aitfinalproject.wakeup.AlarmClock.AddAlarmDialog;
import aitfinalproject.wakeup.AlarmClock.Alarm;
import aitfinalproject.wakeup.AlarmClock.AlarmReceiver;
import aitfinalproject.wakeup.Fragments.AlarmClockFragment;
import aitfinalproject.wakeup.Fragments.StopWatchFragment;
import aitfinalproject.wakeup.Fragments.TimerFragment;
import bsh.EvalError;

public class MainActivity extends AppCompatActivity implements AddAlarmDialog.NoticeDialogListener,
        AlarmClockFragment.OnAlarmFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private FloatingActionButton fabAddAlarm;
    private Intent intent;
    private PendingIntent pi;
    private AlarmManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(this, AlarmReceiver.class);
        pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(getIntent().getBooleanExtra("FROM ALARM",false)==true){
            setContentView(R.layout.activity_stop_alarm);
            final TextView equation = (TextView) findViewById(R.id.equation);
            equation.setText(getIntent().getStringExtra("EQ"));
            final Button btnAnsEq = (Button) findViewById(R.id.btnEqAns);
            final EditText ans = (EditText) findViewById(R.id.answer);
            final Button setOffAlarm = (Button) findViewById(R.id.dummy_button);
            assert btnAnsEq != null;
            btnAnsEq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ans.getText().toString().equals(getIntent().getStringExtra("ANS"))){
                        equation.setVisibility(View.GONE);
                        ans.setVisibility(View.GONE);
                        btnAnsEq.setVisibility(View.GONE);
                        setOffAlarm.setVisibility(View.VISIBLE);
                    }
                    else {
                        ans.setError(getString(R.string.wrong));
                    }
                }
            });
            assert setOffAlarm != null;
            setOffAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("STATE", Constants.OFF);
                    sendBroadcast(intent);
                    am.cancel(pi);

                    noAlarm();
                }
            });
        }
        else{
            noAlarm();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void noAlarm(){
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    fabAddAlarm.show();
                }
                else {
                    fabAddAlarm.hide();
                }
            }



            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fabAddAlarm = (FloatingActionButton) findViewById(R.id.fabAddAlarm);
        fabAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment addAlarmDialog = new AddAlarmDialog();
                addAlarmDialog.show(getSupportFragmentManager(), "addAlarm");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddAlarmPositiveClick(View v, Parcelable uri, int diff) {
        AlarmClockFragment acf = (AlarmClockFragment)
                getSupportFragmentManager().findFragmentByTag(AlarmClockFragment.TAG);
        ToggleButton[] toggleButtons = new ToggleButton[7];
        toggleButtons[Constants.MONDAY] = (ToggleButton) v.findViewById(R.id.monday);
        toggleButtons[Constants.TUESDAY] = (ToggleButton) v.findViewById(R.id.tuesday);
        toggleButtons[Constants.WEDNESDAY] = (ToggleButton) v.findViewById(R.id.wednesday);
        toggleButtons[Constants.THURSDAY] = (ToggleButton) v.findViewById(R.id.thursday);
        toggleButtons[Constants.FRIDAY] = (ToggleButton) v.findViewById(R.id.friday);
        toggleButtons[Constants.SATURDAY] = (ToggleButton) v.findViewById(R.id.saturday);
        toggleButtons[Constants.SUNDAY] = (ToggleButton) v.findViewById(R.id.sunday);

        int dates[] = new int[7];
        for (int i = 0; i < 7; i++) {
            if(toggleButtons[i].isChecked()){
                dates[i] = Constants.ON;
            }
            else {
                dates[i] = Constants.OFF;
            }
        }

        TimePicker tpAlarm = (TimePicker) v.findViewById(R.id.tpAlarm);
        String time = String.valueOf(tpAlarm.getCurrentHour())+":"+String.valueOf(tpAlarm.getCurrentMinute());
        String minute_string = String.valueOf(tpAlarm.getCurrentMinute());
        String hour_string = String.valueOf(tpAlarm.getCurrentHour());

        if (tpAlarm.getCurrentMinute() < 10) {
            minute_string = "0" + String.valueOf(tpAlarm.getCurrentMinute());
        }

        if (tpAlarm.getCurrentHour() > 12) {
            hour_string = String.valueOf(tpAlarm.getCurrentHour() - 12) ;
        }
        Date _24HourDt = null;
        if(!DateFormat.is24HourFormat(this)){
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            try {
                _24HourDt = _24HourSDF.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            time = _12HourSDF.format(_24HourDt);
        }
        Alarm a = new Alarm(true, time, Alarm.convertArrayToString(dates));
        acf.addAlarm(a);
        String[] e = new String[0];
        try {
            e = acf.generateEquation(diff);
        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }
        if(e[0].length()>2)
            e[0] = e[0].substring(9)+"=";

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, _24HourDt.getMinutes());
        calendar.set(Calendar.HOUR_OF_DAY, _24HourDt.getHours());
        if(uri==null){
            uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }
        intent.putExtra("SOUND", uri);
        intent.putExtra("STATE", Constants.ON);
        intent.putExtra("EQUATION", e[0]);
        intent.putExtra("ANS",e[1]);
        pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        Toast.makeText(this, e[1], Toast.LENGTH_SHORT).show();

        Toast.makeText(MainActivity.this, "Alarm set at " +hour_string+":"+minute_string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAlarmFragmentInteraction(Uri uri) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter{

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Bundle args = new Bundle();
            if (position == 0) {
//                showFragment(AlarmClockFragment.TAG);
                return AlarmClockFragment.newInstance(R.id.container);
            }
            if (position == 1) {
//                showFragment(StopWatchFragment.TAG);
                return StopWatchFragment.newInstance();
            }
            if (position == 2) {
                return new TimerFragment().newInstance();
//                return AlarmClockFragment.newInstance();
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.alarm);
                case 1:
                    return getString(R.string.stopwatch);
                case 2:
                    return getString(R.string.timer);
            }
            return null;
        }
    }
}
