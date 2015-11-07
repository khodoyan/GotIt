package pro.khodoian.gotit.client;

import pro.khodoian.gotit.models.SignupUser;
import pro.khodoian.gotit.models.TestModel;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Retrofit interface to get access to Users controller of server-side
 *
 * @author eduardkhodoyan
 */
public interface UsersProxy {
    public static final String SERVICE_PATH = "/user";
    public static final String SIGNUP = "/signup";
    public static final String CHECK_AUTH = "/testauth";

    @POST(SERVICE_PATH + SIGNUP)
    public void signupUser(@Body SignupUser user, Callback<Void> callback);

    @GET(CHECK_AUTH)
    public void checkAuth(Callback<Void> callback);
}
