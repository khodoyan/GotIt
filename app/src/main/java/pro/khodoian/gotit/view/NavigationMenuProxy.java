package pro.khodoian.gotit.view;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.client.AuthenticationDetailsManager;

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

    public void addPosts() {

    }

    public void startCheckinActivity() {
        Intent intent;
        if (activity.get() != null && !(activity.get() instanceof CheckinActivity)) {
            intent = CheckinActivity.makeIntent(activity.get());
            if (activity.get() instanceof MainActivity)
                activity.get().startActivityForResult(intent, CheckinActivity.REQUEST_CODE);
            else
                activity.get().startActivity(intent);
        }
    }

    public void startPeopleActivity() {
        if (activity.get() != null  && !(activity.get() instanceof PeopleActivity))
            activity.get().startActivity(PeopleActivity.makeIntent(activity.get()));
    }

    public void startSettingsActivity() {
        if (activity.get() != null && !(activity.get() instanceof SettingsActivity))
            activity.get().startActivity(SettingsActivity.makeIntent(activity.get()));
    }
}
