package aitfinalproject.wakeup;

import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import aitfinalproject.wakeup.AlarmClock.AddAlarmDialog;
import aitfinalproject.wakeup.AlarmClock.Alarm;
import aitfinalproject.wakeup.Fragments.AlarmClockFragment;
import aitfinalproject.wakeup.Fragments.StopWatchFragment;
import aitfinalproject.wakeup.Fragments.TimerFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
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
    public void onAddAlarmPositiveClick(View v) {
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
        if(!DateFormat.is24HourFormat(this)){
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = null;
            try {
                _24HourDt = _24HourSDF.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            time = _12HourSDF.format(_24HourDt);
        }
        Alarm a = new Alarm(true, time, Alarm.convertArrayToString(dates));
        acf.addAlarm(a);
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
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
