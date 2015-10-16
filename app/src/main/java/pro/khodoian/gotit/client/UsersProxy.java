package pro.khodoian.gotit.client;

import java.util.List;

import pro.khodoian.gotit.models.User;
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
    public static final String SERVICE_PATH = "/users";
    public static final String FOLLOWERS = "/followers";
    public static final String FOLLOWING = "/following";
    public static final String SIGNUP = "/signup";
    public static final String INVITE = "/invite";
    public static final String CONFIRM = "/confirm_following";
    public static final String REJECT = "/reject_following";
    public static final String USERNAME = "username";
    public static final String CHECK_CONTACTS = "/check_contacts";

    @GET(SERVICE_PATH + "/{" + USERNAME + "}")
    public User getUser(@Path(USERNAME) String username);

    @GET(SERVICE_PATH + FOLLOWERS)
    public List<User> getFollowers();

    @GET(SERVICE_PATH + FOLLOWING)
    public List<User> getFollowing();

    @POST(SERVICE_PATH + SIGNUP)
    public User signupUser(@Body User user);

    @POST(SERVICE_PATH + "/{" + USERNAME + "}" + INVITE)
    public void inviteFollower(@Path(USERNAME) String username);

    @POST(SERVICE_PATH + "/{" + USERNAME + "}" + CONFIRM)
    public void confirmFollwing(@Path(USERNAME) String username);

    @POST(SERVICE_PATH + "/{" + USERNAME + "}" + REJECT)
    public void rejectFollowing(@Path(USERNAME) String username);

    @GET(SERVICE_PATH + CHECK_CONTACTS)
    public List<User> checkContacts(@Body List<String> contactUsernames);
}
