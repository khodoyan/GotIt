package pro.khodoian.gotit.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.sql.PostSqlOperations;

/**
 * Async task that starts getting posts from local database and calls one of callback methods
 *
 * @author eduardkhodoyan
 */
public class AddPostAsyncTask extends AsyncTask<Post,Void,Long> {
    Context context;
    Callback callback;

    public interface Callback {
        void onPostAddedSuccess(Long id);
        void onPostAddedFailed();
    }


    public AddPostAsyncTask(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Long doInBackground(Post... posts) {
        if (posts != null && posts[0] != null && !posts[0].isBlank() && context != null) {
            try {
                PostSqlOperations postOperations = new PostSqlOperations(context);
                return postOperations.insert(posts[0].toContentValues());
            } catch (android.database.SQLException e) {
                Log.e(AddPostAsyncTask.class.toString(), e.toString());
                return null;
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(Long id) {
        super.onPostExecute(id);
        if (id == null || id == -1)
            callback.onPostAddedFailed();
        else
            callback.onPostAddedSuccess(id);
    }
}
