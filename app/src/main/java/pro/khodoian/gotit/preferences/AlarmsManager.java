package pro.khodoian.gotit.preferences;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import pro.khodoian.gotit.broadcastreceivers.RegularNotificationReceiver;
import pro.khodoian.gotit.view.SettingsActivity;

/**
 * Helper class that simplifies work with alarms
 */
public class AlarmsManager {

    private Context context;

    public AlarmsManager (Context context) {
        this.context = context;
    }

    /**
     * Sets alarms based on default alarmsSettingsManager
     */
    public void setAlarms() {
        AlarmsSettingsManager alarmsSettingsManager = new AlarmsSettingsManager(context);
        if (alarmsSettingsManager.isNotificationEnabled()) {
            setAlarm(alarmsSettingsManager.getMorningTime(),
                    RegularNotificationReceiver.ACTION_MORNING_ALARM);
            setAlarm(alarmsSettingsManager.getAfternoonTime(),
                    RegularNotificationReceiver.ACTION_AFTERNOON_ALARM);
            setAlarm(alarmsSettingsManager.getEveningTime(),
                    RegularNotificationReceiver.ACTION_EVENING_ALARM);
        } else {
            cancelAlarms();
        }
    }

    public void setAlarmsIfNotSet() {
        AlarmsSettingsManager alarmsSettingsManager = new AlarmsSettingsManager(context);
        if (alarmsSettingsManager.isNotificationEnabled()) {
            if (!isSet(SettingsActivity.DayTime.MORNING))
                setAlarm(alarmsSettingsManager.getMorningTime(),
                        RegularNotificationReceiver.ACTION_MORNING_ALARM);
            if (!isSet(SettingsActivity.DayTime.AFTERNOON))
                setAlarm(alarmsSettingsManager.getAfternoonTime(),
                        RegularNotificationReceiver.ACTION_AFTERNOON_ALARM);
            if (!isSet(SettingsActivity.DayTime.EVENING))
                setAlarm(alarmsSettingsManager.getEveningTime(),
                        RegularNotificationReceiver.ACTION_EVENING_ALARM);
        } else {
            cancelAlarms();
        }
    }

    public void setAlarm(long time, SettingsActivity.DayTime dayTime) {
        String action;
        switch (dayTime) {
            case MORNING:
                action = RegularNotificationReceiver.ACTION_MORNING_ALARM;
                break;
            case AFTERNOON:
                action = RegularNotificationReceiver.ACTION_AFTERNOON_ALARM;
                break;
            case EVENING:
                action = RegularNotificationReceiver.ACTION_EVENING_ALARM;
                break;
            default:
                return;
        }
        setAlarm(time, action);
    }

    private void setAlarm(long time, String action) {
        PendingIntent pendingIntent = getAlarmPendingIntent(action);
        Calendar newTime = Calendar.getInstance();
        newTime.setTimeInMillis(time);
        int hourOfDay = newTime.get(Calendar.HOUR_OF_DAY);
        int minute = newTime.get(Calendar.MINUTE);

        // check if time elapsed today and adjust date if required
        Calendar c = Calendar.getInstance();
        int increment = 0;
        if (c.get(Calendar.HOUR_OF_DAY) > hourOfDay
                || (c.get(Calendar.HOUR_OF_DAY) == hourOfDay) && c.get(Calendar.MINUTE) >= minute) {
            increment = 1;
        }
        c.set(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH) + increment,
                hourOfDay,
                minute);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
        long timeInMillis = c.getTimeInMillis();
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * Cancels all alarms that might have been set previously
     */
    public void cancelAlarms() {
        cancelAlarm(RegularNotificationReceiver.ACTION_MORNING_ALARM);
        cancelAlarm(RegularNotificationReceiver.ACTION_AFTERNOON_ALARM);
        cancelAlarm(RegularNotificationReceiver.ACTION_EVENING_ALARM);
    }

    public void cancelAlarm(String action) {
        PendingIntent pendingIntent = getAlarmPendingIntent(action);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
    }

    public PendingIntent getAlarmPendingIntent(String action) {
        Intent intent = RegularNotificationReceiver.makeIntent(context);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private boolean isSet(SettingsActivity.DayTime dayTime) {
        String action;
        switch (dayTime) {
            case MORNING:
                action = RegularNotificationReceiver.ACTION_MORNING_ALARM;
                break;
            case AFTERNOON:
                action = RegularNotificationReceiver.ACTION_AFTERNOON_ALARM;
                break;
            case EVENING:
                action = RegularNotificationReceiver.ACTION_EVENING_ALARM;
                break;
            default:
                return false;
        }
        return isSet(action);
    }

    private boolean isSet(String action) {
        Intent intent = RegularNotificationReceiver.makeIntent(context);
        intent.setAction(action);
        return (PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_NO_CREATE) != null);
    }
}
