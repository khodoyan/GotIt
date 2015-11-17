package pro.khodoian.gotit.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.broadcastreceivers.RegularNotificationReceiver;
import pro.khodoian.gotit.preferences.AlarmsManager;
import pro.khodoian.gotit.preferences.AlarmsSettingsManager;
import pro.khodoian.gotit.preferences.AuthenticationDetailsManager;
import pro.khodoian.gotit.client.UserService;
import pro.khodoian.gotit.models.UserClient;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {

    public static final String TAG = SplashActivity.class.getCanonicalName();

    public static final int REQUEST_LOGIN = 0;
    private boolean exitActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        if (exitActivity)
            finish();
        // Checks credentials and starts login screen if no credentials available
        final AuthenticationDetailsManager authManager = new AuthenticationDetailsManager(this);

        // set status to user
        final TextView status = (TextView)
                SplashActivity.this.findViewById(R.id.splash_status_text_field);
        if (status != null)
            status.setText(R.string.splash_logging_in);

        // start login check if credentials are available
        if (authManager.getToken() == null || authManager.getToken().equals("")) {
            startLoginActivity();
        } else {
            new UserService(authManager).getPrincipal(
                    new UserService.GetPrincipalListener() {
                        @Override
                        public void onSuccess(UserClient user) {
                            authManager.setUserDetails(user);
                            if (status != null)
                                status.setText(R.string.splash_loading_posts_and_people);
                            // TODO: request people and posts
                            // set alarms
                            if (authManager.isPatient()) {
                                new AlarmsManager(SplashActivity.this).setAlarmsIfNotSet();
                            } else {
                                new AlarmsManager(SplashActivity.this).cancelAlarms();
                            }
                            startMainActivityAndFinish();
                        }

                        @Override
                        public void onUnauthorized() {
                            startLoginActivity();
                        }

                        @Override
                        public void onFailure() {
                            startLoginActivity();
                        }
                    }
            );
        }
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == LoginActivity.RESULT_FAILED_LOGIN) {
                // user pressed back on login screen
                exitActivity = true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startMainActivityAndFinish() {
        startActivity(MainActivity.makeIntent(SplashActivity.this));
        finish();
    }

    private void startLoginActivity() {
        startActivityForResult(
                LoginActivity.makeIntent(SplashActivity.this),
                REQUEST_LOGIN);
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }
}
