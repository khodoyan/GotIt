package pro.khodoian.gotit.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Date;

import pro.khodoian.gotit.view.SettingsActivity;

/**
 * Class provides access to alarms settings
 */
public class AlarmsSettingsManager {
    public static final String TAG = AlarmsSettingsManager.class.getCanonicalName();

    public static final String PREFERENCES = "AlarmPreferences";

    public static final String KEY_NOTIFICATIONS_ENABLED = "NotificationsEnabled";
    public static final String KEY_MORNING = "Morning";
    public static final String KEY_AFTERNOON = "Afternoon";
    public static final String KEY_EVENING = "Evening";

    private static final long DEFAULT_MORNING = 28800000; // 8 am in millis
    private static final long DEFAULT_AFTERNOON = 46800000; // 1 pm in millis
    private static final long DEFAULT_EVENING = 72000000; // 8 pm in millis

    SharedPreferences preferences;

    boolean notificationEnabled = true;
    long morningTime = DEFAULT_MORNING;
    long afternoonTime = DEFAULT_AFTERNOON;
    long eveningTime = DEFAULT_EVENING;

    public AlarmsSettingsManager (Context context) {
        preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        updateValues();
    }

    private void updateValues() {
        if (preferences != null) {
            morningTime = preferences.getLong(KEY_MORNING, DEFAULT_MORNING);
            afternoonTime = preferences.getLong(KEY_AFTERNOON, DEFAULT_AFTERNOON);
            eveningTime = preferences.getLong(KEY_EVENING, DEFAULT_EVENING);
        }
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_NOTIFICATIONS_ENABLED, notificationEnabled);
            editor.commit();
        }
    }

    public void setTime(SettingsActivity.DayTime dayTime, long timeInMillis) {
        switch (dayTime) {
            case MORNING:
                setMorningTime(timeInMillis);
                break;
            case AFTERNOON:
                setAfternoonTime(timeInMillis);
                break;
            case EVENING:
                setEveningTime(timeInMillis);
                break;
            default:
        }
    }

    private void setMorningTime(long morningTime) {
        this.morningTime = morningTime;
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(KEY_MORNING, morningTime);
            editor.commit();
        }
    }

    private void setAfternoonTime(long afternoonTime) {
        this.afternoonTime = afternoonTime;
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(KEY_AFTERNOON, afternoonTime);
            editor.commit();
        }
    }

    private void setEveningTime(long eveningTime) {
        this.eveningTime = eveningTime;
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(KEY_EVENING, eveningTime);
            editor.commit();
        }
    }

    private void setAlarmsTime(long morningTime, long afternoonTime, long eveningTime) {
        this.morningTime = morningTime;
        this.afternoonTime = afternoonTime;
        this.eveningTime = eveningTime;
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(KEY_MORNING, morningTime);
            editor.putLong(KEY_AFTERNOON, afternoonTime);
            editor.putLong(KEY_EVENING, eveningTime);
            editor.commit();
        }
    }

    public long getMorningTime() {
        return morningTime;
    }

    public long getAfternoonTime() {
        return afternoonTime;
    }

    public long getEveningTime() {
        return eveningTime;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }
}
