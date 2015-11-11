package pro.khodoian.gotit.client;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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

    private String username;
    private String password;
    private String token;

    SharedPreferences preferences;

    public AuthenticationDetailsManager(Context context) {
        if (context != null) {
            preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            updateCredentialsFromPreferences();
        }
    }

    private void updateCredentialsFromPreferences() {
        if (preferences != null) {
            username = preferences.getString(KEY_USERNAME, null);
            password = preferences.getString(KEY_PASSWORD, null);
            token = preferences.getString(KEY_TOKEN, null);
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

    public boolean hasDetails() {
        return (username != null && !username.equals("")
                && password != null && !password.equals(""));
    }

    public void logout() {
        this.clearPassword();
        this.clearToken();
    }
}
