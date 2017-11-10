package jlvivero.medule.timers;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import jlvivero.medule.MainActivity;
import jlvivero.medule.R;

/**
 * Created by joslu on 11/9/2017.
 */

public class AlarmReceiver extends BroadcastReceiver{
    private static NotificationChannel CH;
    private static NotificationManager MAN;
    public static String ID;

    @Override
    public void onReceive(Context context, Intent intent) {

        //this actually builds the notification, we don't want to actually build it yet
        //so we have to move this code to alarm receiver. But alarm receiver needs to be able to recieve the information above somehow
        //TODO: make or get an icon for notifications
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ID).setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("reminder").setContentText("take your meds");
        Intent resultIntent = new Intent(context, MainActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        MAN.notify(0, builder.build());
        Log.d("alarm", "sound the alarm");
    }

    public static void getNotificationInfo(NotificationManager notif, String id) {
        MAN = notif;
        ID = id;
    }

    @TargetApi(26)
    public static void getChannel(NotificationChannel channel){
        CH = channel;
    }
}
