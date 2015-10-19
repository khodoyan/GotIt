package pro.khodoian.gotit.sql;

/**
 * Class containing SQL constants for Question class
 *
 * @author eduardkhodoyan
 */
public class QuestionContract {
    public class Columns {
        public static final String ID = "_id";
        public static final String QUESTION = "question";
    }

    public static final String TABLE_NAME = "questions";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + " ("
            + Columns.ID + " INTEGER PRIMARY KEY, "
            + Columns.QUESTION + " TEXT NOT NULL"
            + ");";
}
