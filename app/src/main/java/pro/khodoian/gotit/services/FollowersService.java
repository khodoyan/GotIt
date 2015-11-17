package pro.khodoian.gotit.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import pro.khodoian.gotit.client.UserService;
import pro.khodoian.gotit.models.User;
import pro.khodoian.gotit.preferences.AuthenticationDetailsManager;
import pro.khodoian.gotit.sql.UserSqlOperations;

/**
 * Service that requests followers from server and save them to SQLite database
 */
public class FollowersService extends Service {

    public static final String TAG = FollowersService.class.getCanonicalName();

    private final IBinder binder = new LocalBinder();

    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public FollowersService getService() {
            // Return this instance of MyService so clients can call public methods
            return FollowersService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, FollowersService.class);
    }

    public void updateFollowers(final UserService.GetFollowersListener callbacks) {
        UserService userService = new UserService(new AuthenticationDetailsManager(this));
        userService.getFollowers(new UserService.GetFollowersListener() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                UserSqlOperations userSqlOperations = new UserSqlOperations(FollowersService.this);
                userSqlOperations.clearTable();
                if (users != null) {
                    // convert posts into content values
                    ArrayList<ContentValues> contentValues = new ArrayList<>();
                    for (User user : users) {
                        ContentValues cv = user.toContentValues();
                        if (cv != null)
                            contentValues.add(cv);
                    }
                    // convert ArrayList to array
                    ContentValues[] contentValuesArray =
                            new ContentValues[contentValues.size()];
                    contentValuesArray = contentValues.toArray(contentValuesArray);
                    // insert into local database
                    Log.v(TAG, "Posts added to local database: "
                            + userSqlOperations.insertBulk(contentValuesArray));
                }
                if (callbacks != null)
                    callbacks.onSuccess(users);
            }

            @Override
            public void onUnauthorized() {
                if (callbacks != null)
                    callbacks.onUnauthorized();
            }

            @Override
            public void onFailure() {
                if (callbacks != null)
                    callbacks.onFailure();
            }
        });
    }
}
