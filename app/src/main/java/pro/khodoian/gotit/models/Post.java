package pro.khodoian.gotit.models;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import pro.khodoian.gotit.sql.PostContract;

/**
 * Class for one post with extra questions list
 *
 * @author eduardkhodoyan
 */
public class Post implements ToContentValues {

    public enum Feeling {
        BAD, OKAY, GOOD
    }

    private long id;
    private long serverId;
    private String username;
    private long updatedAt;
    private long deletedAt;
    private long timestamp;
    private boolean isShared;
    private Feeling feeling;
    private float bloodSugar;
    private boolean administeredInsulin;
    private Questionnaire questionnaire;

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(PostContract.Columns.SERVER_ID, serverId);
        values.put(PostContract.Columns.USERNAME, username);
        values.put(PostContract.Columns.UPDATED_AT, updatedAt);
        values.put(PostContract.Columns.DELETED_AT, deletedAt);
        values.put(PostContract.Columns.TIMESTAMP, timestamp);
        values.put(PostContract.Columns.IS_SHARED, (isShared) ? 1 : 0);
        values.put(PostContract.Columns.FEELING, feeling.toString());
        values.put(PostContract.Columns.BLOOD_SUGAR, bloodSugar);
        values.put(PostContract.Columns.ADMINISTERED_INSULIN, (administeredInsulin) ? 1 : 0);
        values.put(PostContract.Columns.QUESTIONNAIRE, questionnaire.toJson());
        return values;
    }

    public Post(ContentValues values) {

        serverId = values.getAsLong(PostContract.Columns.SERVER_ID);
        username = values.getAsString(PostContract.Columns.USERNAME);
        updatedAt = values.getAsLong(PostContract.Columns.UPDATED_AT);
        deletedAt = values.getAsLong(PostContract.Columns.DELETED_AT);
        timestamp = values.getAsLong(PostContract.Columns.TIMESTAMP);
        isShared = (values.getAsInteger(PostContract.Columns.IS_SHARED) == 1);
        switch (values.getAsString(PostContract.Columns.FEELING)) {
            case "BAD":
                feeling = Feeling.BAD;
                break;
            case "OKAY":
                feeling = Feeling.OKAY;
                break;
            case "GOOD":
                feeling = Feeling.GOOD;
        }
        bloodSugar = values.getAsFloat(PostContract.Columns.BLOOD_SUGAR);
        administeredInsulin = (values.getAsInteger(PostContract.Columns.ADMINISTERED_INSULIN) == 1);
        questionnaire = new Gson().fromJson(values.getAsString(PostContract.Columns.QUESTIONNAIRE),
                new TypeToken<Questionnaire>(){}.getType());
    }

    public Post() {}

    public Post(long id, long serverId, String username, long updatedAt, long deletedAt,
                long timestamp, boolean isShared, Feeling feeling, float bloodSugar,
                boolean administeredInsulin, Questionnaire questionnaire) {
        this.id = id;
        this.serverId = serverId;
        this.username = username;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.timestamp = timestamp;
        this.isShared = isShared;
        this.feeling = feeling;
        this.bloodSugar = bloodSugar;
        this.administeredInsulin = administeredInsulin;
        this.questionnaire = questionnaire;
    }

    public Post(long id, long serverId, String username, long updatedAt, long deletedAt,
                long timestamp, int isShared, Feeling feeling, float bloodSugar,
                int administeredInsulin, String questionnaireJsonString) {
        this.id = id;
        this.serverId = serverId;
        this.username = username;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.timestamp = timestamp;
        this.isShared = (isShared == 1);
        this.feeling = feeling;
        this.bloodSugar = bloodSugar;
        this.administeredInsulin = (administeredInsulin == 1);
        this.questionnaire = new Questionnaire(questionnaireJsonString);
    }

    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(long deletedAt) {
        this.deletedAt = deletedAt;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setIsShared(boolean isShared) {
        this.isShared = isShared;
    }

    public Feeling getFeeling() {
        return feeling;
    }

    public void setFeeling(Feeling feeling) {
        this.feeling = feeling;
    }

    public float getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(float bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public boolean isAdministeredInsulin() {
        return administeredInsulin;
    }

    public void setAdministeredInsulin(boolean administeredInsulin) {
        this.administeredInsulin = administeredInsulin;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
