package aitfinalproject.wakeup.AlarmClock;

import android.widget.TextClock;

/**
 * Created by Memo on 5/20/16.
 */
public class Alarm {
    private boolean isEnabled;
    private TextClock mTextClock;

    public Alarm(Boolean iE, TextClock tc) {
        isEnabled = iE;
        mTextClock = tc;

    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public TextClock getTextClock() {
        return mTextClock;
    }

    public void setmTextClock(TextClock textClock) {
        mTextClock = textClock;
    }
}
