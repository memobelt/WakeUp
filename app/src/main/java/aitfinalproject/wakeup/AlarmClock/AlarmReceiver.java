package aitfinalproject.wakeup.AlarmClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import aitfinalproject.wakeup.Constants;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        Uri uri = intent.getExtras().getParcelable("SOUND");
        Intent si = new Intent(context, AlarmService.class);
        int id = intent.getIntExtra("STATE", Constants.OFF);
        si.putExtra("SOUND", uri);
        si.putExtra("STATE", id);
        si.putExtra("EQ", intent.getStringExtra("EQUATION"));
        si.putExtra("ANS", intent.getStringExtra("ANS"));
        si.addCategory(AlarmService.SERVICE);
        context.startService(si);
    }
}
