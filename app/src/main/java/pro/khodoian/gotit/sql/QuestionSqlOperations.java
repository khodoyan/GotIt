package pro.khodoian.gotit.sql;

import android.content.Context;

/**
 * Class manages SQL operations with questions table
 *
 * @author eduardkhodoyan
 */
public class QuestionSqlOperations extends SqlOperations {
    public QuestionSqlOperations(Context context) {
        super(context, QuestionContract.TABLE_NAME, QuestionContract.Columns.ID);
    }
}
