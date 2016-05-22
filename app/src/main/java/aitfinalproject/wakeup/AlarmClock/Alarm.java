package aitfinalproject.wakeup.AlarmClock;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Memo on 5/20/16.
 */
public class Alarm extends SugarRecord implements Serializable {
    boolean isEnabled;
    String mTime;
    String mDays;

    public Alarm(){}

    public Alarm(Boolean iE, String hour, String days) {
        isEnabled = iE;
        mTime = hour;
        mDays = days;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public int[] getDays() {
        return convertStringToArray(mDays);
    }

    public void setDays(int[] days) {
        mDays = convertArrayToString(days);
    }

    public static String strSeparator = "__,__";
    public static String convertArrayToString(int[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }
    public static int[] convertStringToArray(String str){
        String[] arr = str.split(strSeparator);
        int[] days = new int[7];
        int x = 0;
        for (String s : arr) {
            days[x] = Integer.parseInt(s);
            x++;
        }
        return days;
    }
}
