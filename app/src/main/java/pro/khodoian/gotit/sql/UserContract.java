package pro.khodoian.gotit.sql;

/**
 * Class containing SQL constants for User class (followers and following)
 *
 * @author eduardkhodoyan
 */
public class UserContract {
    public static final class Columns {
        public static final String ID = "_id";
        public static final String USERNAME = "username";
        public static final String IS_PATIENT = "is_patient";
        public static final String FIRSTNAME = "firstname";
        public static final String LASTNAME = "lastname";
        public static final String BIRTHDAY = "birthday";
        public static final String MEDICAL_RECORD_NUMBER = "medical_record_number";
        public static final String USERPIC_FILENAME = "userpic_filename";
        public static final String IS_CONFIRMED_BY_YOU = "is_confirmed_by_you";
        public static final String IS_CONFIRMED_BY_FRIEND = "is_confirmed_by_friend";
        public static final String IS_FOLLOWED = "is_followed";
        public static final String SHARE_FEELING = "share_feeling";
        public static final String SHARE_BLOOD_SUGAR = "share_blood_sugar";
        public static final String SHARE_INSULIN = "share_insulin";
        public static final String SHARE_QUESTIONS = "share_questions";
    }

    public static final String TABLE_NAME = "users";
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + " ("
            + Columns.ID + " INTEGER PRIMARY KEY, "
            + Columns.USERNAME + " TEXT NOT NULL, "
            + Columns.IS_PATIENT + " INTEGER, "
            + Columns.FIRSTNAME + " TEXT NOT NULL, "
            + Columns.LASTNAME + " TEXT NOT NULL, "
            + Columns.BIRTHDAY + " INTEGER, "
            + Columns.MEDICAL_RECORD_NUMBER + " TEXT, "
            + Columns.USERPIC_FILENAME + " TEXT, "
            + Columns.IS_CONFIRMED_BY_YOU + " INTEGER, "
            + Columns.IS_CONFIRMED_BY_FRIEND + " INTEGER, "
            + Columns.IS_FOLLOWED + " INTEGER, "
            + Columns.SHARE_FEELING + " INTEGER, "
            + Columns.SHARE_BLOOD_SUGAR + " INTEGER, "
            + Columns.SHARE_INSULIN + " INTEGER, "
            + Columns.SHARE_QUESTIONS + " INTEGER"
            + ");";
}
