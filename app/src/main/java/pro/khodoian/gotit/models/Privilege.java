package pro.khodoian.gotit.models;

/**
 * Simple POJO class to manage followers/following and privileges of such connection
 *
 * @author eduardkhodoyan
 */
public class Privilege {
    private String patientUsername;
    private String followerusername;
    private boolean isConfirmedByPatient;
    private boolean isConfirmedByFollower;
    private boolean hasPrivilegeSugar;
    private boolean hasPrivilegeInsulin;
    private boolean hasPrivilegeQuestionnaire;

    public String getPatientUsername() {
        return patientUsername;
    }

    public void setPatientUsername(String patientUsername) {
        this.patientUsername = patientUsername;
    }

    public String getFollowerusername() {
        return followerusername;
    }

    public void setFollowerusername(String followerusername) {
        this.followerusername = followerusername;
    }

    public boolean isConfirmedByPatient() {
        return isConfirmedByPatient;
    }

    public void setIsConfirmedByPatient(boolean isConfirmedByPatient) {
        this.isConfirmedByPatient = isConfirmedByPatient;
    }

    public boolean isConfirmedByFollower() {
        return isConfirmedByFollower;
    }

    public void setIsConfirmedByFollower(boolean isConfirmedByFollower) {
        this.isConfirmedByFollower = isConfirmedByFollower;
    }

    public boolean isHasPrivilegeSugar() {
        return hasPrivilegeSugar;
    }

    public void setHasPrivilegeSugar(boolean hasPrivilegeSugar) {
        this.hasPrivilegeSugar = hasPrivilegeSugar;
    }

    public boolean isHasPrivilegeInsulin() {
        return hasPrivilegeInsulin;
    }

    public void setHasPrivilegeInsulin(boolean hasPrivilegeInsulin) {
        this.hasPrivilegeInsulin = hasPrivilegeInsulin;
    }

    public boolean isHasPrivilegeQuestionnaire() {
        return hasPrivilegeQuestionnaire;
    }

    public void setHasPrivilegeQuestionnaire(boolean hasPrivilegeQuestionnaire) {
        this.hasPrivilegeQuestionnaire = hasPrivilegeQuestionnaire;
    }
}
