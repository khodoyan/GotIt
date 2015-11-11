package pro.khodoian.gotit.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.client.AuthenticationDetailsManager;
import pro.khodoian.gotit.client.UserService;
import pro.khodoian.gotit.client.UsersProxy;

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
        // start login check if credentials are available
        if (authManager.getToken() == null || authManager.getToken().equals("")) {
            startLoginActivity();
        } else {
            new UserService(authManager).checkLogin(new UserService.CheckLoginListener() {
                @Override
                public void onCheckLoginSuccess() {
                    startMainActivityAndFinish();
                }

                @Override
                public void onCheckLoginUnauthorized() {
                    startLoginActivity();
                }

                @Override
                public void onCheckLoginFailure() {
                    startLoginActivity();
                }
            });
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
}
