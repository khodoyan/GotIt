package pro.khodoian.gotit.services;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import pro.khodoian.gotit.client.AuthenticationDetailsManager;
import pro.khodoian.gotit.client.HttpStatus;
import pro.khodoian.gotit.client.PostsProxy;
import pro.khodoian.gotit.client.PostsService;
import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.sql.PostSqlOperations;
import pro.khodoian.gotit.sql.PostUnsentSqlOperations;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Service that requests posts from server and save them to SQLite database
 */
public class PostService extends Service {

    public static final String TAG = PostService.class.getCanonicalName();

    public interface UpdateFeedListener {
        void onSuccess();
        void onUnauthorized();
        void onFailure();
    }

    public interface AddPostListener {
        void onSuccess(Post post);
        void onUnauthorized();
        void onFailure();
    }

    public interface DeletePostListener {
        void onSuccess();
        void onUnauthorized();
        void onFailure();
    }



    private PostsProxy postsProxy;

    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Registered callbacks
    private UpdateFeedListener callbacks;

    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public PostService getService() {
            // Return this instance of MyService so clients can call public methods
            return PostService.this;
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

    public void addPosts(AddPostListener callbacks) {
        // TODO: implement method
    }

    public void addPost(Post post, final AddPostListener callbacks) {
        Log.v(TAG, "adding one post by post object");
        if (post != null)
            postsProxy.addPost(post.toPostClient(), new Callback<Post>() {
                @Override
                public void success(Post post, Response response) {
                    Log.e(TAG, "Add post: success");
                    callbacks.onSuccess(post);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Add post: failure");
                    if (error != null && error.getResponse() != null) {
                        if (error.getResponse().getStatus() == HttpStatus.SC_UNAUTHORIZED) {
                            callbacks.onUnauthorized();
                            return;
                        }
                        callbacks.onFailure();
                    }

                }
            });
    }

    public void addPost(long id, AddPostListener callbacks) {
        Log.v(TAG, "Adding one posts by id");
        Cursor cursor = new PostUnsentSqlOperations(PostService.this).queryById(null, id);
        if (cursor.moveToNext())
            addPost(Post.makePost(cursor), callbacks);
    }

    public void deletePost(final long serverId) {
        // TODO: Don't understand, why method inside service is called on the main thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (postsProxy != null) {
                    postsProxy.deletePost(serverId);
                }
            }
        }).start();

    }


    public void updatePosts(final UpdateFeedListener callbacks) throws Exception {
        if (postsProxy != null) {
            postsProxy.getAll(new Callback<ArrayList<Post>>() {
                @Override
                public void success(ArrayList<Post> posts, Response response) {
                    switch (response.getStatus()){
                        case HttpStatus.SC_OK:
                            PostSqlOperations postOperations =
                                    new PostSqlOperations(PostService.this);
                            // clear SQLite posts table
                            Log.v(TAG, "Deleted items from local database: "
                                    + postOperations.clearTable());

                            // add results to SQLite posts table
                            if (posts != null) {
                                // convert posts into content values
                                ArrayList<ContentValues> contentValues = new ArrayList<>();
                                for (Post post : posts) {
                                    ContentValues cv = post.toContentValues();
                                    if (cv != null)
                                        contentValues.add(cv);
                                }
                                // convert ArrayList to array
                                ContentValues[] contentValuesArray =
                                        new ContentValues[contentValues.size()];
                                contentValuesArray = contentValues.toArray(contentValuesArray);
                                // insert into local database
                                Log.v(TAG, "Posts added to local database: "
                                        + postOperations.insertBulk(contentValuesArray));
                            }
                            callbacks.onSuccess();
                            break;
                        case HttpStatus.SC_UNAUTHORIZED:
                            callbacks.onUnauthorized();
                            break;
                        case HttpStatus.SC_BAD_REQUEST:
                            callbacks.onUnauthorized();
                            break;
                        default:
                            callbacks.onFailure();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    callbacks.onFailure();
                }
            });
        }
    }
}
