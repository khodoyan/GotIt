package pro.khodoian.gotit.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import pro.khodoian.gotit.models.UserClient;

/**
 * Class for managing and keeping authentication data: username, password and token
 *
 * @author eduardkhodoyan
 */
public class AuthenticationDetailsManager {

    public static final String CLIENT = "mobile";
    public static final String SECRET = "";

    public static final String PREFERENCES = "MyPrefsFile";

    public static final String KEY_USERNAME = "USERNAME";
    public static final String KEY_PASSWORD = "PASSWORD";
    public static final String KEY_TOKEN = "TOKEN";
    public static final String KEY_FIRSTNAME = "FIRSTNAME";
    public static final String KEY_LASTNAME = "LASTNAME";
    public static final String KEY_IS_PATIENT = "IS_PATIENT";

    private String username;
    private String password;
    private String token;

    private String firstname;
    private String lastname;
    private boolean isPatient;


    SharedPreferences preferences;

    public AuthenticationDetailsManager(Context context) {
        if (context != null) {
            preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            updateDetailsFromPreferences();
        }
    }

    private void updateDetailsFromPreferences() {
        if (preferences != null) {
            username = preferences.getString(KEY_USERNAME, null);
            password = preferences.getString(KEY_PASSWORD, null);
            token = preferences.getString(KEY_TOKEN, null);
            firstname = preferences.getString(KEY_FIRSTNAME, null);
            lastname = preferences.getString(KEY_LASTNAME, null);
            isPatient = preferences.getBoolean(KEY_IS_PATIENT, false);

        }
    }

    public void clearAll() {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(KEY_USERNAME);
            editor.remove(KEY_PASSWORD);
            editor.remove(KEY_TOKEN);
            editor.remove(KEY_FIRSTNAME);
            editor.remove(KEY_LASTNAME);
            editor.remove(KEY_IS_PATIENT);
            editor.commit();
        }
    }

    public void clearCredentials() {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(KEY_USERNAME);
            editor.remove(KEY_PASSWORD);
            editor.commit();
        }
    }

    public void clearPassword() {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(KEY_PASSWORD);
            editor.commit();
        }
    }

    public void clearDetails() {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(KEY_FIRSTNAME);
            editor.remove(KEY_LASTNAME);
            editor.remove(KEY_IS_PATIENT);
            editor.commit();
        }
    }

    public void clearToken() {
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(KEY_PASSWORD);
            editor.commit();
        }
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, password);
            editor.commit();
        }
    }

    public void setUserDetails(UserClient user) {
        if (user != null && preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            username = user.getUsername();
            firstname = user.getFirstName();
            lastname = user.getLastName();
            isPatient = user.getIsPatient();

            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_FIRSTNAME, firstname);
            editor.putString(KEY_LASTNAME, lastname);
            editor.putBoolean(KEY_IS_PATIENT, isPatient);
            editor.commit();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_USERNAME, username);
            editor.commit();
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_PASSWORD, password);
            editor.commit();
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KEY_TOKEN, token);
            editor.commit();
        }
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public boolean isPatient() {
        return isPatient;
    }

    public boolean hasDetails() {
        return (username != null && !username.equals("")
                && password != null && !password.equals(""));
    }

    public void logout() {
        this.clearPassword();
        this.clearToken();
    }
}
