package pro.khodoian.gotit.sqlasynctasks1;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.sql.PostContract;
import pro.khodoian.gotit.sql.PostSqlOperations;
import pro.khodoian.gotit.sql.SqlOperations;

/**
 * Async task that starts getting posts from local database and calls one of callback methods
 *
 * @author eduardkhodoyan
 */
public class GetPostsAsyncTask extends AsyncTask<Long, Void, ArrayList<Post>> {
    public interface Callback {
        void onGotPostsSuccess(ArrayList<Post> posts);
        void onGotPostFailed();
    }

    private Context context;
    private Callback callback;

    public GetPostsAsyncTask(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected ArrayList<Post> doInBackground(Long... longs) {
        String limit = null;
        if (longs != null && longs[0] != null)
            limit = String.valueOf(longs[0]);

        if (context == null)
            return null;

        PostSqlOperations operations = new PostSqlOperations(context);
        try {
            Cursor cursor = operations.query(null, null, null, PostContract.Columns.TIMESTAMP,
                    SqlOperations.SortOrder.DESC, limit);
            ArrayList<Post> result = new ArrayList<>();
            while (cursor.moveToNext()) {
                Post post = Post.makePost(cursor);
                if (post != null)
                    result.add(post);
            }
            return result;
        } catch (android.database.SQLException e) {
            Log.e(GetPostsAsyncTask.class.toString(), e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Post> posts) {
        super.onPostExecute(posts);
        if (posts != null)
            callback.onGotPostsSuccess(posts);
        else
            callback.onGotPostFailed();
    }
}
