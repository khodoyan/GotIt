package pro.khodoian.gotit.sql;

import android.content.Context;

/**
 * Class manages SQL operations with users table
 *
 * @author eduardkhodoyan
 */
public class UserSqlOperations extends SqlOperations {
    public UserSqlOperations(Context context) {
        super(context, UserContract.TABLE_NAME, UserContract.Columns.ID);
    }
}
