package pro.khodoian.gotit.models;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.sql.UserContract;

/**
 * Simple POJO class for User entity
 *
 * @author eduardkhodoyan
 */
public class User implements ToContentValues {
    private transient long id;
    private transient boolean isPatient;
    private String follower;
    private String firstName;
    private String lastName;
    private transient long birthDay;
    private transient String medicalRecordNumber;
    private String userpicFilename;
    private transient Drawable userpic;
    private boolean confirmedByPatient;
    private boolean confirmedByFollower;
    private boolean followed;
    private boolean shareFeeling;
    private boolean shareBloodSugar;
    private boolean shareInsulin;
    private boolean shareQuestions;

    public User() {
    }

    public User(boolean isPatient, String follower, String firstName, String lastName,
                long birthDay, String medicalRecordNumber, String userpicFilename,
                boolean confirmedByPatient, boolean confirmedByFollower) {
        this.isPatient = isPatient;
        this.follower = follower;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.medicalRecordNumber = medicalRecordNumber;
        this.userpicFilename = userpicFilename;
        this.confirmedByPatient = confirmedByPatient;
        this.confirmedByFollower = confirmedByFollower;
    }

    public User(boolean isPatient,
                String follower,
                String firstName,
                String lastName,
                long birthDay,
                String medicalRecordNumber,
                String userpicFilename,
                Drawable userpic,
                boolean confirmedByPatient,
                boolean confirmedByFollower,
                boolean followed,
                boolean shareFeeling,
                boolean shareBloodSugar,
                boolean shareInsulin,
                boolean shareQuestions) {
        this.isPatient = isPatient;
        this.follower = follower;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.medicalRecordNumber = medicalRecordNumber;
        this.userpicFilename = userpicFilename;
        this.userpic = userpic;
        this.confirmedByPatient = confirmedByPatient;
        this.confirmedByFollower = confirmedByFollower;
        this.followed = followed;
        this.shareFeeling = shareFeeling;
        this.shareBloodSugar = shareBloodSugar;
        this.shareInsulin = shareInsulin;
        this.shareQuestions = shareQuestions;
    }

