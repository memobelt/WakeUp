package aitfinalproject.wakeup.AlarmClock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import aitfinalproject.wakeup.Constants;
import aitfinalproject.wakeup.MainActivity;
import aitfinalproject.wakeup.R;

public class AlarmService extends Service {
    public static String SERVICE = "aitfinalproject.AlarmService";
    MediaPlayer mediaPlayer;
    Boolean isRunning = false;
    int state;
    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        state = intent.getIntExtra("STATE", Constants.ON);
        final NotificationManager mNM = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(this.getApplicationContext(), MainActivity.class);
        intent1.putExtra("EQ",intent.getStringExtra("EQ"));
        intent1.putExtra("ANS", intent.getStringExtra("ANS"));
        intent1.putExtra("FROM ALARM", true);
        intent1.putExtra("adsfa","fd");
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification mNotify  = new Notification.Builder(this)
                .setContentTitle(getString(R.string.alarm_notify))
                .setContentText(getString(R.string.click_me))
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        if(!isRunning && state == Constants.ON) {
            Uri alarmSound = intent.getExtras().getParcelable("SOUND");
            mediaPlayer = MediaPlayer.create(this, alarmSound);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            mNM.notify(0, mNotify);
            isRunning = true;
            state = Constants.OFF;
        }
        else if(!isRunning && state == Constants.OFF){
            isRunning = false;
            state = Constants.OFF;
        }
        else if (isRunning && state == Constants.ON){
            isRunning = false;
            state = Constants.OFF;
        }
        else {
            mediaPlayer.stop();
            mediaPlayer.reset();
            isRunning = false;
            state = Constants.OFF;
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }
}
