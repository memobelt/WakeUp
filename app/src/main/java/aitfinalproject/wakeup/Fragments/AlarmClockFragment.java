package aitfinalproject.wakeup.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aitfinalproject.wakeup.AlarmClock.Alarm;
import aitfinalproject.wakeup.AlarmClock.AlarmAdapter;
import aitfinalproject.wakeup.AlarmClock.AlarmTouchHelper;
import aitfinalproject.wakeup.R;
import bsh.EvalError;
import bsh.Interpreter;

/*
* Memo
* */

public class AlarmClockFragment extends Fragment{
    public static String TAG = "AlarmClockFragment";
    public static String LAYOUT_TAG = "Container";
    Context mContext;
    FloatingActionButton fabAddAlarm;
    AlarmAdapter mAlarmAdapter;
    public AlarmClockFragment() {
    }
    public static AlarmClockFragment newInstance(int layout) {
        Bundle args = new Bundle();
        args.putInt(LAYOUT_TAG,layout);
        AlarmClockFragment fragment = new AlarmClockFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private OnAlarmFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlarmAdapter = new AlarmAdapter(getActivity());
        mContext = getContext();
        getFragmentManager().beginTransaction().
                replace(getArguments().getInt(LAYOUT_TAG), this, AlarmClockFragment.TAG);
        mAlarmAdapter.loadAlarms();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_clock, container, false);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView recycler_View = (RecyclerView) view.findViewById(R.id.recycler_view1);
        recycler_View.setAdapter(mAlarmAdapter);
        recycler_View.setLayoutManager(llm);

        ItemTouchHelper.Callback callback = new AlarmTouchHelper(mAlarmAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recycler_View);

        return view;
    }

    public void addAlarm(Alarm alarm){
        mAlarmAdapter.addAlarm(alarm);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAlarmFragmentInteractionListener) {
            mListener = (OnAlarmFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAlarmFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String[] generateEquation(int diff) throws EvalError {
        if(diff>0) {
            int nums[] = new int[diff + 1];
            for (int i = 0; i < diff + 1; i++) {
                nums[i] = (int) ((Math.random() * (10 * diff)) + (Math.random() * 10));
            }
            StringBuilder equation = new StringBuilder("result = ");
            for (int i = 0; i < diff; i++) {
                int operator = (int) (Math.random() * 4);
                switch (operator) {
                    case 0:
                        equation.append(nums[i]).append("+");
                        break;
                    case 1:
                        equation.append(nums[i]).append("*");
                        break;
                    case 2:
                        equation.append(nums[i]).append("/");
                        break;
                    case 3:
                        equation.append(nums[i]).append("-");
                        break;
                }
            }
            equation.append(nums[diff]);
            Interpreter interpreter = new Interpreter();
            interpreter.eval(equation.toString());
            String s = String.valueOf(interpreter.get("result"));
            return new String[]{equation.toString(), s};
        }
        else {
            return new String[]{"0","0"};
        }
    }

    public interface OnAlarmFragmentInteractionListener {
        void onAlarmFragmentInteraction(Uri uri);
    }
}
