package pro.khodoian.gotit.client;

import android.util.Log;

import pro.khodoian.gotit.models.SignupUser;
import pro.khodoian.gotit.models.UserClient;
import pro.khodoian.gotit.preferences.AuthenticationDetailsManager;
import pro.khodoian.gotit.retrofit.AccessPoint;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Class is a wrapper for UsersProxy Retrofit interface. It is designed in order to make sure
 * that interface is accessed in error free, protected way
 *
 * @author eduardkhodoyan
 */
public class UserService {

    public static final String TAG = UserService.class.getCanonicalName();

    UsersProxy authorisedUsersService;
    UsersProxy unauthorisedUsersService;

    public interface SignupListener {
        void onSignupSuccess(int responseCode, SignupUser user);
        void onSignupFailure();
    }

    public interface CheckLoginListener {
        void onCheckLoginSuccess();
        void onCheckLoginUnauthorized();
        void onCheckLoginFailure();
    }

    public interface GetUserListener {
        void onSuccess(UserClient user);
        void onUnauthorized();
        void onFailure();
    }

    public interface GetPrincipalListener {
        void onSuccess(UserClient user);
        void onUnauthorized();
        void onFailure();
    }

    public UserService(AuthenticationDetailsManager authManager) {
        if (authManager.getToken() != null && !authManager.getToken().equals("")) {
            authorisedUsersService = getAuthorisedService(authManager.getToken());
        }
        unauthorisedUsersService = getUnauthorisedUsersService();
    }

    private UsersProxy getUnauthorisedUsersService() {
        return new RestAdapter.Builder()
                .setClient(new OkClient(UnsafeHttpsClient.getUnsafeOkHttpClient()))
                .setEndpoint(AccessPoint.ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .build()
                .create(UsersProxy.class);
    }

    private UsersProxy getAuthorisedService(String token) {
        return new SecuredRestAdapter()
                .setLoginEndpoint(AccessPoint.ENDPOINT + AccessPoint.TOKEN_PATH)
                .setToken(token)
                .setClient(new OkClient(UnsafeHttpsClient.getUnsafeOkHttpClient()))
                .setEndpoint(AccessPoint.ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .build()
                .create(UsersProxy.class);
    }

    public void signup(final SignupUser user, final SignupListener callback) {
        // todo: add validity check
        if (unauthorisedUsersService != null && user != null) {
            unauthorisedUsersService.signupUser(user, new Callback<Void>() {
                @Override
                public void success(Void noReturn, Response response) {
                    if (response != null)
                        callback.onSignupSuccess(response.getStatus(), user);
                }

                @Override
                public void failure(RetrofitError error) {
                    callback.onSignupFailure();
                }
            });
        }
    }

    public void checkLogin(final CheckLoginListener callback) {
        if (authorisedUsersService != null) {
            authorisedUsersService.checkAuth(new Callback<Void>() {
                @Override
                public void success(Void noReturn, Response response) {
                    Log.e(TAG, "Callback.success started");
                    if (response != null && response.getStatus() == HttpStatus.SC_OK)
                        callback.onCheckLoginSuccess();
                    else if (response != null
                            && (response.getStatus() == HttpStatus.SC_UNAUTHORIZED
                            || response.getStatus() == HttpStatus.SC_BAD_REQUEST))
                        callback.onCheckLoginUnauthorized();
                    else
                        callback.onCheckLoginFailure();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Callback.failure started");
                    callback.onCheckLoginFailure();
                }
            });
        } else {
            Log.e(TAG, "Couldn't create authorised service for some reason");
            callback.onCheckLoginFailure();
        }
    }

    public void getUser(String username, final GetUserListener callbacks) {
        if (authorisedUsersService != null) {
            authorisedUsersService.getUser(username, new Callback<UserClient>() {
                @Override
                public void success(UserClient user, Response response) {
                    Log.e(TAG, "Callback.success started");
                    if (response != null && response.getStatus() == HttpStatus.SC_OK)
                        callbacks.onSuccess(user);
                    else if (response != null
                            && (response.getStatus() == HttpStatus.SC_UNAUTHORIZED
                            || response.getStatus() == HttpStatus.SC_BAD_REQUEST))
                        callbacks.onUnauthorized();
                    else
                        callbacks.onFailure();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Callback.failure started");
                    callbacks.onFailure();
                }
            });
        } else {
            Log.e(TAG, "Couldn't create authorised service for some reason");
            callbacks.onFailure();
        }
    }

    public void getPrincipal(final GetPrincipalListener callbacks) {
        if (authorisedUsersService != null) {
            authorisedUsersService.getPrincipal(new Callback<UserClient>() {
                @Override
                public void success(UserClient user, Response response) {
                    Log.e(TAG, "Callback.success started");
                    if (response != null && response.getStatus() == HttpStatus.SC_OK)
                        callbacks.onSuccess(user);
                    else if (response != null
                            && (response.getStatus() == HttpStatus.SC_UNAUTHORIZED
                            || response.getStatus() == HttpStatus.SC_BAD_REQUEST))
                        callbacks.onUnauthorized();
                    else
                        callbacks.onFailure();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Callback.failure started");
                    callbacks.onFailure();
                }
            });
        } else {
            Log.e(TAG, "Couldn't create authorised service for some reason");
            callbacks.onFailure();
        }
    }
}
