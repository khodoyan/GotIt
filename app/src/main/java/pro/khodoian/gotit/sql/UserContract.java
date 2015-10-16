package pro.khodoian.gotit.sql;

import android.provider.BaseColumns;

/**
 * Class containing SQL constants for User class
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
            + Columns.USERPIC_FILENAME + " TEXT"
            + ");";
}
