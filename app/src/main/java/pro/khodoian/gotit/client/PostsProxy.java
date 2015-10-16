package pro.khodoian.gotit.client;

import java.util.List;

import pro.khodoian.gotit.models.Post;
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
    public static final String SERVICE_PATH = "/posts";
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
}
