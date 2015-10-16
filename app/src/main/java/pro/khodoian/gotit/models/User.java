package pro.khodoian.gotit.models;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.sql.UserContract;

/**
 * Simple POJO class for User entity
 *
 * @author eduardkhodoyan
 */
public class User implements ToContentValues {
    private boolean isPatient;
    private String username;
    private String firstName;
    private String lastName;
    private long birthDay;
    private String medicalRecordNumber;
    private String userpicFilename;
    private Drawable userpic;

    public User(boolean isPatient, String username, String firstName, String lastName,
                long birthDay, String medicalRecordNumber, String userpicFilename) {
        this.isPatient = isPatient;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.medicalRecordNumber = medicalRecordNumber;
        this.userpicFilename = userpicFilename;
    }

    public boolean isPatient() {
        return isPatient;
    }

    public void setIsPatient(boolean isPatient) {
        this.isPatient = isPatient;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(long birthDay) {
        this.birthDay = birthDay;
    }

    public String getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public void setMedicalRecordNumber(String medicalRecordNumber) {
        this.medicalRecordNumber = medicalRecordNumber;
    }

    public String getUserpicFilename() {
        return userpicFilename;
    }

    public void setUserpicFilename(String userpicFilename) {
        this.userpicFilename = userpicFilename;
    }

    public Drawable getUserpic() {
        return userpic;
    }

    public void setUserpic(Drawable userpic) {
        this.userpic = userpic;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(UserContract.Columns.USERNAME, this.username);
        values.put(UserContract.Columns.IS_PATIENT, this.isPatient ? 1 : 0);
        values.put(UserContract.Columns.USERPIC_FILENAME, this.userpicFilename);
        values.put(UserContract.Columns.BIRTHDAY, this.birthDay);
        values.put(UserContract.Columns.FIRSTNAME, this.firstName);
        values.put(UserContract.Columns.LASTNAME, this.lastName);
        values.put(UserContract.Columns.MEDICAL_RECORD_NUMBER, this.medicalRecordNumber);
        return values;
    }
}
