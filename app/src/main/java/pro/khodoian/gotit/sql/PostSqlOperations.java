package pro.khodoian.gotit.sql;

import android.content.Context;
import android.database.Cursor;

import pro.khodoian.gotit.models.Post;

/**
 * Class manages SQL operations with questions table
 *
 * @author eduardkhodoyan
 */
public class PostSqlOperations extends SqlOperations {
    public PostSqlOperations(Context context) {
        super(context, PostContract.TABLE_NAME, PostContract.Columns.ID);
    }

    public void clearAllExceptNonPosted() {
        dbHelper.getWritableDatabase().execSQL("DELETE FROM ? WHERE ? NOT IN ( \n" +
                "   SELECT ? FROM ?  \n" +
                "   WHERE ? = ? \n" +
                "   );",
                new String[]{
                        PostContract.TABLE_NAME,
                        PostContract.Columns.ID,
                        PostContract.Columns.ID,
                        PostContract.TABLE_NAME,
                        PostContract.Columns.SERVER_ID,
                        null
                });
    }

    public Cursor getNotPosted() {
        if (dbHelper != null)
            return dbHelper.getWritableDatabase().query(
                    PostContract.TABLE_NAME, // table name
                    null, // columns
                    PostContract.Columns.SERVER_ID + " is null or "
                            + PostContract.Columns.SERVER_ID + " = ?",
                    new String[] {""},
                    null,
                    null,
                    PostContract.Columns.ID + " " + SortOrder.ASC
                    );
        else
            return null;
    }
}
