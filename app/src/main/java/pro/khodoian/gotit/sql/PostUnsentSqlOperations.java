package pro.khodoian.gotit.sql;

import android.content.Context;

/**
 * Class manages SQL operations with questions table
 *
 * @author eduardkhodoyan
 */
public class PostUnsentSqlOperations extends SqlOperations {

    public static final String TAG = PostUnsentSqlOperations.class.getCanonicalName();

    public PostUnsentSqlOperations(Context context) {
        super(context, PostUnsentContract.TABLE_NAME, PostUnsentContract.Columns.ID);
    }
}
