package pro.khodoian.gotit.client;

import java.util.ArrayList;

import pro.khodoian.gotit.models.SignupUser;
import pro.khodoian.gotit.models.TestModel;
import pro.khodoian.gotit.models.User;
import pro.khodoian.gotit.models.UserClient;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Retrofit interface to get access to Users controller of server-side
 *
 * @author eduardkhodoyan
 */
public interface UsersProxy {
    public static final String SERVICE_PATH = "/user";
    public static final String FOLLOWERS_PATH = "/followers";
    public static final String SIGNUP = "/signup";
    public static final String CHECK_AUTH = "/testauth";

    @POST(SERVICE_PATH + SIGNUP)
    public void signupUser(@Body SignupUser user, Callback<Void> callback);

    @GET(CHECK_AUTH)
    public void checkAuth(Callback<Void> callback);

    @GET(SERVICE_PATH + "/{username}")
    public void getUser(@Path("username") String username, Callback<UserClient> callback);

    @GET(SERVICE_PATH + "/principal")
    public void getPrincipal(Callback<UserClient> callback);

    @GET(FOLLOWERS_PATH)
    public void getFollowers(Callback<ArrayList<User>> callback);
}
