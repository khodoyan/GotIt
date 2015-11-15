package pro.khodoian.gotit.broadcastreceivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import pro.khodoian.gotit.R;
import pro.khodoian.gotit.view.CheckinActivity;

/**
 * This class is a broadcast receiver class intended to get pending intents for showing
 * notifications to user of the app. Notification shall start checkin activity, when tapped
 *
 * @author eduardkhodoyan
 */
public class RegularNotificationReceiver extends BroadcastReceiver {
    public static final String REQUESTED_BY_KEY = "Requested by";
    public static final String REQUESTED_BY = "Check in notification";
    public static final String ACTION_MORNING_ALARM = "pro.khodoian.MORNING_ALARM";
    public static final String ACTION_AFTERNOON_ALARM = "pro.khodoian.AFTERNOON_ALARM";
    public static final String ACTION_EVENING_ALARM = "pro.khodoian.EVENING_ALARM";

    public void onReceive(Context context, Intent intent) {
        // Build notification: set text, title and icon
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_assignment_late_black_48dp)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText(context.getString(R.string.notification_check_in_text));

        // create intent to start checkin activity and a respective pending intent
        Intent resultIntent = CheckinActivity.makeIntent(context);
        resultIntent.putExtra(REQUESTED_BY_KEY, REQUESTED_BY);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        CheckinActivity.REQUEST_FROM_NOTIFICATION,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        // assign intent to tapping notification content
        mBuilder.setContentIntent(resultPendingIntent);

        // set an ID for the notification
        int mNotificationId = 101;
        // get an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // build the notification and issue it
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, RegularNotificationReceiver.class);
    }
}
