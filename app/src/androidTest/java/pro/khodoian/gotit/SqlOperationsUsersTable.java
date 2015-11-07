package pro.khodoian.gotit;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import pro.khodoian.gotit.models.User;
import pro.khodoian.gotit.sql.SqlOperations;
import pro.khodoian.gotit.sql.UserContract;
import pro.khodoian.gotit.view.MainActivity;

/**
 * Test class for pro.khodoian.gotit.sql.SqlOperations based on User class and "users" table
 *
 * @author eduardkhodoyan
 */

public class SqlOperationsUsersTable extends ActivityInstrumentationTestCase2<MainActivity> {
    public ArrayList<User> sampleUsers;

    public SqlOperations usersSql;

    public SqlOperationsUsersTable() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //Intent launchIntent = new Intent(getInstrumentation()
        //        .getTargetContext(), MainActivity.class);
        //startActivity(launchIntent, null, null);
        usersSql = new SqlOperations(
                getActivity(),
                UserContract.TABLE_NAME,
                UserContract.Columns.ID
        );
        generateSampleUsers();
    }

    @Override
    protected void tearDown() throws Exception {
        usersSql.clearTable();
        usersSql = null;
        super.tearDown();
    }

    public void generateSampleUsers() {
        sampleUsers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            sampleUsers.add(new User(
                    true,
                    "USER" + i,
                    "USER" + i + " name",
                    "USER" + i + " lastname",
                    new GregorianCalendar().getTimeInMillis(),
                    "USER" + i + " medical record number",
                    null,
                    true,
                    true
            ));
        }
    }

    @MediumTest
    public void testInsertOneRecord() {
        // Check that function returns id
        long id = usersSql.insert(sampleUsers.get(0).toContentValues());
        assertTrue(id != -1);

        // Check that added item equals to the added one
        Cursor cursor = usersSql.queryById(new String[] {UserContract.Columns.USERNAME}, id);
        assertTrue(cursor.getCount() > 0);
        cursor.moveToFirst();
        String username = cursor.getString(cursor.getColumnIndex(UserContract.Columns.USERNAME));
        assertEquals("USER0", username);
    }

    @MediumTest
    public void testInsertBulk() {
        usersSql.clearTable();
        // Test that correct number of items is added
        int result = usersSql.insertBulk(new ContentValues[]{
                sampleUsers.get(1).toContentValues(),
                sampleUsers.get(2).toContentValues()
        });
        assertEquals(2, result);

        // Check that correct number of records and correct values are actually added
        Cursor cursor = usersSql.query(new String[] {UserContract.Columns.USERNAME},
                null, null, null, null, null);
        assertEquals(2, cursor.getCount());
        boolean user1Correct = false;
        boolean user2Correct = false;

        while (cursor.moveToNext()) {
            switch (cursor.getString(cursor.getColumnIndex(UserContract.Columns.USERNAME))) {
                case "USER1":
                    user1Correct = true;
                    break;
                case "USER2":
                    user2Correct = true;
                    break;
            }
        }
        assertTrue(user1Correct);
        assertTrue(user2Correct);
    }

    @MediumTest
    public void testDeleteById() {
        long id = usersSql.insert(sampleUsers.get(3).toContentValues());
        boolean result = usersSql.deleteById(id);
        assertTrue(result);
    }

    @MediumTest
    public void testClearTable() {
        usersSql.clearTable();
        usersSql.insertBulk(new ContentValues[]{
                sampleUsers.get(1).toContentValues(),
                sampleUsers.get(2).toContentValues()
        });
        int result = usersSql.clearTable();
        assertEquals(2, result);
    }

    @MediumTest
    public void testDelete() {
        usersSql.clearTable();
        usersSql.insertBulk(new ContentValues[]{
                sampleUsers.get(1).toContentValues(),
                sampleUsers.get(2).toContentValues()
        });
        int result = usersSql.delete(
                UserContract.Columns.USERNAME + " = ?",
                new String[]{"USER1"}
        );
        assertEquals(1, result);
    }

    @MediumTest
    public void testUpdateById() {
        usersSql.clearTable();
        usersSql.insertBulk(new ContentValues[]{
                sampleUsers.get(1).toContentValues(),
                sampleUsers.get(2).toContentValues()
        });

        // Check that correct number of entries is updated
        int result = usersSql.update(
                sampleUsers.get(3).toContentValues(),
                UserContract.Columns.USERNAME + " = ?",
                new String[]{"USER1"}
        );
        assertEquals(1, result);

        // Check that new value is correct
        Cursor cursor = usersSql.query(
                new String[] {UserContract.Columns.USERNAME},
                UserContract.Columns.USERNAME + " = ?",
                new String[]{"USER3"},
                null,
                null,
                null
        );
        assertEquals(1, cursor.getCount());
    }
}
