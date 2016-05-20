package aitfinalproject.wakeup.AlarmClock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextClock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aitfinalproject.wakeup.R;

/**
 * Created by Memo on 5/19/16.
 */
    public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private Context mContext;
    private List<Alarm> mAlarms = new ArrayList<>();
    public AlarmAdapter(Context context){
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Alarm alarm = mAlarms.get(position);
        final Switch sAlarm = holder.sAlarm;
        sAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarm.setEnabled(sAlarm.isEnabled());
            }
        });

        holder.itemView.setTag(alarm);

    }
    public void addAlarm(){
        mAlarms.add(mAlarms.size(),new Alarm(true, new TextClock(mContext)));
        notifyDataSetChanged();
    }

    public void onItemDismiss(int position) {
        mAlarms.remove(position);
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

        private TextClock tcAlarmTime;
        private Switch sAlarm;

        public ViewHolder(View itemView) {
            super(itemView);
            tcAlarmTime = (TextClock) itemView.findViewById(R.id.tcAlarmTime);
            sAlarm = (Switch) itemView.findViewById(R.id.sAlarm);
        }
    }
}
