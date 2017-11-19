package jlvivero.medule.timers;

import android.annotation.TargetApi;
import android.app.Notification;
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
import jlvivero.medule.models.Database;
import jlvivero.medule.models.Medicine;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by joslu on 11/9/2017.
 */

public class AlarmReceiver extends BroadcastReceiver{
    private static NotificationChannel CH;
    private static NotificationManager MAN;
    public static String ID;

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id",0);

        //notification code
        createNotification(context);

        Database db = Database.getDatabase(context);
        Medicine toEdit = db.medicineDao().get(id);
        toEdit.setDue(true);
        db.medicineDao().updateMed(toEdit);
        String name = toEdit.getMedName();
        //this actually builds the notification, we don't want to actually build it yet
        //so we have to move this code to alarm receiver. But alarm receiver needs to be able to recieve the information above somehow
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ID).setSmallIcon(R.drawable.ic_stat_onesignal_default).setContentTitle("reminder").setContentText("It's time to take " + name);
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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


    public void createNotification(Context context){
        //change this to a different method that has the annotaiton @TargetAPI
        NotificationManager notif = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //the id of the channel
        String id = "medule_reminder";
        //the user-visible name of the channel
        CharSequence name = context.getString(R.string.channel_name);
        //the user-visible description of the channel
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        createChannel(notif, id, name, importance, description);
        getNotificationInfo(notif, id);
    }

    @TargetApi(26)
    public void createChannel(NotificationManager notif, String id, CharSequence name, int importance, String description) {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setDescription(description);
        channel.enableLights(true);
        channel.enableVibration(true);
        notif.createNotificationChannel(channel);
        getChannel(channel);
    }

}
