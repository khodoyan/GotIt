package pro.khodoian.gotit.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class is a broadcast receiver class intended to get pending intents for showing
 * notifications to user of the app
 *
 * @author eduardkhodoyan
 */
public class RegularNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: Implement notification
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, RegularNotificationReceiver.class);
    }
}
