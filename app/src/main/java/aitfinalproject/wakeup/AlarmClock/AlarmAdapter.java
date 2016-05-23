package aitfinalproject.wakeup.AlarmClock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import aitfinalproject.wakeup.Constants;
import aitfinalproject.wakeup.R;

/**
 * Created by Memo on 5/19/16.
 */
    public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private Context mContext;
    private List<Alarm> mAlarms = new Vector();
    public AlarmAdapter(Context context){
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(mContext)
                .inflate(R.layout.alarm_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Alarm alarm = mAlarms.get(position);
        final Switch sAlarm = holder.sAlarm;
        sAlarm.setChecked(alarm.isEnabled());
        sAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm.setEnabled(sAlarm.isEnabled());
            }
        });

        final TextView tvAlarm = holder.tvAlarmTime;
        tvAlarm.setText(alarm.getTime());

        int[] days = alarm.getDays();
        if(days!=null) {
            for (int i = 0; i < 7; i++) {
                if (days[i] == Constants.ON) {
                    holder.toggleButtons[i].setChecked(true);
                } else {
                    holder.toggleButtons[i].setChecked(false);
                }
            }
        }
        holder.itemView.setTag(alarm);

    }
    public void addAlarm(Alarm alarm){
        if(alarm == null){
            alarm = new Alarm(true, "f", Alarm.convertArrayToString(new int[]{1}));
        }
        long i = alarm.save();
        mAlarms.add(mAlarms.size(),alarm);
        notifyDataSetChanged();
    }

    public void loadAlarms(){
        List<Alarm> alarms = Alarm.listAll(Alarm.class);
        for (Alarm alarm : alarms) {
            mAlarms.add(mAlarms.size(), alarm);
        }
        notifyDataSetChanged();
    }

    public void onItemDismiss(int position) {
        mAlarms.remove(position).delete();
        notifyItemRemoved(position);
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mAlarms, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mAlarms, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAlarmTime;
        private Switch sAlarm;
        private ToggleButton[] toggleButtons = new ToggleButton[7];

        public ViewHolder(View itemView) {
            super(itemView);
            tvAlarmTime = (TextView) itemView.findViewById(R.id.tvAlarmTime);
            sAlarm = (Switch) itemView.findViewById(R.id.sAlarm);
            toggleButtons[Constants.MONDAY] = (ToggleButton) itemView.findViewById(R.id.rowMonday);
            toggleButtons[Constants.TUESDAY] = (ToggleButton) itemView.findViewById(R.id.rowTuesday);
            toggleButtons[Constants.WEDNESDAY] = (ToggleButton) itemView.findViewById(R.id.rowWednesday);
            toggleButtons[Constants.THURSDAY] = (ToggleButton) itemView.findViewById(R.id.rowThursday);
            toggleButtons[Constants.FRIDAY] = (ToggleButton) itemView.findViewById(R.id.rowFriday);
            toggleButtons[Constants.SATURDAY] = (ToggleButton) itemView.findViewById(R.id.rowSaturday);
            toggleButtons[Constants.SUNDAY] = (ToggleButton) itemView.findViewById(R.id.rowSunday);
        }
    }
}
