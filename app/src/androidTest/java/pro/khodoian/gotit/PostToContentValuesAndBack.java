package pro.khodoian.gotit;

import android.content.ContentValues;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;

import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.models.Question;
import pro.khodoian.gotit.models.Questionnaire;

/**
 * Test case to check converting questionnaire into JSON and back
 * class: pro.khodoian.gotit.models.Post
 *
 * @author eduardkhodoyan
 */
public class PostToContentValuesAndBack extends TestCase {

    Post samplePost;
    Questionnaire sampleQuestionnaire;

    @Before
    public void setUp() throws Exception {
        sampleQuestionnaire = new Questionnaire();
        sampleQuestionnaire.add(new Question("Favourite color", "Red"));
        sampleQuestionnaire.add(new Question("Really?", "Yes"));

        samplePost = new Post(
                1, // id
                -1, // serverId
                "User1", // username
                0, // updatedAt
                0, // deletedAt
                new GregorianCalendar().getTimeInMillis(), // timestamp
                true, // isShared
                Post.Feeling.GOOD, // feeling
                1.0f, // bloodSugar
                false, // administeredInsulin
                sampleQuestionnaire // questionnaire
        );
    }

    @Test
    public void testPostToCvsAndBack() throws Exception{
        ContentValues values = samplePost.toContentValues();
        Post post = new Post(values);
        assertEquals("User1", post.getUsername());
        assertEquals(0, post.getUpdatedAt());
        assertEquals(0, post.getDeletedAt());
        assertTrue(post.getTimestamp() > 0);
        assertEquals(true, post.isShared());
        assertEquals(Post.Feeling.GOOD, post.getFeeling());
        assertEquals(1.0f, post.getBloodSugar());
        assertEquals(false, post.isAdministeredInsulin());

        assertEquals("Red", post.getQuestionnaire().get(0).getAnswer());
        assertEquals("Favourite color", post.getQuestionnaire().get(0).getQuestion());
        assertEquals("Yes", post.getQuestionnaire().get("Really?").getAnswer());
        assertEquals("Really?", post.getQuestionnaire().get("Really?").getQuestion());
    }
}
