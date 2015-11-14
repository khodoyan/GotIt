package pro.khodoian.gotit.sqlasynctasks1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import pro.khodoian.gotit.sql.PostUnsentSqlOperations;

/**
 * Async task that starts deleting post by id from local database and calls one of callback methods
 *
 * @author eduardkhodoyan
 */

public class DeleteUnsentPostByIdAsyncTask extends AsyncTask<Long, Void, Boolean> {
    private Context context;
    private Callback callback;

    public interface Callback {
        void onPostDeletedByIdSuccess();
        void onPostDeletedByIdFailed();
    }

    public DeleteUnsentPostByIdAsyncTask(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Long... longs) {
        if (longs == null || longs[0] == null || context == null)
            return null;

        try {
            PostUnsentSqlOperations operations = new PostUnsentSqlOperations(context);
            return operations.deleteById(longs[0]);
        } catch (android.database.SQLException e) {
            Log.e(AddUnsentPostAsyncTask.class.toString(), e.toString());
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
