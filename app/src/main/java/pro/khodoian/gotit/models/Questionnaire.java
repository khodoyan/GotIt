package pro.khodoian.gotit.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for handling a list of questions by number of question or by it's name
 *
 * @author eduardkhodoyan
 */
public class Questionnaire {
    public static final Question[] DEFAULT_QUESTIONS = {
            new Question("Physical activity")
            // TODO: Make up list of default questions
    };

    public enum FillType {
        BLANK, DEFAULT, DEFAULT_WITH_USER_DEFINED
    }

    private ArrayList<Question> arrayList;
    private HashMap<String, Integer> hashMap;

    public Questionnaire() {
        // Blank constructor
        arrayList = new ArrayList<>();
        hashMap = new HashMap<>();
    }

    public Questionnaire(String jsonString) {
        Gson gson = new Gson();
        arrayList = (ArrayList<Question>) gson.fromJson(jsonString, new TypeToken<ArrayList<Question>>(){}.getType());
        hashMap = new HashMap<>();
        if(arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                hashMap.put(arrayList.get(i).getQuestion(), i);
            }
        }
    }

    public Questionnaire(FillType type) {
        // Initializing blank questionnaire
        arrayList = new ArrayList<>();
        hashMap = new HashMap<>();

        // Filling questionnaire with default values
        if (type == FillType.DEFAULT || type == FillType.DEFAULT_WITH_USER_DEFINED) {
            for (Question question : DEFAULT_QUESTIONS) {
                add(question);
            }
        }

        if (type == FillType.DEFAULT_WITH_USER_DEFINED) {
            // TODO: implement user default values
        }
    }

    /**
     * Adds question to ArrayList and HashMap to the questionnaire
     *
     * @param question to be added to questionnaire
     * @return true if successful, false if not
     */
    public boolean add(Question question) {
        // Input data validity check
        if (question == null || question.getQuestion() == null)
            return false;

        // Check if question is already in the hash map. If so, exits and returns false
        if (this.hashMap == null || this.hashMap.get(question.getQuestion()) != null)
            return false;

        // Everything is okay, add question to arrayList and hashMap
        if (arrayList == null || !arrayList.add(question))
            return false;
        int arrayKey = arrayList.size() - 1; // key number of the added question
        hashMap.put(question.getQuestion(), arrayKey);
        return true;
    }

    /**
     * Returns question from questionnaire by String key
     *
     * @param key - String key to HashMap containing questions
     * @return question from the questionnaire or null if key is invalid
     */
    public Question get (String key) {
        if(hashMap != null && arrayList != null) {
            Integer arrayKey = hashMap.get(key);
            if (arrayKey != null && arrayKey < arrayList.size())
                return arrayList.get(arrayKey);
        }
        return null;
    }

    /**
     * Returns question from questionnaire by int key
     *
     * @param key - int key to ArrayList containing questions
     * @return question from the questionnaire or null if key is invalid
     */
    public Question get (int key) {
        if (arrayList != null && key < arrayList.size())
            return arrayList.get(key);
        else
            return null;
    }

    /**
     * Removes question from questionnaire by int key
     *
     * @param key - int key to ArrayList containing questions
     * @return true if successful, false if not
     */
    public boolean remove (int key) {
        if (arrayList != null && hashMap != null &&
                key < arrayList.size() && arrayList.get(key) != null
                && hashMap.get(arrayList.get(key).getQuestion()) != null) {

            // update every record after the key number in the hashMap
            for (int i = key + 1; i < arrayList.size(); i++) {
                String hashMapKey = arrayList.get(i).getQuestion();
                hashMap.put(
                        hashMapKey,
                        hashMap.get(hashMapKey) - 1
                );
            }
            // remove records
            hashMap.remove(arrayList.get(key).getQuestion());
            arrayList.remove(key);
            return true;
        }
        return false;
    }

    /**
     * Removes question from questionnaire by String key
     *
     * @param key - int key to HashMap containing questions
     * @return true if successful, false if not
     */
    public boolean remove (String key) {
        if (arrayList != null && hashMap != null &&
                hashMap.get(key) != null && arrayList.get(hashMap.get(key)) != null) {
            arrayList.remove(hashMap.get(key));
            hashMap.remove(key);
            return true;
        }
        return false;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
