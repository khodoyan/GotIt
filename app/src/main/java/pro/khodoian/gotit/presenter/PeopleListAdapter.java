package pro.khodoian.gotit.presenter;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.models.Post;
import pro.khodoian.gotit.models.User;
import pro.khodoian.gotit.sql.UserSqlOperations;
import pro.khodoian.gotit.view.LinearLayoutExpandCollapseAnimation;

/**
 * Adapter class to generate view of list of questions in Post - MainActivity
 *
 * @author eduardkhodoyan
 */
public class PeopleListAdapter extends BaseAdapter {

    public class ViewHolder {
        private LinearLayout parentLayout;
        private ImageView userpic;
        private TextView name;
        private CheckBox isFollowed;
        private CheckBox shareFeeling;
        private CheckBox shareBloodSugar;
        private CheckBox shareInsulin;
        private CheckBox shareQuestions;
        private LinearLayout settingsLayout;
        private Button delete;
        private Button done;

    }

    public static final String TAG = PeopleListAdapter.class.getName();

    private int expandedPosition = -1;

    private ArrayList<User> users;
    private WeakReference<Activity> activity;


    public PeopleListAdapter(Activity activity, ArrayList<User> users) {
        this.users = users;
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public int getCount() {
        if (users != null)
            return users.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        if (users != null && i < getCount() && i >= 0)
            return users.get(i);
        else
            return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
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
                        .inflate(R.layout.people_list_item, null);
            //}

            holder = new ViewHolder();
            holder.parentLayout =
                    (LinearLayout) resultView.findViewById(R.id.people_list_item_parent_layout);
            holder.userpic =
                    (ImageView) resultView.findViewById(R.id.people_list_item_userpic);
            holder.name =
                    (TextView) resultView.findViewById(R.id.people_list_item_name);
            holder.isFollowed =
                    (CheckBox) resultView.findViewById(R.id.people_list_item_follow);
            holder.shareFeeling =
                    (CheckBox) resultView.findViewById(R.id.people_list_item_share_feeling);
            holder.shareBloodSugar =
                    (CheckBox) resultView.findViewById(R.id.people_list_item_share_blood_sugar);
            holder.shareInsulin =
                    (CheckBox) resultView.findViewById(R.id.people_list_item_share_insulin);
            holder.shareQuestions =
                    (CheckBox) resultView.findViewById(R.id.people_list_item_share_questions);
            holder.settingsLayout =
                    (LinearLayout) resultView.findViewById(R.id.people_list_item_settings_layout);
            holder.done =
                    (Button) resultView.findViewById(R.id.people_list_item_done_button);
            holder.delete =
                    (Button) resultView.findViewById(R.id.people_list_item_delete_button);

            if (position != expandedPosition) {
                holder.settingsLayout.setVisibility(View.GONE);
                holder.settingsLayout.getLayoutParams().height = 0;
            }
            resultView.setTag(holder);


            final View finalView = resultView;

            if (users.size() > 0) {
                User user = users.get(position);
                if (user != null) {
                    if (user.getUserpic() != null)
                        holder.userpic.setImageDrawable(user.getUserpic());
                    holder.name.setText(user.getFullName());
                    holder.isFollowed.setChecked(user.getFollowed());
                    holder.shareFeeling.setChecked(user.getShareFeeling());
                    holder.shareBloodSugar.setChecked(user.isShareBloodSugar());
                    holder.shareInsulin.setChecked(user.getShareInsulin());
                    holder.shareQuestions.setChecked(user.getShareQuestions());
                }
                holder.done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onDoneButtonClick(position,
                                (LinearLayout) finalView
                                        .findViewById(R.id.people_list_item_settings_layout));
                    }
                });
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onDeleteButtonClick(position,
                                (LinearLayout) finalView
                                        .findViewById(R.id.people_list_item_settings_layout));
                    }
                });

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v(TAG, "onClick(View view) started. View "
                                + (view == null ? "is null" : "is not null"));
                        Log.v(TAG, "expandedPosition = " + String.valueOf(expandedPosition));
                        Log.v(TAG, "position = " + String.valueOf(position));

                        // collapse expanded layout if any
                        if (expandedPosition >= 0 && activity.get() != null) {
                            collapseAllItemsSettings();
                        }

                        // expand clicked item if required
                        if (position != expandedPosition && finalView != null) {
                            View settingsLayout = finalView.findViewById(
                                    R.id.people_list_item_settings_layout);
                            if (settingsLayout != null) {
                                LinearLayoutExpandCollapseAnimation.expand(settingsLayout);
                                expandedPosition = position;
                                Log.v(TAG, "expand(View view) started");
                            }
                        } else {
                            // reset expandedPosition if there is no expanded position
                            expandedPosition = -1;
                        }
                        notifyDataSetChanged();
                    }
                };
                holder.name.setOnClickListener(listener);
                holder.userpic.setOnClickListener(listener);

            }
        }
        return resultView;
    }

    public int add(User user) {
        users.add(user);
        notifyDataSetChanged();
        return users.size() - 1;
    }

    public void remove(final int position) {
        if (position >= 0 && position < users.size()) {
            collapseAllItemsSettings();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    activity.get().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            users.remove(position);
                            notifyDataSetChanged();
                        }
                    });

                }
            }, LinearLayoutExpandCollapseAnimation.DEFAULT_DURATION);
            expandedPosition = -1;
        }
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        this.notifyDataSetChanged();
    }

    public void clearList() {
        this.users.clear();
        this.notifyDataSetChanged();
    }

    public void onDeleteButtonClick(final int position, LinearLayout settingsLayout) {
        // TODO: implement method
        if (users == null || position >= users.size()) {
            // Error. This must not be the case
            return;
        }

        final User user = users.get(position);
        this.remove(position);
        this.notifyDataSetChanged();


        Snackbar.make(activity.get().findViewById(R.id.people_parent_layout),"Deleting user",Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // UNDO pressed
                        users.add(position, user);
                        PeopleListAdapter.this.notifyDataSetChanged();
                    }
                })
                .setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        if (activity.get() != null) {
                            FloatingActionButton fab =
                                    (FloatingActionButton) activity.get()
                                            .findViewById(R.id.fab_people);
                            fab.show();

                            if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                // NOT UNDONE
                                // mark deleted from local database
                                new UserSqlOperations(activity.get())
                                        .deleteById(user.getId());
                                // TODO: delete from server
                                Toast.makeText(activity.get(),
                                        "Follower not actually deleted on server for debugging purposes",
                                        Toast.LENGTH_SHORT).show();
                                // if error, show snackbar with RETRY action
                            }
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        super.onShown(snackbar);
                        if (activity.get() != null) {
                            FloatingActionButton fab =
                                    (FloatingActionButton) activity.get()
                                            .findViewById(R.id.fab_people);
                            //fab.hide();
                        }
                    }
                }).show();

    }

    public void onDoneButtonClick(final int position, LinearLayout settingsLayout) {
        // update in the list view adapter
        // hide settings with animation
        if (activity.get() != null) {
            LinearLayoutExpandCollapseAnimation.collapse(settingsLayout);
            ListView listView = (ListView) activity.get().findViewById(R.id.people_list_view);

            // update in local database, save value before update
            if (users != null && users.get(position) != null && listView != null
                    && (position - listView.getFirstVisiblePosition()) >= 0
                    && (position - listView.getFirstVisiblePosition()) < users.size()) {
                final boolean isFollowed = users.get(position).getFollowed();
                final boolean isShareFeeling = users.get(position).getShareFeeling();
                final boolean isShareBloodSugar = users.get(position).isShareBloodSugar();
                final boolean isShareInsulin = users.get(position).getShareInsulin();
                final boolean isShareQuestion = users.get(position).getShareQuestions();

                CheckBox isFollowedCheckBox =
                        (CheckBox) listView.getChildAt(position - listView.getFirstVisiblePosition())
                                .findViewById(R.id.people_list_item_follow);
                CheckBox isShareFeelingCheckBox =
                        (CheckBox) listView.getChildAt(position - listView.getFirstVisiblePosition())
                                .findViewById(R.id.people_list_item_share_feeling);
                CheckBox isShareBloodSugarCheckBox =
                        (CheckBox) listView.getChildAt(position - listView.getFirstVisiblePosition())
                                .findViewById(R.id.people_list_item_share_blood_sugar);
                CheckBox isShareInsulinCheckBox =
                        (CheckBox) listView.getChildAt(position - listView.getFirstVisiblePosition())
                                .findViewById(R.id.people_list_item_share_insulin);
                CheckBox isShareQuestionCheckBox =
                        (CheckBox) listView.getChildAt(position - listView.getFirstVisiblePosition())
                                .findViewById(R.id.people_list_item_share_questions);

                final User user = users.get(position);
                user.setFollowed(isFollowedCheckBox.isChecked());
                user.setShareFeeling(isShareFeelingCheckBox.isChecked());
                user.setShareBloodSugar(isShareBloodSugarCheckBox.isChecked());
                user.setShareInsulin(isShareInsulinCheckBox.isChecked());
                user.setShareQuestions(isShareQuestionCheckBox.isChecked());

                if (new UserSqlOperations(activity.get())
                        .updateById(user.toContentValues(), user.getId())) {
                    Snackbar.make(activity.get().findViewById(R.id.people_parent_layout),
                            "Updated following/sharing options", Snackbar.LENGTH_LONG)
                            .setAction(R.string.undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // prepare user values for update
                                    user.setFollowed(isFollowed);
                                    user.setShareFeeling(isShareFeeling);
                                    user.setShareBloodSugar(isShareBloodSugar);
                                    user.setShareInsulin(isShareInsulin);
                                    user.setShareQuestions(isShareQuestion);

                                    // update values back
                                    if(!new UserSqlOperations(activity.get())
                                            .updateById(user.toContentValues(), user.getId()))
                                        Toast.makeText(activity.get(),
                                                "Can't undo the action. Sorry",
                                                Toast.LENGTH_LONG).show();
                                }
                            }).setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                                // TODO: update data on server
                                // if error, show snackbar with RETRY action
                            }
                            FloatingActionButton fab = (FloatingActionButton)
                                    activity.get().findViewById(R.id.fab_people);
                            fab.show();
                            PeopleListAdapter.this.notifyDataSetChanged();
                        }

                        @Override
                        public void onShown(Snackbar snackbar) {
                            super.onShown(snackbar);
                            FloatingActionButton fab = (FloatingActionButton)
                                    activity.get().findViewById(R.id.fab_people);
                            //fab.hide();
                        }
                    }).show();
                }
            }
        }

    }



    public void collapseAllItemsSettings() {

        if (activity.get() != null) {
            ListView listView = (ListView) activity.get().findViewById(R.id.people_list_view);
            for (int i = 0; i<listView.getChildCount(); i++) {
                View expandedSettingsLayout = listView.getChildAt(i)
                        .findViewById(R.id.people_list_item_settings_layout);
                if (activity.get() != null) {
                    LinearLayoutExpandCollapseAnimation.collapse(
                            expandedSettingsLayout
                    );
                }
            }
            Log.v(TAG, "Collapsed all items of listView");
        }
    }
}