    public static User makeUser(Cursor cursor) {
        User result = new User();

        if (cursor.getColumnIndex(UserContract.Columns.ID) >= 0)
            result.id = cursor.getLong(cursor.getColumnIndex(UserContract.Columns.ID));
        else
            return null;

        //isPatient
        if (cursor.getColumnIndex(UserContract.Columns.IS_PATIENT) >= 0)
            result.isPatient = (cursor.getInt(cursor.getColumnIndex(UserContract.Columns.ID)) == 1);
        else
            return null;

        // follower
        if (cursor.getColumnIndex(UserContract.Columns.USERNAME) >= 0)
            result.follower = cursor.getString(
                    cursor.getColumnIndex(UserContract.Columns.USERNAME));
        else
            return null;

        // firstName
        if (cursor.getColumnIndex(UserContract.Columns.FIRSTNAME) >= 0)
            result.firstName = cursor.getString(
                    cursor.getColumnIndex(UserContract.Columns.FIRSTNAME));
        else
            result.firstName = null;

        // lastName
        if (cursor.getColumnIndex(UserContract.Columns.LASTNAME) >= 0)
            result.lastName = cursor.getString(
                    cursor.getColumnIndex(UserContract.Columns.LASTNAME));
        else
            result.lastName = null;

        // birthDay
        if (cursor.getColumnIndex(UserContract.Columns.BIRTHDAY) >= 0)
            result.birthDay = cursor.getLong(
                    cursor.getColumnIndex(UserContract.Columns.BIRTHDAY));
        else
            result.birthDay = 0;

        // medicalRecordNumber
        if (cursor.getColumnIndex(UserContract.Columns.MEDICAL_RECORD_NUMBER) >= 0)
            result.medicalRecordNumber = cursor.getString(
                    cursor.getColumnIndex(UserContract.Columns.MEDICAL_RECORD_NUMBER));
        else
            result.medicalRecordNumber = null;

        // userpicFilename
        if (cursor.getColumnIndex(UserContract.Columns.USERPIC_FILENAME) >= 0)
            result.userpicFilename = cursor.getString(
                    cursor.getColumnIndex(UserContract.Columns.USERPIC_FILENAME));
        else
            result.userpicFilename = null;

        // userpic to be assigned separately
        result.userpic = null;

        // confirmedByPatient
        if (cursor.getColumnIndex(UserContract.Columns.IS_CONFIRMED_BY_YOU) >= 0)
            result.confirmedByPatient = (cursor.getInt(
                    cursor.getColumnIndex(UserContract.Columns.IS_CONFIRMED_BY_YOU)) == 1);
        else
            return null;

        // confirmedByFollower
        if (cursor.getColumnIndex(UserContract.Columns.IS_CONFIRMED_BY_FRIEND) >= 0)
            result.confirmedByFollower = (cursor.getInt(
                    cursor.getColumnIndex(UserContract.Columns.IS_CONFIRMED_BY_FRIEND)) == 1);
        else
            return null;

        // getFollowed
        if (cursor.getColumnIndex(UserContract.Columns.IS_FOLLOWED) >= 0)
            result.followed = (cursor.getInt(
                    cursor.getColumnIndex(UserContract.Columns.IS_FOLLOWED)) == 1);
        else
            return null;

        // shareFeeling
        if (cursor.getColumnIndex(UserContract.Columns.SHARE_FEELING) >= 0)
            result.shareFeeling = (cursor.getInt(
                    cursor.getColumnIndex(UserContract.Columns.SHARE_FEELING)) == 1);
        else
            return null;

        // shareBloodSugar
        if (cursor.getColumnIndex(UserContract.Columns.SHARE_BLOOD_SUGAR) >= 0)
            result.shareBloodSugar = (cursor.getInt(
                    cursor.getColumnIndex(UserContract.Columns.SHARE_BLOOD_SUGAR)) == 1);
        else
            return null;

        // shareInsulin
        if (cursor.getColumnIndex(UserContract.Columns.SHARE_INSULIN) >= 0)
            result.shareInsulin = (cursor.getInt(
                    cursor.getColumnIndex(UserContract.Columns.SHARE_INSULIN)) == 1);
        else
            return null;

        // shareQuestions
        if (cursor.getColumnIndex(UserContract.Columns.SHARE_QUESTIONS) >= 0)
            result.shareQuestions = (cursor.getInt(
                    cursor.getColumnIndex(UserContract.Columns.SHARE_QUESTIONS)) == 1);
        else
            return null;

        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPatient() {
        return isPatient;
    }

    public void setIsPatient(boolean isPatient) {
        this.isPatient = isPatient;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
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

    public boolean getConfirmedByPatient() {
        return confirmedByPatient;
    }

    public void setConfirmedByPatient(boolean confirmedByPatient) {
        this.confirmedByPatient = confirmedByPatient;
    }

    public boolean getConfirmedByFollower() {
        return confirmedByFollower;
    }

    public void setConfirmedByFriend(boolean confirmedByFriend) {
        this.confirmedByFollower = confirmedByFriend;
    }

    public Drawable getUserpic() {
        return userpic;
    }

    public void setUserpic(Drawable userpic) {
        this.userpic = userpic;
    }

    public boolean getFollowed() {
        return followed;
    }

    public void setFollowed(boolean isFollowed) {
        this.followed = isFollowed;
    }

    public boolean getShareFeeling() {
        return shareFeeling;
    }

    public void setShareFeeling(boolean shareFeeling) {
        this.shareFeeling = shareFeeling;
    }

    public boolean isShareBloodSugar() {
        return shareBloodSugar;
    }

    public void setShareBloodSugar(boolean shareBloodSugar) {
        this.shareBloodSugar = shareBloodSugar;
    }

    public boolean getShareInsulin() {
        return shareInsulin;
    }

    public void setShareInsulin(boolean shareInsulin) {
        this.shareInsulin = shareInsulin;
    }

    public boolean getShareQuestions() {
        return shareQuestions;
    }

    public void setShareQuestions(boolean shareQuestions) {
        this.shareQuestions = shareQuestions;
    }

    public String getFullName() {
        String fullName = "";
        if (this.firstName != null && !this.firstName.equals(""))
            fullName += this.firstName;
        if (this.lastName != null && !this.lastName.equals(""))
            if (!fullName.equals(""))
                fullName += " ";
            fullName += this.lastName;
        if (fullName.equals(""))
            fullName = this.follower;
        return fullName;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(UserContract.Columns.USERNAME, this.follower);
        values.put(UserContract.Columns.IS_PATIENT, this.isPatient ? 1 : 0);
        values.put(UserContract.Columns.USERPIC_FILENAME, this.userpicFilename);
        values.put(UserContract.Columns.BIRTHDAY, this.birthDay);
        values.put(UserContract.Columns.FIRSTNAME, this.firstName);
        values.put(UserContract.Columns.LASTNAME, this.lastName);
        values.put(UserContract.Columns.MEDICAL_RECORD_NUMBER, this.medicalRecordNumber);
        values.put(UserContract.Columns.IS_CONFIRMED_BY_YOU, this.confirmedByPatient ? 1 : 0);
        values.put(UserContract.Columns.IS_CONFIRMED_BY_FRIEND, this.confirmedByFollower ? 1 : 0);
        values.put(UserContract.Columns.IS_FOLLOWED, this.followed ? 1 : 0);
        values.put(UserContract.Columns.SHARE_FEELING, this.shareFeeling ? 1 : 0);
        values.put(UserContract.Columns.SHARE_BLOOD_SUGAR, this.shareBloodSugar ? 1 : 0);
        values.put(UserContract.Columns.SHARE_INSULIN, this.shareInsulin ? 1 : 0);
        values.put(UserContract.Columns.SHARE_QUESTIONS, this.shareQuestions ? 1 : 0);
        return values;
    }

    @Override
    public String toString() {
        return String.format("User{follower=%s; full name = %s}", new String[]{
                this.follower, this.getFullName()
        });
    }

    public static ArrayList<User> getSampleUsersList(Activity activity) {
        ArrayList<User> samplePeopleList = new ArrayList<>();
        samplePeopleList.add(new User(
                true, // isPatient
                "mike", // follower
                "Mike", // firstName
                "Johnson", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //confirmedByPatient
                true, // confirmedByFollower
                false, // getFollowed
                false, // shareFeeling
                false, // shareBloodSugar
                false, // shareInsulin
                false  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                true, // isPatient
                "jane", // follower
                "Jane", // firstName
                "Smith", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //confirmedByPatient
                true, // confirmedByFollower
                true, // getFollowed
                true, // shareFeeling
                true, // shareBloodSugar
                true, // shareInsulin
                true  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                false, // isPatient
                "jules", // follower
                "Jules", // firstName
                "Santori", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //confirmedByPatient
                true, // confirmedByFollower
                false, // getFollowed
                true, // shareFeeling
                true, // shareBloodSugar
                true, // shareInsulin
                true  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                false, // isPatient
                "kate", // follower
                "Kate", // firstName
                "Patel", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //confirmedByPatient
                true, // confirmedByFollower
                true, // getFollowed
                false, // shareFeeling
                true, // shareBloodSugar
                true, // shareInsulin
                true  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                false, // isPatient
                "den", // follower
                "Den", // firstName
                "James", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //confirmedByPatient
                true, // confirmedByFollower
                true, // getFollowed
                true, // shareFeeling
                false, // shareBloodSugar
                true, // shareInsulin
                true  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                false, // isPatient
                "sam", // follower
                "Sam", // firstName
                "Black", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //confirmedByPatient
                true, // confirmedByFollower
                true, // getFollowed
                true, // shareFeeling
                true, // shareBloodSugar
                false, // shareInsulin
                true  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                false, // isPatient
                "terry", // follower
                "Terry", // firstName
                "Brown", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //confirmedByPatient
                true, // confirmedByFollower
                true, // getFollowed
                true, // shareFeeling
                true, // shareBloodSugar
                true, // shareInsulin
                false  // shareQuestionnaire
        ));

        return samplePeopleList;
    }
}
