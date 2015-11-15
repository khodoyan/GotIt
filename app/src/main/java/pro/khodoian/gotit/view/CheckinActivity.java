package pro.khodoian.gotit.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.IBinder;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import pro.khodoian.gotit.broadcastreceivers.RegularNotificationReceiver;
import pro.khodoian.gotit.client.AuthenticationDetailsManager;
import pro.khodoian.gotit.services.CheckinService;
import pro.khodoian.gotit.sqlasynctasks.AddUnsentPostAsyncTask;
import pro.khodoian.gotit.R;
import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.models.Question;
import pro.khodoian.gotit.models.Questionnaire;
import pro.khodoian.gotit.presenter.QuestionInputListAdapter;
import pro.khodoian.gotit.sql.QuestionContract;
import pro.khodoian.gotit.sql.QuestionSqlOperations;
import pro.khodoian.gotit.sql.SqlOperations;

public class CheckinActivity extends AppCompatActivity implements AddUnsentPostAsyncTask.Callback {

    // TODO: implement fragment to restore values

    public static final int REQUEST_CODE = 1001;
    public static final int REQUEST_FROM_NOTIFICATION = 1002;
    public static final int SUCCESSFUL = 1003;
    public static final int FAILED = 1004;
    public static final String KEY_POST_ID = "post_id";

    private boolean isBackPressed = false;

    LinearLayout listLinearLayout;
    EditText bloodSugarEditText;
    Switch administeredInsulinSwitch;
    RadioButton goodRadio;
    RadioButton okayRadio;
    RadioButton badRadio;
    Switch isSharedSwitch;
    CheckBox addDefaultQuestionCheckBox;

    CoordinatorLayout parentLayout;
    QuestionInputListAdapter adapter;

    CheckinService checkinService;

    private ServiceConnection checkinServiceConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Because we have bound to an explicit
            // service that is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            CheckinService.LocalBinder binder = (CheckinService.LocalBinder) service;
            checkinService = binder.getService();
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            checkinService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        // Turn on Back button on in action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        // Initialize widgets and layouts
        parentLayout = (CoordinatorLayout) findViewById(R.id.checkInParentLayout);
        listLinearLayout = (LinearLayout) findViewById(R.id.checkInListLinearLayout);
        bloodSugarEditText = (EditText) findViewById(R.id.bloodSugarEditText);
        administeredInsulinSwitch = (Switch) findViewById(R.id.checkInAdministeredInsulinSwitch);
        goodRadio = (RadioButton) findViewById(R.id.checkInGoodRadio);
        okayRadio = (RadioButton) findViewById(R.id.checkInOkayRadio);
        badRadio = (RadioButton) findViewById(R.id.checkInBadRadio);
        isSharedSwitch = (Switch) findViewById(R.id.checkInIsSharedSwitch);
        final EditText newQuestionEditText = (EditText) findViewById(R.id.checkInNewQuestionEditText);
        final EditText newAnswerEditText = (EditText) findViewById(R.id.checkInNewAnswerEditText);
        addDefaultQuestionCheckBox = (CheckBox) findViewById(R.id.checkInAddDefaultCheckBox);
        final Button addQuestionButton = (Button) findViewById(R.id.checkInAddQuestionButton);

        // Add action listeners
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newQuestionEditText != null && newQuestionEditText.getText() != null
                        && !newQuestionEditText.getText().toString().equals("")
                        && newAnswerEditText != null && newAnswerEditText.getText() != null) {
                    addQuestion(new Question(newQuestionEditText.getText().toString(),
                            newAnswerEditText.getText().toString()));
                    newQuestionEditText.setText("");
                    newAnswerEditText.setText("");
                }
            }
        });

        // Get list of default questions
        // TODO: consider doing it in the background
        Questionnaire defaultQuestionnaire = new Questionnaire(Questionnaire.FillType.DEFAULT);
        Cursor cursor = new QuestionSqlOperations(this)
                .queryAll(null, QuestionContract.Columns.ID, SqlOperations.SortOrder.ASC);
        while (cursor.moveToNext()) {
            defaultQuestionnaire.add(new Question(cursor.getString(
                    cursor.getColumnIndex(QuestionContract.Columns.QUESTION))));
        }

        // Create adapter and fill list of questions
        adapter =
                new QuestionInputListAdapter(
                        this, // activity
                        defaultQuestionnaire,
                        true // hasHeader
                );
        for (int i = 0; i < adapter.getCount(); i++) {
            listLinearLayout.addView(adapter.getView(i, null, listLinearLayout));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBackPressed = false;

        // bind to CheckinService
        bindService(CheckinService.makeIntent(this), checkinServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(checkinServiceConnection);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Menu is not inflated as it is replaced with the drawer
        getMenuInflater().inflate(R.menu.checkin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_send) {
            Post newPost = CheckinActivity.this.getPost();
            if (newPost != null && !newPost.isBlank()) {
                new AddUnsentPostAsyncTask(this, this).execute(newPost);
            } else {
                if (parentLayout != null)
                    Snackbar.make(parentLayout,
                            R.string.check_in_no_data,
                            Snackbar.LENGTH_LONG)
                            .show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, CheckinActivity.class);
    }

    private Post getPost() {
        Post result = new Post();
        result.setBloodSugar(this.getBloodSugar());
        result.setAdministeredInsulin(this.administeredInsulin());
        result.setFeeling(getFeeling());
        result.setIsShared(isShared());
        result.setQuestionnaire(this.getQuestionnaire());
        result.setUsername(new AuthenticationDetailsManager(CheckinActivity.this).getUsername());
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
        return null;
    }

    private boolean isShared() {
        return isSharedSwitch != null
                && isSharedSwitch.isChecked();
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
            // send request to server if called by notification
            if (getIntent().getStringExtra(RegularNotificationReceiver.REQUESTED_BY) ==
                    RegularNotificationReceiver.REQUESTED_BY_KEY && checkinService != null) {
                checkinService.addPost(id);
            }
            this.finish();
        } else {
            onPostAddedFailed();
        }
    }

    @Override
    public void onPostAddedFailed() {
        if (!isBackPressed)
            Toast.makeText(this, R.string.cant_add_post, Toast.LENGTH_LONG)
                    .show();
        isBackPressed = false;
        setResult(FAILED);
    }

    public void addQuestion(Question question) {
        if (adapter != null) {
            // add to screen
            int newQuestionId = adapter.add(question);
            if (newQuestionId >= 0) {
                listLinearLayout.addView(adapter.getView(newQuestionId, null, listLinearLayout));
            }

            // add to questions table in database
            // TODO: consider doing it in the background
            if (addDefaultQuestionCheckBox != null && addDefaultQuestionCheckBox.isChecked()) {
                QuestionSqlOperations operations = new QuestionSqlOperations(this);
                try {
                    operations.insert(question.toContentValues());
                } catch (android.database.SQLException e) {
                    Toast.makeText(this, R.string.check_in_cant_add_permanent_question,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
