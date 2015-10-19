package pro.khodoian.gotit.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import pro.khodoian.gotit.AsyncTasks.AddPostAsyncTask;
import pro.khodoian.gotit.R;
import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.models.Question;
import pro.khodoian.gotit.models.Questionnaire;
import pro.khodoian.gotit.presenter.QuestionInputListAdapter;

public class CheckinActivity extends AppCompatActivity implements AddPostAsyncTask.Callback {

    // TODO: implement fragment to restore values

    public static final int REQUEST_CODE = 1001;
    public static final int SUCCESSFUL = 1002;
    public static final int FAILED = 1003;
    public static final String KEY_POST_ID = "post_id";

    private boolean isBackPressed = false;

    private FloatingActionButton fab;
    LinearLayout listLinearLayout;
    EditText bloodSugarEditText;
    Switch administeredInsulinSwitch;
    RadioButton goodRadio;
    RadioButton okayRadio;
    RadioButton badRadio;
    Switch isSharedSwitch;

    CoordinatorLayout parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        // Initialize widgets and layouts
        parentLayout = (CoordinatorLayout) findViewById(R.id.checkInParentLayout);
        listLinearLayout = (LinearLayout) findViewById(R.id.checkInListLinearLayout);
        fab = (FloatingActionButton) findViewById(R.id.fabCheckIn);
        bloodSugarEditText = (EditText) findViewById(R.id.bloodSugarEditText);
        administeredInsulinSwitch = (Switch) findViewById(R.id.checkInAdministeredInsulinSwitch);
        goodRadio = (RadioButton) findViewById(R.id.checkInGoodRadio);
        okayRadio = (RadioButton) findViewById(R.id.checkInOkayRadio);
        badRadio = (RadioButton) findViewById(R.id.checkInBadRadio);
        isSharedSwitch = (Switch) findViewById(R.id.checkInIsSharedSwitch);

        final QuestionInputListAdapter adapter =
                new QuestionInputListAdapter(
                        this, // activity
                        new Questionnaire(Questionnaire.FillType.DEFAULT_WITH_USER_DEFINED),
                        true // hasHeader
                );
        for (int i = 0; i < adapter.getCount(); i++) {
            listLinearLayout.addView(adapter.getView(i, null, listLinearLayout));
        }

        if (fab != null) {

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFABClick();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBackPressed = false;
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CheckinActivity.class);
    }

    private void onFABClick() {
        this.fab.hide();
        Post newPost = getPost();
        if (newPost != null && !newPost.isBlank()) {
            new AddPostAsyncTask(this, this).execute(newPost);
        } else {
            if (parentLayout != null)
                Snackbar.make(parentLayout, R.string.check_in_no_data, Snackbar.LENGTH_LONG)
                .setAction(R.string.got_it, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // No action is required
                    }
                })
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        if (!fab.isShown())
                             fab.show();
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                        if (fab.isShown())
                            fab.hide();
                    }
                }).show();
        }
    }

    private Post getPost() {
        Post result = new Post();
        result.setBloodSugar(this.getBloodSugar());
        result.setAdministeredInsulin(this.administeredInsulin());
        result.setFeeling(getFeeling());
        result.setIsShared(isShared());
        result.setQuestionnaire(this.getQuestionnaire());
        // TODO: make user not default
        result.setUsername("user1");
        if (result.isBlank())
            return null;
        return result;
    }

    private Questionnaire getQuestionnaire() {
        Questionnaire result = new Questionnaire(Questionnaire.FillType.BLANK);
        for (int i = 0; i < listLinearLayout.getChildCount(); i++) {
            LinearLayout questionLayout = (LinearLayout) listLinearLayout.getChildAt(i);
            if (questionLayout != null) {
                TextView questionTextView =
                        (TextView) questionLayout.findViewById(R.id.checkInQuestionTextView);
                String questionString = null;
                if (questionTextView != null)
                    questionString = questionTextView.getText().toString();
                EditText answerEditText =
                        (EditText) questionLayout.findViewById(R.id.checkInAnswerEditText);
                String answerString = null;
                if (answerEditText != null)
                    answerString = answerEditText.getText().toString();
                if (answerEditText != null && !answerString.equals("") &&
                        questionString != null && !questionString.equals(""))
                    result.add(new Question(questionString, answerString));
            }
        }
        return result;
    }

    private float getBloodSugar() {
        float result = 0f;
        if (bloodSugarEditText != null && bloodSugarEditText.getText() != null &&
                !bloodSugarEditText.getText().toString().equals(""))
            result = Float.valueOf(bloodSugarEditText.getText().toString());
        return result;
    }

    private boolean administeredInsulin() {
        boolean result = false;
        if (administeredInsulinSwitch != null) {
            result = administeredInsulinSwitch.isChecked();
        }
        return result;
    }

    private Post.Feeling getFeeling() {
        if (goodRadio != null && okayRadio != null && badRadio != null) {
            if (goodRadio.isChecked())
                return Post.Feeling.GOOD;
            if (okayRadio.isChecked())
                return Post.Feeling.OKAY;
            if (badRadio.isChecked())
                return Post.Feeling.BAD;
        }
        return Post.Feeling.OKAY;
    }

    private boolean isShared() {
        if (isSharedSwitch != null)
            return isSharedSwitch.isChecked();
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBackPressed = true;
    }

    @Override
    public void onPostAddedSuccess(Long id) {
        if (id != null) {
            Intent result = new Intent();
            result.putExtra(KEY_POST_ID, id);
            this.setResult(SUCCESSFUL, result);
            this.finish();
        } else {
            onPostAddedFailed();
        }
    }

    @Override
    public void onPostAddedFailed() {
        if (!isBackPressed)
            Toast.makeText(this, R.string.cant_add_post, Toast.LENGTH_LONG).show();
        isBackPressed = false;
        setResult(FAILED);
    }
}
