package pro.khodoian.gotit.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.preferences.AuthenticationDetailsManager;
import pro.khodoian.gotit.client.HttpStatus;
import pro.khodoian.gotit.client.UserService;
import pro.khodoian.gotit.models.SignupUser;

public class SignUpActivity extends AppCompatActivity
        implements DatePickerFragment.Listener, UserService.SignupListener {

    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAIL = 0;

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRSTNAME= "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_MEDICAL_RECORD_NUMBER = "medical record number";
    public static final String KEY_IS_PATIENT = "is patient";
    public static final String KEY_USERPIC = "userpic";

    private UserService userService;

    private GregorianCalendar birthday;

    private ScrollView signupScrollView;
    private ProgressBar progressBar;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText firstnameEditText;
    private EditText lastnameEditText;
    private Button chooseDateButton;
    private Button signupButton;

    private EditText medicalRecordNumberEditText;
    private Switch iAmPatientSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize user interface elements
        signupScrollView = (ScrollView) findViewById(R.id.signup_form);
        progressBar = (ProgressBar) findViewById(R.id.signup_progress);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        firstnameEditText = (EditText) findViewById(R.id.firstnameEditText);
        lastnameEditText = (EditText) findViewById(R.id.lastnameEditText);
        chooseDateButton = (Button) findViewById(R.id.chooseDateButton);
        signupButton = (Button) findViewById(R.id.sign_up_button);
        medicalRecordNumberEditText = (EditText) findViewById(R.id.medicalRecordNumberEditText);
        iAmPatientSwitch = (Switch) findViewById(R.id.iAmPatientSwitch);

        // set listener for the switch
        iAmPatientSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                medicalRecordNumberEditText.setEnabled(iAmPatientSwitch.isChecked());
            }
        });

        // restore state from bundle
        restoreState(savedInstanceState);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_USERNAME, usernameEditText.getText().toString());
        outState.putString(KEY_PASSWORD, passwordEditText.getText().toString());
        outState.putString(KEY_FIRSTNAME, firstnameEditText.getText().toString());
        outState.putString(KEY_LASTNAME, lastnameEditText.getText().toString());
        outState.putString(KEY_MEDICAL_RECORD_NUMBER, medicalRecordNumberEditText.getText().toString());
        outState.putBoolean(KEY_IS_PATIENT, iAmPatientSwitch.isChecked());
        if(birthday != null)
            outState.putLong(KEY_BIRTHDAY, birthday.getTimeInMillis());
    }

    private void restoreState(Bundle inState) {
        if (inState != null) {
            usernameEditText.setText(inState.getString(KEY_USERNAME));
            passwordEditText.setText(inState.getString(KEY_PASSWORD));
            firstnameEditText.setText(inState.getString(KEY_FIRSTNAME));
            lastnameEditText.setText(inState.getString(KEY_LASTNAME));
            medicalRecordNumberEditText.setText(inState.getString(KEY_MEDICAL_RECORD_NUMBER));
            long birthdayInMillis = inState.getLong(KEY_BIRTHDAY, 0);
            if (birthdayInMillis > 0) {
                this.birthday = new GregorianCalendar();
                this.birthday.setTimeInMillis(birthdayInMillis);
                setDayToButton(
                        this.birthday.get(Calendar.YEAR),
                        this.birthday.get(Calendar.MONTH),
                        this.birthday.get(Calendar.DAY_OF_MONTH)
                );
            }
            boolean isPatient = inState.getBoolean(KEY_IS_PATIENT, false);
            iAmPatientSwitch.setChecked(isPatient);
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    @Override
    public void onDateChosen(int year, int month, int day) {
        if (birthday == null)
            birthday = new GregorianCalendar();
        birthday.set(year, month, day);
        setDayToButton(year, month, day);
    }

    private void setDayToButton(int year, int month, int day) {
        chooseDateButton.setText(
                formatIntToTwoDigits(day)
                        + "." + formatIntToTwoDigits(month)
                        + "." + String.valueOf(year)
        );
    }

    private String formatIntToTwoDigits(int digit) {
        if (digit < 0)
            return "00";
        return (digit > 9)
                ? String.valueOf(digit)
                : "0" + String.valueOf(digit);
    }

    private void toggleUIEnabled(boolean enabled) {
        usernameEditText.setEnabled(enabled);
        passwordEditText.setEnabled(enabled);
        firstnameEditText.setEnabled(enabled);
        lastnameEditText.setEnabled(enabled);
        chooseDateButton.setEnabled(enabled);
        medicalRecordNumberEditText.setEnabled(enabled);
        iAmPatientSwitch.setEnabled(enabled);
        signupButton.setEnabled(enabled);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            signupScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
            signupScrollView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    signupScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            signupScrollView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void chooseDate(View view) {
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    public void signup(View view) {
        if (userService != null)
            return;

        // get values from UI elements
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // validate inputs
        boolean cancel = false;
        View focusView = null;
        if (username == null || username.trim().equals("")) {
            focusView = usernameEditText;
            showToast(R.string.signup_toast_username_not_blank);
            cancel = true;
        }

        if (!LoginActivity.isEmailValid(username.trim()) && !cancel) {
            focusView = usernameEditText;
            showToast(R.string.error_invalid_email);
            cancel = true;
        }

        if (!LoginActivity.isPasswordValid(password) && !cancel) {
            focusView = passwordEditText;
            showToast(R.string.error_short_password);
            cancel = true;
        }

        // start signup async task if okay
        SignupUser user = makeUserFromUI();

        if (user == null && !cancel) {
            showToast(R.string.signup_toast_incorrect_input);
            focusView = usernameEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return;
        }

        // show progress bar
        showProgress(true);
        toggleUIEnabled(false);

        userService = new UserService(new AuthenticationDetailsManager(this));
        userService.signup(makeUserFromUI(), this);

    }

    private SignupUser makeUserFromUI() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String firstname = firstnameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        boolean isPatient = iAmPatientSwitch.isChecked();
        String medicalRecordNumber = (isPatient)
                ? medicalRecordNumberEditText.getText().toString()
                : "";
        SignupUser.Role role = (isPatient)
                ? SignupUser.Role.PATIENT
                : SignupUser.Role.FOLLOWER;
        long birthday = 0;
        // todo: make userpic loadable to server

        // verify values
        String userpic = "";
        if(this.birthday != null)
            birthday = this.birthday.getTimeInMillis();

        return SignupUser.makeUser(username, password, isPatient,
                firstname, lastname, birthday, medicalRecordNumber, userpic, role);
    }

    @Override
    public void onSignupSuccess(int responseCode, SignupUser user) {
        userService = null;
        switch(responseCode) {
            // returned error code
            case HttpStatus.SC_OK:
                if (user != null) {
                    SignUpActivity.this.setResult(SignUpActivity.RESULT_SUCCESS,
                            makeResultIntent(user));
                    AuthenticationDetailsManager authManager = new AuthenticationDetailsManager(this);
                    authManager.setCredentials(user.getUsername(), user.getPassword());
                    SignUpActivity.this.finish();
                }
                break;
            case HttpStatus.SC_CONFLICT:
                // user already exists
                showToast(R.string.signup_toast_username_busy);
                break;
            case HttpStatus.SC_BAD_REQUEST:
                // incorrect input
                showToast(R.string.signup_toast_incorrect_input);
                break;
            default:
                // internal server error or trying to add admin rights
                showToast(R.string.signup_toast_cant_signup);
        }
        toggleUIEnabled(true);
        showProgress(false);
    }

    @Override
    public void onSignupFailure() {
        userService = null;
        showToast(R.string.signup_toast_no_access);
        toggleUIEnabled(true);
        showProgress(false);
    }



    private void showToast(int stringId) {
        Toast.makeText(SignUpActivity.this,
                stringId,
                Toast.LENGTH_LONG).show();
    }

    private Intent makeResultIntent(SignupUser user) {
        Intent result = new Intent();
        result.putExtra(KEY_USERNAME, user.getUsername());
        result.putExtra(KEY_PASSWORD, user.getPassword());
        result.putExtra(KEY_FIRSTNAME, user.getFirstname());
        result.putExtra(KEY_LASTNAME, user.getLastname());
        result.putExtra(KEY_BIRTHDAY, user.getBirthday());
        result.putExtra(KEY_IS_PATIENT, user.getIsPatient());
        result.putExtra(KEY_MEDICAL_RECORD_NUMBER, user.getMedicalRecordNumber());
        result.putExtra(KEY_USERPIC, user.getUserpicFilename());
        return result;
    }
}
