package pro.khodoian.gotit.sql;

import android.content.Context;

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
}
