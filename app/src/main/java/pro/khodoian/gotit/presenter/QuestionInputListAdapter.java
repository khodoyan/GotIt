package pro.khodoian.gotit.presenter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.models.Question;
import pro.khodoian.gotit.models.Questionnaire;

/**
 * Adapter class to generate view of list of questions in CheckinActivity
 *
 * @author eduardkhodoyan
 */
public class QuestionInputListAdapter extends BaseAdapter {
    private Questionnaire questionnaire;
    private WeakReference<Activity> activity;
    private boolean hasHeader;

    public QuestionInputListAdapter(Activity activity, boolean hasHeader) {
        // TODO: implement data retain fragment
        questionnaire = new Questionnaire(Questionnaire.FillType.DEFAULT_WITH_USER_DEFINED);
        this.activity = new WeakReference<>(activity);
        this.hasHeader = hasHeader;
    }

    public QuestionInputListAdapter(Activity activity, Questionnaire questionnaire,
                                    boolean hasHeader) {
        if (questionnaire != null)
            this.questionnaire = questionnaire;
        else
            this.questionnaire =
                    new Questionnaire(Questionnaire.FillType.DEFAULT_WITH_USER_DEFINED);
        this.activity = new WeakReference<>(activity);
        this.hasHeader = hasHeader;
    }

    @Override
    public int getCount() {
        return questionnaire.size();
    }

    @Override
    public Object getItem(int i) {
        // TODO: Разобраться, почему после добавления header'а при обращении к 0 массиву, приложение выкидывает NullPointerException
        if (i >= getCount() || i < 0) {
            return null;
        } else {
            return questionnaire.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static class ViewHolder {
        TextView questionTextView;
        EditText answerEditText;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        // Check data validity
        if (position >= getCount() || position < 0)
            return null;

        View resultView = view;
        ViewHolder holder;

        if (view != null)
            holder = (ViewHolder) resultView.getTag();
        else {
            if (activity.get() != null)
                resultView = activity.get().getLayoutInflater()
                        .inflate(R.layout.check_in_question, null);
            holder = new ViewHolder();
            holder.questionTextView =
                    (TextView) resultView.findViewById(R.id.checkInQuestionTextView);
            holder.answerEditText =
                    (EditText) resultView.findViewById(R.id.checkInAnswerEditText);
            resultView.setTag(holder);
        }

        if (questionnaire.size() <= 0) {
            holder.questionTextView.setText(R.string.check_in_there_is_no_questions);
            holder.answerEditText.setVisibility(View.INVISIBLE);
        } else {

            Question tempQuestion = questionnaire.get(position);
            holder.questionTextView.setText(tempQuestion.getQuestion());
            holder.answerEditText.setText(tempQuestion.getAnswer());
        }
        return resultView;
    }

    public int add(Question question) {
        if (questionnaire.add(question))
            return questionnaire.size() - 1;
        else
            return -1;
    }
}
