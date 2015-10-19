package pro.khodoian.gotit.sql;

import android.content.Context;

import pro.khodoian.gotit.models.Post;

/**
 * Created by eduardkhodoyan on 10/17/15.
 */
public class PostSqlOperations extends SqlOperations {
    public PostSqlOperations(Context context) {
        super(context, PostContract.TABLE_NAME, PostContract.Columns.ID);
    }
}
