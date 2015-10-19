package pro.khodoian.gotit.presenter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.models.Question;
import pro.khodoian.gotit.models.Questionnaire;

/**
 * Adapter class to generate view of list of questions in Post - MainActivity
 *
 * @author eduardkhodoyan
 */
public class QuestionPostListAdapter extends BaseAdapter {

    public class ViewHolder {
        private TextView question;
        private TextView answer;
    }

    Questionnaire questionnaire;
    WeakReference<Activity> activity;

    public QuestionPostListAdapter(Activity activity, Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public int getCount() {
        if (questionnaire != null)
            return questionnaire.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        if (questionnaire != null && i < getCount() && i >= 0)
            return questionnaire.get(i);
        else
            return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
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
                        .inflate(R.layout.post_question, null);
            holder = new ViewHolder();
            holder.question =
                    (TextView) resultView.findViewById(R.id.postQuestionTextView);
            holder.answer =
                    (TextView) resultView.findViewById(R.id.postAnswerTextView);
            resultView.setTag(holder);
        }

        if (questionnaire.size() <= 0) {
            holder.question.setText(R.string.post_no_questions);
            holder.answer.setVisibility(View.INVISIBLE);
        } else {
            Question tempQuestion = questionnaire.get(position);
            holder.question.setText(tempQuestion.getQuestion());
            holder.answer.setText(tempQuestion.getAnswer());
        }
        return resultView;
    }
}
