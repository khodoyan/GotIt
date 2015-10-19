package pro.khodoian.gotit.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Database helper class based on SQLiteOpenHelper
 *
 * @author eduardkhodoyan
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pro_khodoyan_gotit_db";
    public static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(
                context,
                context.getFilesDir() + File.separator + DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserContract.CREATE_TABLE);
        db.execSQL(PostContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // TODO: for future versions, this one shall be updated
        db.execSQL("DROP TABLE IF EXISTS "
                + UserContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "
                + PostContract.TABLE_NAME);

        onCreate(db);
    }
}
