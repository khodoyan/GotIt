package pro.khodoian.gotit.client;

import pro.khodoian.gotit.preferences.AuthenticationDetailsManager;
import pro.khodoian.gotit.retrofit.AccessPoint;
import retrofit.RestAdapter;
import retrofit.client.ApacheClient;

/**
 * Class is a wrapper for PostsProxy Retrofit interface. It is designed in order to make sure
 * that interface is accessed in error free, protected way
 *
 * @author eduardkhodoyan
 */
public class PostsService {
    // TODO: Implement class
    PostsProxy postsService;

    public PostsService(AuthenticationDetailsManager authManager) {
        if (authManager != null && authManager.getToken() != null
                && !authManager.getToken().equals("")) {
            postsService = getAuthorisedService(authManager.getToken());
        }
    }

    public static PostsProxy getAuthorisedService(String token) {
        return new SecuredRestAdapter()
                .setLoginEndpoint(AccessPoint.ENDPOINT + AccessPoint.TOKEN_PATH)
                .setToken(token)
                .setClient(new ApacheClient())
                .setEndpoint(AccessPoint.ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(PostsProxy.class);
    }
}
