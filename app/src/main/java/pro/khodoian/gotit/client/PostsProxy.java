package pro.khodoian.gotit.client;

import java.util.ArrayList;
import java.util.List;

import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.models.PostClient;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Retrofit interface to get access to Posts controller of server-side
 *
 * @author eduardkhodoyan
 */
public interface PostsProxy {
    public static final String SERVICE_PATH = "/post";
    public static final String FOLLOWERS = "/followers";
    public static final String ID = "id";
    public static final String USERNAME = "username";

    @GET(SERVICE_PATH + "/{" + USERNAME + "}")
    public List<Post> getPosts(@Path(USERNAME) String username);

    @GET(SERVICE_PATH + FOLLOWERS)
    public List<Post> getFollowersPosts();

    @GET(SERVICE_PATH + "/" + ID)
    public Post getPost(@Path(ID) long id);

    @POST(SERVICE_PATH)
    public Post addPost(@Body Post post);

    @POST(SERVICE_PATH)
    public void addPost(@Body PostClient post, Callback<Post> returned);

    /**
     * @return all posts that current user has access to
     */
    @GET(value = SERVICE_PATH + "/all")
    public void getAll(Callback<ArrayList<Post>> callback);
}
