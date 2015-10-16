package pro.khodoian.gotit.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Parent class that manages SQL operations with the given table in given database
 *
 * @author eduardkhodoyan
 */
public class SqlOperations {
    DBHelper dbHelper;
    String tableName;
    String idColumn;

    public SqlOperations(Context context, String tableName, String idColumn) {
        dbHelper = new DBHelper(context);
        this.tableName = tableName;
        this.idColumn = idColumn;
    }

    /**
     * Inserts one record into the table specified at creation of SQLOperations object
     * Throws android.database.SQLException if database is not connected
     *
     * @param values - ContentValues representing record to be inserted
     * @return newly created ID of inserted record, or -1 if not inserted
     */
    public long insert(ContentValues values) {
        if (dbHelper == null)
            throw new android.database.SQLException ("Failed to insert row");
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.insert(tableName, null, values);
    }

    /**
     * Inserts records into the table specified at creation of SQLOperations object
     * Throws android.database.SQLException if database is not connected
     *
     * @param values array of ContentValues representing records to be inserted
     * @return number of inserted records
     */
    public int insertBulk(ContentValues[] values) {
        // Check input validity
        if (values == null || values.length == 0)
            return 0;
        // Check if DBHelper initialized properly
        if (dbHelper == null)
            throw new android.database.SQLException ("Failed to insert row");
        // Get database for write
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = 0;
        try {
            db.beginTransaction();
            for (ContentValues value : values) {
                if (db.insert(tableName, null, value) != -1)
                    result++;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return result;
    }

    /**
     * Queries records from the table specified at creation of SQLOperations object
     * Throws android.database.SQLException if database is not connected
     *
     * @param columns A list of which columns to return. Passing null will return all columns, which
     *                is discouraged to prevent reading data from storage that isn't going to be
     *                used
     * @param selection A filter declaring which rows to return, formatted as an SQL WHERE clause
     *                  (excluding the WHERE itself). Passing null will return all rows for the
     *                  given table
     * @param selectionArgs You may include ?s in selection, which will be replaced by the values
     *                      from selectionArgs, in order that they appear in the selection. The
     *                      values will be bound as Strings
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *                ORDER BY itself). Passing null will use the default sort order, which may be
     *                unordered
     * @param limit Limits the number of rows returned by the query, formatted as LIMIT clause.
     *              Passing null denotes no LIMIT clause
     * @return Cursor resulting from the query
     */
    public Cursor query(String[] columns, String selection, String[] selectionArgs,
                        String orderBy, String limit) {
        // Check if DBHelper initialized properly
        if (dbHelper == null)
            throw new android.database.SQLException ("Failed to query from database");
        return dbHelper.getReadableDatabase().query(
                tableName,
                columns, // projection
                selection, // selection clause with ? instead of args
                selectionArgs, // selection arguments to be set instead of ? above
                null, // GROUP BY
                null, // HAVING
                orderBy, // ORDER BY
                limit
        );
    }

    /**
     * Queries one item by id from the table specified at creation of SQLOperations object
     * Throws android.database.SQLException if database is not connected
     *
     * @param columns A list of which columns to return. Passing null will return all columns, which
     *                is discouraged to prevent reading data from storage that isn't going to be
     *                used
     * @param id An id of the item to be returned
     * @return Cursor with one or no record from the table
     */
    public Cursor queryById(String[] columns, long id) {
        // Check if DBHelper initialized properly
        if (dbHelper == null)
            throw new android.database.SQLException ("Failed to query from database");
        return query(
                columns,
                idColumn + " = ?",
                new String[]{String.valueOf(id)},
                null,
                String.valueOf(1)
        );
    }

    /**
     * Queries whole table specified at creation of SQLOperations object
     * Throws android.database.SQLException if database is not connected
     *
     * @param columns A list of which columns to return. Passing null will return all columns, which
     *                is discouraged to prevent reading data from storage that isn't going to be
     *                used
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause (excluding the
     *                ORDER BY itself). Passing null will use the default sort order, which may be
     *                unordered
     * @return Cursor with all records from the table
     */
    public Cursor queryAll(String[] columns, String orderBy) {
        return query(columns, null, null, orderBy, null);
    }

    /**
     * Deletes records from the table specified at creation of SQLOperations object
     * Throws android.database.SQLException if database is not connected
     *
     * @param whereClause the optional WHERE clause to apply when deleting. Passing null will delete
     *                    all rows
     * @param whereArgs You may include ?s in the where clause, which will be replaced by the values
     *                  from whereArgs. The values will be bound as Strings
     * @return number of records deleted from the table
     */
    public int delete(String whereClause, String[] whereArgs) {
        // Check if DBHelper initialized properly
        if (dbHelper == null)
            throw new android.database.SQLException ("Failed to query from database");
        return dbHelper.getWritableDatabase().delete(tableName, whereClause, whereArgs);
    }

    /**
     * Deletes records by id from the table specified at creation of SQLOperations object
     * Throws android.database.SQLException if database is not connected
     *
     * @param id of the record to be deleted
     * @return true if successful, false if not
     */
    public boolean deleteById(long id) {
        return (delete(
                idColumn + " = ?",
                new String[]{String.valueOf(id)}
        ) > 0);
    }

    /**
     * Clears the table specified at creation of SQLOperations object
     * Throws android.database.SQLException if database is not connected
     *
     * @return number of deleted rows
     */
    public int clearTable() {
        // Check if DBHelper initialized properly
        if (dbHelper == null)
            throw new android.database.SQLException ("Failed to query from database");
        return delete(null, null);
    }

    /**
     * Updates rows in the database with values
     * Throws android.database.SQLException if database is not connected
     *
     * @param values a map from column names to new column values. null is a valid value that will
     *               be translated to NULL
     * @param selection the optional WHERE clause to apply when updating. Passing null will update
     *                  all rows
     * @param selectionArgs You may include ?s in the where clause, which will be replaced by the
     *                      values from whereArgs. The values will be bound as Strings
     * @return the number of rows affected
     */
    public int update(ContentValues values, String selection, String[] selectionArgs) {
        // Check if DBHelper initialized properly
        if (dbHelper == null)
            throw new android.database.SQLException ("Failed to query from database");
        return dbHelper.getWritableDatabase().update(tableName, values, selection, selectionArgs);
    }

    /**
     * Updates rows in the database with values
     * Throws android.database.SQLException if database is not connected
     *
     * @param values a map from column names to new column values. null is a valid value that will
     *               be translated to NULL
     * @param id An id of the record to be updated
     * @return true if successful, false if not
     */
    public boolean updateById(ContentValues values, long id) {
        // Check if DBHelper initialized properly
        if (dbHelper == null)
            throw new android.database.SQLException ("Failed to query from database");
        return (update(values, idColumn + " = ?", new String[]{String.valueOf(id)}) > 0);
    }
}
