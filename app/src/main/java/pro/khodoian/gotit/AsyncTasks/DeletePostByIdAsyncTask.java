package pro.khodoian.gotit.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import pro.khodoian.gotit.sql.PostSqlOperations;

/**
 * Async task that starts deleting post by id from local database and calls one of callback methods
 *
 * @author eduardkhodoyan
 */

public class DeletePostByIdAsyncTask extends AsyncTask<Long, Void, Boolean> {
    private Context context;
    private Callback callback;

    public interface Callback {
        void onPostDeletedByIdSuccess();
        void onPostDeletedByIdFailed();
    }

    public DeletePostByIdAsyncTask(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Long... longs) {
        if (longs == null || longs[0] == null || context == null)
            return null;

        try {
            PostSqlOperations operations = new PostSqlOperations(context);
            return operations.deleteById(longs[0]);
        } catch (android.database.SQLException e) {
            Log.e(AddPostAsyncTask.class.toString(), e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result == null || result == false)
            callback.onPostDeletedByIdFailed();
        else
            callback.onPostDeletedByIdSuccess();
    }
}
