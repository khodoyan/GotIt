package pro.khodoian.gotit.models;

import android.content.ContentValues;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.HashMap;

import pro.khodoian.gotit.sql.QuestionContract;

/**
 *
 * Created by eduardkhodoyan on 10/12/15.
 */
public class Question {

    protected String question;
    protected String answer;

    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public Question(String question) {
        this.question = question;
    }

    public Question(String[] strings) {
        if (strings.length > 1) {
            this.question = strings[0];
            this.answer = strings[1];
        }
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String toJson() {
        return new Gson().toJson(new String[]{question, answer});
    }

    public ContentValues toContentValues() {
        ContentValues result = new ContentValues();
        result.put(QuestionContract.Columns.QUESTION, this.question);
        return result;
    }
}
