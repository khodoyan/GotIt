package pro.khodoian.gotit.presenter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.models.Question;
import pro.khodoian.gotit.models.Questionnaire;

/**
 * Adapter class to generate view of Posts in MainActivity
 *
 * @author eduardkhodoyan
 */
public class PostListAdapter extends BaseAdapter {

    private class ViewHolder {
        ImageView userpic;
        TextView timestamp;
        TextView name;
        TextView feeling;
        TextView sugar;
        TextView sugarValue;
        TextView insulin;
        LinearLayout questions;
    }

    ArrayList<Post> posts;
    WeakReference<Activity> activity;

    public PostListAdapter(Activity activity, ArrayList<Post> posts) {
        this.activity = new WeakReference<>(activity);
        this.posts = posts;
    }


    @Override
    public int getCount() {
        if (posts != null)
            return posts.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        if (posts != null && i < getCount() && i >= 0)
            return posts.get(i);
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
        if (position >= getCount() || position < 0 || posts.get(position) == null)
            return null;

        Post post = posts.get(position);
        View resultView = view;
        ViewHolder holder;

        if (view != null)
            holder = (ViewHolder) resultView.getTag();
        else {
            if (activity.get() != null)
                resultView = activity.get().getLayoutInflater()
                        .inflate(R.layout.post, null);
            holder = new ViewHolder();

            // set userpic
            holder.userpic =
                    (ImageView) resultView.findViewById(R.id.postUserpicImageView);
            Drawable userpic = null;
            if (post.getUser() != null){
                userpic = post.getUser().getUserpic();
            }
            if (userpic == null && activity.get() != null)
                userpic = activity.get().getDrawable(R.drawable.ic_user_default);
            if (holder.userpic != null)
                holder.userpic.setImageDrawable(userpic);

            // set timestamp
            holder.timestamp =
                    (TextView) resultView.findViewById(R.id.postTimestampTextView);
            if (post.getTimestamp() != 0)
                holder.timestamp.setText(
                        new SimpleDateFormat("DD MMMM YYYY HH:mm", Locale.getDefault())
                                .format(new Date(post.getTimestamp())));

            // set name
            holder.name =
                    (TextView) resultView.findViewById(R.id.postNameTextView);
            String name = "";
            if (post.getUser() != null && post.getUser().getFirstName() != null)
                name += post.getUser().getFirstName();
            if (post.getUser() != null && post.getUser().getLastName() != null)
                name += post.getUser().getLastName();
            if (name.equals(""))
                name = post.getUsername();
            holder.name.setText(name);

            // set feeling
            holder.feeling =
                    (TextView) resultView.findViewById(R.id.postFeelingTextView);
            Post.Feeling feeling = post.getFeeling();
            if (feeling != null) {
                switch (feeling) {
                    case GOOD:
                        holder.feeling.setText(R.string.post_feeling_good);
                        break;
                    case OKAY:
                        holder.feeling.setText(R.string.post_feeling_okay);
                        break;
                    case BAD:
                        holder.feeling.setText(R.string.post_feeling_bad);
                }
            } else {
                    holder.feeling.setVisibility(View.INVISIBLE);
                }

            // blood sugar
            holder.sugar =
                    (TextView) resultView.findViewById(R.id.postSugarTextView);
            holder.sugarValue =
                    (TextView) resultView.findViewById(R.id.postSugarValueTextView);
            if (post.getBloodSugar() == 0f) {
                holder.sugar.setVisibility(View.INVISIBLE);
                holder.sugar.setHeight(0);
                holder.sugarValue.setVisibility(View.INVISIBLE);
                holder.sugarValue.setHeight(0);
            } else {
                holder.sugarValue.setText(String.valueOf(post.getBloodSugar()));
            }

            // set insulin
            holder.insulin =
                    (TextView) resultView.findViewById(R.id.postInsulinTextView);
            if (!post.isAdministeredInsulin())
                holder.insulin.setVisibility(View.INVISIBLE);
            holder.insulin.setHeight(0);

            // set list of questions
            holder.questions =
                    (LinearLayout) resultView.findViewById(R.id.postQuestionsList);
            if (activity.get() != null && posts.get(position) != null
                    && posts.get(position).getQuestionnaire() != null) {
                Questionnaire questionnaire = posts.get(position).getQuestionnaire();
                QuestionPostListAdapter adapter =
                        new QuestionPostListAdapter(activity.get(), questionnaire);
                for (int i = 0; i < questionnaire.size(); i++) {
                    Question question = questionnaire.get(i);
                    if (question != null)
                        holder.questions.addView(adapter.getView(i, null, holder.questions));
                }
            }

            resultView.setTag(holder);
        }

        return resultView;
    }
}
