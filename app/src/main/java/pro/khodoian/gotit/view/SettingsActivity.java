package pro.khodoian.gotit.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.preferences.AlarmsManager;
import pro.khodoian.gotit.preferences.AlarmsSettingsManager;

public class SettingsActivity extends AppCompatActivity
        implements TimePickerFragment.Listener{

    public enum DayTime {MORNING, AFTERNOON, EVENING}

    private long morningTime;
    private long afternoonTime;
    private long eveningTime;

    private Button morningButton;
    private Button afternoonButton;
    private Button eveningButton;
    private Switch enableNotificationsSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // get UI elements
        morningButton = (Button) findViewById(R.id.settings_morning_alarm_button);
        afternoonButton = (Button) findViewById(R.id.settings_afternoon_alarm_button);
        eveningButton = (Button) findViewById(R.id.settings_evening_alarm_button);
        enableNotificationsSwitch = (Switch) findViewById(R.id.notifications_enabled_switch);

        // update values of the UI elements
        updateUI(new AlarmsSettingsManager(this));

        // set listeners to call TimePicker
        morningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTimePicker(DayTime.MORNING);
            }
        });

        afternoonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTimePicker(DayTime.AFTERNOON);
            }
        });

        eveningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTimePicker(DayTime.EVENING);
            }
        });

        enableNotificationsSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean enabled) {
                new AlarmsSettingsManager(SettingsActivity.this).setNotificationEnabled(enabled);
                if (enabled) {
                    new AlarmsManager(SettingsActivity.this).setAlarms();
                } else {
                    new AlarmsManager(SettingsActivity.this).cancelAlarms();
                }
            }
        });

    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    private void callTimePicker(DayTime dayTime) {
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setDayTime(dayTime);
        timePickerFragment.setInitialTime(new AlarmsSettingsManager(this));
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void setTimeToButton(DayTime dayTime, long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String timeString =
                formatIntToTwoDigits(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + formatIntToTwoDigits(calendar.get(Calendar.MINUTE));
        Button button;
        switch (dayTime) {
            case MORNING:
                button = morningButton;
                break;
            case AFTERNOON:
                button = afternoonButton;
                break;
            case EVENING:
                button = eveningButton;
                break;
            default:
                button = morningButton;
        }
        button.setText(timeString);
    }

    private String formatIntToTwoDigits(int digit) {
        if (digit < 0)
            return "00";
        return (digit > 9)
                ? String.valueOf(digit)
                : "0" + String.valueOf(digit);
    }

    @Override
    public void onTimeChosen(DayTime dayTime, long timeInMillis) {
        setTimeToButton(dayTime, timeInMillis);
        new AlarmsSettingsManager(this).setTime(dayTime, timeInMillis);
        new AlarmsManager(this).setAlarm(timeInMillis, dayTime);
    }

    private void updateUI(AlarmsSettingsManager alarmsSettingsManager) {
        if (alarmsSettingsManager != null) {
            enableNotificationsSwitch.setChecked(alarmsSettingsManager.isNotificationEnabled());
            setTimeToButton(DayTime.MORNING, alarmsSettingsManager.getMorningTime());
            setTimeToButton(DayTime.AFTERNOON, alarmsSettingsManager.getAfternoonTime());
            setTimeToButton(DayTime.EVENING, alarmsSettingsManager.getEveningTime());
        }
    }
}
