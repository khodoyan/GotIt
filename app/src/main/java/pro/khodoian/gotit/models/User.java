package pro.khodoian.gotit.models;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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
    private boolean isPatient;
    private String username;
    private String firstName;
    private String lastName;
    private long birthDay;
    private String medicalRecordNumber;
    private String userpicFilename;
    private Drawable userpic;
    private boolean isConfirmedByYou;
    private boolean isConfirmedByFriend;
    private boolean isFollowed;
    private boolean shareFeeling;
    private boolean shareBloodSugar;
    private boolean shareInsulin;
    private boolean shareQuestions;


    public User(boolean isPatient, String username, String firstName, String lastName,
                long birthDay, String medicalRecordNumber, String userpicFilename,
                boolean isConfirmedByYou, boolean isConfirmedByFriend) {
        this.isPatient = isPatient;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.medicalRecordNumber = medicalRecordNumber;
        this.userpicFilename = userpicFilename;
        this.isConfirmedByYou = isConfirmedByYou;
        this.isConfirmedByFriend = isConfirmedByFriend;
    }

    public User(boolean isPatient,
                String username,
                String firstName,
                String lastName,
                long birthDay,
                String medicalRecordNumber,
                String userpicFilename,
                Drawable userpic,
                boolean isConfirmedByYou,
                boolean isConfirmedByFriend,
                boolean isFollowed,
                boolean shareFeeling,
                boolean shareBloodSugar,
                boolean shareInsulin,
                boolean shareQuestions) {
        this.isPatient = isPatient;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.medicalRecordNumber = medicalRecordNumber;
        this.userpicFilename = userpicFilename;
        this.userpic = userpic;
        this.isConfirmedByYou = isConfirmedByYou;
        this.isConfirmedByFriend = isConfirmedByFriend;
        this.isFollowed = isFollowed;
        this.shareFeeling = shareFeeling;
        this.shareBloodSugar = shareBloodSugar;
        this.shareInsulin = shareInsulin;
        this.shareQuestions = shareQuestions;
    }

    public User makeUser() {
        return null;
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

    public boolean isConfirmedByYou() {
        return isConfirmedByYou;
    }

    public void setIsConfirmedByYou(boolean isConfirmedByYou) {
        this.isConfirmedByYou = isConfirmedByYou;
    }

    public boolean isConfirmedByFriend() {
        return isConfirmedByFriend;
    }

    public void setIsConfirmedByFriend(boolean isConfirmedByFriend) {
        this.isConfirmedByFriend = isConfirmedByFriend;
    }

    public Drawable getUserpic() {
        return userpic;
    }

    public void setUserpic(Drawable userpic) {
        this.userpic = userpic;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public boolean isShareFeeling() {
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

    public boolean isShareInsulin() {
        return shareInsulin;
    }

    public void setShareInsulin(boolean shareInsulin) {
        this.shareInsulin = shareInsulin;
    }

    public boolean isShareQuestions() {
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
            fullName = this.username;
        return fullName;
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
        values.put(UserContract.Columns.IS_CONFIRMED_BY_YOU, this.isConfirmedByYou ? 1 : 0);
        values.put(UserContract.Columns.IS_CONFIRMED_BY_FRIEND, this.isConfirmedByFriend ? 1 : 0);
        values.put(UserContract.Columns.IS_FOLLOWED, this.isFollowed ? 1 : 0);
        values.put(UserContract.Columns.SHARE_FEELING, this.shareFeeling ? 1 : 0);
        values.put(UserContract.Columns.SHARE_BLOOD_SUGAR, this.shareBloodSugar ? 1 : 0);
        values.put(UserContract.Columns.SHARE_INSULIN, this.shareInsulin ? 1 : 0);
        values.put(UserContract.Columns.SHARE_QUESTIONS, this.shareQuestions ? 1 : 0);
        return values;
    }

    public static ArrayList<User> getSampleUsersList(Activity activity) {
        ArrayList<User> samplePeopleList = new ArrayList<>();
        samplePeopleList.add(new User(
                true, // isPatient
                "mike", // username
                "Mike", // firstName
                "Johnson", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //isConfirmedByYou
                true, // isConfirmedByFriend
                false, // isFollowed
                false, // shareFeeling
                false, // shareBloodSugar
                false, // shareInsulin
                false  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                true, // isPatient
                "jane", // username
                "Jane", // firstName
                "Smith", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //isConfirmedByYou
                true, // isConfirmedByFriend
                true, // isFollowed
                true, // shareFeeling
                true, // shareBloodSugar
                true, // shareInsulin
                true  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                false, // isPatient
                "jules", // username
                "Jules", // firstName
                "Santori", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //isConfirmedByYou
                true, // isConfirmedByFriend
                false, // isFollowed
                true, // shareFeeling
                true, // shareBloodSugar
                true, // shareInsulin
                true  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                false, // isPatient
                "kate", // username
                "Kate", // firstName
                "Patel", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //isConfirmedByYou
                true, // isConfirmedByFriend
                true, // isFollowed
                false, // shareFeeling
                true, // shareBloodSugar
                true, // shareInsulin
                true  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                false, // isPatient
                "den", // username
                "Den", // firstName
                "James", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //isConfirmedByYou
                true, // isConfirmedByFriend
                true, // isFollowed
                true, // shareFeeling
                false, // shareBloodSugar
                true, // shareInsulin
                true  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                false, // isPatient
                "sam", // username
                "Sam", // firstName
                "Black", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //isConfirmedByYou
                true, // isConfirmedByFriend
                true, // isFollowed
                true, // shareFeeling
                true, // shareBloodSugar
                false, // shareInsulin
                true  // shareQuestionnaire
        ));

        samplePeopleList.add(new User(
                false, // isPatient
                "terry", // username
                "Terry", // firstName
                "Brown", // lastName
                new GregorianCalendar().getTimeInMillis(), // birthday
                "", // medicalRecordsNumber
                "", // userpicFilename
                (activity != null)
                        ? activity.getDrawable(R.drawable.ic_user_default)
                        : null,
                true, //isConfirmedByYou
                true, // isConfirmedByFriend
                true, // isFollowed
                true, // shareFeeling
                true, // shareBloodSugar
                true, // shareInsulin
                false  // shareQuestionnaire
        ));

        return samplePeopleList;
    }
}
