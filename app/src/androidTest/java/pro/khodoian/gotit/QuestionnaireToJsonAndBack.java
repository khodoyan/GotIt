package pro.khodoian.gotit;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.models.Question;
import pro.khodoian.gotit.models.Questionnaire;

/**
 * Test case to check converting questionnaire into JSON and back
 * class: pro.khodoian.gotit.models.Questionnaire
 *
 * @author eduardkhodoyan
 */
public class QuestionnaireToJsonAndBack extends TestCase {

    Post samplePost;
    Questionnaire sampleQuestionnaire;

    @Before
    public void setUp() throws Exception {
        sampleQuestionnaire = new Questionnaire();
        sampleQuestionnaire.add(new Question("Favourite color", "Red"));
        sampleQuestionnaire.add(new Question("Really?", "Yes"));
    }

    @Test
    public void testQuestionnaireToJsonAndBack() throws Exception{
        String json = this.sampleQuestionnaire.toJson();
        Questionnaire questionnaire =
                new Gson().fromJson(json, new TypeToken<Questionnaire>(){}.getType());
        assertEquals("Red", questionnaire.get(0).getAnswer());
        assertEquals("Favourite color", questionnaire.get(0).getQuestion());
        assertEquals("Yes", questionnaire.get("Really?").getAnswer());
        assertEquals("Really?", questionnaire.get("Really?").getQuestion());
    }
}
