package pro.khodoian.gotit.models;

/**
 * POJO for storing posts on server
 *
 * @author eduardkhodoyan
 */
public class PostClient {
    private long id;
    private String username;
    private long updatedAt;
    private long deletedAt;
    private long timestamp;
    private boolean isShared;
    private Post.Feeling feeling;
    private float bloodSugar;
    private boolean administeredInsulin;
    private String questionnaire;

    protected PostClient() {
    }

    public PostClient makePost(Post post) {
        if(post == null)
            return null;
        PostClient result = new PostClient();
        result.id = post.getServerId();
        result.username = post.getUsername();
        result.updatedAt = post.getUpdatedAt();
        result.deletedAt = post.getDeletedAt();
        result.timestamp = post.getTimestamp();
        result.isShared = post.isShared();
        result.feeling = post.getFeeling();
        result.bloodSugar = post.getBloodSugar();
        result.administeredInsulin = post.isAdministeredInsulin();
        result.questionnaire = post.getQuestionnaire().toJson();
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean getIsShared() {
        return isShared;
    }

    public void setIsShared(boolean isShared) {
        this.isShared = isShared;
    }

    public Post.Feeling getFeeling() {
        return feeling;
    }

    public void setFeeling(Post.Feeling feeling) {
        this.feeling = feeling;
    }

    public float getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(float bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public boolean getAdministeredInsulin() {
        return administeredInsulin;
    }

    public void setAdministeredInsulin(boolean administeredInsulin) {
        this.administeredInsulin = administeredInsulin;
    }

    public String getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(String questionnaire) {
        this.questionnaire = questionnaire;
    }
}
