package pro.khodoian.gotit.view;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.preferences.AuthenticationDetailsManager;

/**
 * Class for managing navigation menu
 *
 * @author eduardkhodoyan
 */
public class NavigationMenuProxy {
    WeakReference<Activity> activity;

    public NavigationMenuProxy(Activity activity) {
        if (activity != null){
            this.activity = new WeakReference<>(activity);
        }
    }

    public void onNavigationItemSelected(MenuItem item) {
        // Handles navigation view item clicks
        int id = item.getItemId();
        if (activity.get() != null) {
            if (id == R.id.nav_main) {
                if (!(activity.get() instanceof MainActivity)) {
                    activity.get().finish();
                }
            } else if (id == R.id.nav_check_in) {
                startCheckinActivity();
            } else if (id == R.id.nav_feedback) {

            } else if (id == R.id.nav_people) {
                startPeopleActivity();
            } else if (id == R.id.nav_preferences) {
                startSettingsActivity();
            }
        }
    }

    public void startCheckinActivity() {
        // check if user is a patient. If not, checkin activity shall not be started
        if (activity.get() != null)
            if (new AuthenticationDetailsManager(activity.get()).isPatient()) {
                // start CheckinActivity
                Intent intent;
                if (activity.get() != null && !(activity.get() instanceof CheckinActivity)) {
                    intent = CheckinActivity.makeIntent(activity.get());
                    if (activity.get() instanceof MainActivity)
                        activity.get().startActivityForResult(intent, CheckinActivity.REQUEST_CODE);
                    else
                        activity.get().startActivity(intent);
                }
            } else {
                Toast.makeText(activity.get(), R.string.check_in_have_to_be_patient,
                        Toast.LENGTH_LONG).show();
            }
    }

    public void startPeopleActivity() {
        if (activity.get() != null  && !(activity.get() instanceof PeopleActivity))
            activity.get().startActivity(PeopleActivity.makeIntent(activity.get()));
    }

    public void startSettingsActivity() {
        if (activity.get() != null  && !(activity.get() instanceof SettingsActivity))
            activity.get().startActivity(SettingsActivity.makeIntent(activity.get()));
    }
}
