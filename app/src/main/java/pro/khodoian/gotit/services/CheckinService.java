package pro.khodoian.gotit.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import pro.khodoian.gotit.client.AuthenticationDetailsManager;
import pro.khodoian.gotit.client.HttpStatus;
import pro.khodoian.gotit.client.PostsProxy;
import pro.khodoian.gotit.client.PostsService;
import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.sql.PostUnsentSqlOperations;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Service to perform a checkin
 */
public class CheckinService extends Service {

    public static final String TAG = CheckinService.class.getCanonicalName();

    private PostsProxy postsProxy;

    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public CheckinService getService() {
            // Return this instance of MyService so clients can call public methods
            return CheckinService.this;
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, PostService.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        String token = new AuthenticationDetailsManager(this).getToken();
        if (token != null && !token.equals("")) {
            postsProxy = PostsService.getAuthorisedService(token);
        }
        return binder;
    }

    public void addPost(Post post) {
        Log.v(TAG, "adding one post by post object");
        if (post != null)
            postsProxy.addPost(post.toPostClient(), new Callback<Post>() {
                @Override
                public void success(Post post, Response response) {
                    Log.v(TAG, "Add post: success");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Add post: failure");
                    if (error != null && error.getResponse() != null) {
                        if (error.getResponse().getStatus() == HttpStatus.SC_UNAUTHORIZED) {
                            Log.e(TAG,
                                    "User unauthorized. Post will be sent on next application run");
                            return;
                        }
                        Log.e(TAG, "Add post: failure");
                    }

                }
            });
    }

    public void addPost(long id) {
        Log.v(TAG, "Adding one posts by id");
        Cursor cursor = new PostUnsentSqlOperations(CheckinService.this).queryById(null, id);
        if (cursor.moveToNext())
            addPost(Post.makePost(cursor));
    }


}
