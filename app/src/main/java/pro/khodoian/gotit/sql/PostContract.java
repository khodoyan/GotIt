package pro.khodoian.gotit.sql;

/**
 * Class containing SQL constants for Post class
 *
 * @author eduardkhodoyan
 */
public class PostContract {
    public static final class Columns {
        public static final String ID = "_id";
        public static final String SERVER_ID = "server_id";
        public static final String USERNAME = "username";
        public static final String UPDATED_AT = "updated_at";
        public static final String DELETED_AT = "deleted_at";
        public static final String TIMESTAMP = "timestamp";
        public static final String IS_SHARED = "is_shared";
        public static final String FEELING = "feeling";
        public static final String BLOOD_SUGAR = "blood_sugar";
        public static final String ADMINISTERED_INSULIN = "administered_insulin";
        public static final String QUESTIONNAIRE = "questinnaire";
    }

    public static final String TABLE_NAME = "posts";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + " ("
            + Columns.ID + " INTEGER PRIMARY KEY, "
            + Columns.SERVER_ID + " INTEGER, "
            + Columns.USERNAME + " TEXT NOT NULL, "
            + Columns.UPDATED_AT + " INTEGER, "
            + Columns.DELETED_AT + " INTEGER, "
            + Columns.TIMESTAMP + " INTEGER, "
            + Columns.IS_SHARED + " INTEGER, "
            + Columns.FEELING + " TEXT, "
            + Columns.BLOOD_SUGAR + " REAL, "
            + Columns.ADMINISTERED_INSULIN + " INTEGER, "
            + Columns.QUESTIONNAIRE + " TEXT"
            + ");";
}
