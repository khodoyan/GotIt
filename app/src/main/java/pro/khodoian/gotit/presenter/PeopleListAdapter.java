package pro.khodoian.gotit.presenter;

import android.app.Activity;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.models.User;
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
            holder = new ViewHolder();
            holder.parentLayout =
                    (LinearLayout) resultView.findViewById(R.id.people_list_item_parent_layout);
            holder.userpic =
                    (ImageView) resultView.findViewById(R.id.people_list_item_userpic);
            holder.name =
                    (TextView) resultView.findViewById(R.id.people_list_item_name);
            holder.isFollowed =
                    (CheckBox) resultView.findViewById(R.id.post_list_item_follow);
            holder.shareFeeling =
                    (CheckBox) resultView.findViewById(R.id.post_list_item_share_feeling);
            holder.shareBloodSugar =
                    (CheckBox) resultView.findViewById(R.id.post_list_item_share_blood_sugar);
            holder.shareInsulin =
                    (CheckBox) resultView.findViewById(R.id.post_list_item_share_insulin);
            holder.shareQuestions =
                    (CheckBox) resultView.findViewById(R.id.post_list_item_share_questions);
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
        }

        final View finalView = resultView;

        if (users.size() > 0) {
            User user = users.get(position);
            if (user != null) {
                if (user.getUserpic() != null)
                    holder.userpic.setImageDrawable(user.getUserpic());
                holder.name.setText(user.getFullName());
                holder.isFollowed.setChecked(user.isFollowed());
                holder.shareFeeling.setChecked(user.isShareFeeling());
                holder.shareBloodSugar.setChecked(user.isShareBloodSugar());
                holder.shareInsulin.setChecked(user.isShareInsulin());
                holder.shareQuestions.setChecked(user.isShareQuestions());
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
                }
            };
            holder.name.setOnClickListener(listener);
            holder.userpic.setOnClickListener(listener);

        }
        return resultView;
    }

    public int add(User user) {
        // TODO: check if need to update view somehow
        users.add(user);
        return users.size() - 1;
    }

    public void onDeleteButtonClick(final int postion, LinearLayout settingsLayout) {
        // TODO: implement method
        // delete from list view adapter
        if (postion >= 0 && postion < users.size()) {
            collapseAllItemsSettings();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    users.remove(postion);
                    activity.get().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });

                }
            }, LinearLayoutExpandCollapseAnimation.DEFAULT_DURATION);
            expandedPosition = -1;
        }

        // mark deleted from local database
        // show snackbar with UNDO action
        // TODO: delete from server
        // if error, show snackbar with RETRY action
    }

    public void onDoneButtonClick(int position, LinearLayout settingsLayout) {
        // TODO: implement method
        // update in the list view adapter
        // hide settings with animation
        if (activity.get() != null)
            LinearLayoutExpandCollapseAnimation.collapse(settingsLayout);

        // update in local database, save value before update
        // show snacbar with UNDO action
        // TODO: update on server
        // if error, show snackbar with RETRY action
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

    public void collapseOneItemEntirely(final int position) {
        Log.v(TAG, "collapseOneItemEntirely. Position = " + String.valueOf(position));
        if (position < getCount() && position >= 0) {
            final ListView listView = (ListView) activity.get().findViewById(R.id.people_list_view);
            // TODO: correct. Deletes one and hides another one. What is happening when visibility is set to GONE to getChildAt()

            if(position - listView.getFirstVisiblePosition() >= 0) {
                LinearLayoutExpandCollapseAnimation.collapseAndExecuteRunnable(listView.getChildAt(
                        position - listView.getFirstVisiblePosition()
                ), new Runnable(){
                    @Override
                    public void run() {
                        if (activity.get() != null)
                            activity.get().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.v(TAG, String.format("Removing item %d of %d", new Integer[]{position, users.size()}));

                                    PeopleListAdapter.this.notifyDataSetChanged();
                                }
                            });
                    }
                });
            }
            users.remove(position);
            expandedPosition = -1;
        }
    }
}
