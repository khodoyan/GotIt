package pro.khodoian.gotit.sql;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import pro.khodoian.gotit.models.Post;

/**
 * Class manages SQL operations with questions table
 *
 * @author eduardkhodoyan
 */
public class PostSqlOperations extends SqlOperations {

    public static final String TAG = PostSqlOperations.class.getCanonicalName();

    public PostSqlOperations(Context context) {
        super(context, PostContract.TABLE_NAME, PostContract.Columns.ID);
    }

    public void clearAllExceptNonPosted() {
        int numberDeleted = dbHelper.getWritableDatabase().delete(PostContract.TABLE_NAME,
                PostContract.Columns.SERVER_ID + " IS NOT NULL",
                null);
        Log.v(TAG, "clearAllExceptNonPosted: deleted " + numberDeleted + " items");
    }
}
