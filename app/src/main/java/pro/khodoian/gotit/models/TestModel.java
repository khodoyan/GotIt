package pro.khodoian.gotit.models;

/**
 * Simple POJO class for testing purposes
 */
public class TestModel {
    private String result;

    public TestModel() {
        result = "Ok";
    }

    public TestModel(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
