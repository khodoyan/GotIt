package pro.khodoian.gotit.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import pro.khodoian.gotit.preferences.AlarmsSettingsManager;

/**
 * This class was taken from android developers website
 * @author Google
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private SettingsActivity.DayTime dayTime;
    long initialTime;

    public interface Listener {
        void onTimeChosen(SettingsActivity.DayTime dayTime, long timeInMillis);
    }

    public void setDayTime(SettingsActivity.DayTime dayTime) {
        this.dayTime = dayTime;
    }

    public void setInitialTime(AlarmsSettingsManager alarmsSettingsManager) {
        if (dayTime != null) {
            switch (dayTime) {
                case MORNING:
                    initialTime = alarmsSettingsManager.getMorningTime();
                    break;
                case AFTERNOON:
                    initialTime = alarmsSettingsManager.getAfternoonTime();
                    break;
                case EVENING:
                    initialTime = alarmsSettingsManager.getEveningTime();
                    break;
                default:
                    initialTime = alarmsSettingsManager.getMorningTime();
            }
        }

    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the initialTime as the default values for the picker
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(initialTime);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (getActivity() instanceof Listener) {
            Calendar c = Calendar.getInstance();
            int increment = 0;
            // check if time elapsed today and adjust date if required
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
            long timeInMillis = c.getTimeInMillis();
            ((Listener)getActivity()).onTimeChosen(dayTime, timeInMillis);
        }
    }
}