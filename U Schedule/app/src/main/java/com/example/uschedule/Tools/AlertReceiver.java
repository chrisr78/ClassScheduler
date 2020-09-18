package com.example.uschedule.Tools;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.uschedule.R;
import com.example.uschedule.TermActivity;

public class AlertReceiver extends BroadcastReceiver {

    String CHANNEL_ID = "U_SCHEDULE_REMINDER";
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context, CHANNEL_ID);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent remind = new Intent(context, TermActivity.class );

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1001, remind, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notify)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_notify))
                .setContentTitle("U Schedule Reminder")
                .setContentText(intent.getStringExtra("what"))
                .setAutoCancel(true);

        notificationManager.notify(100, builder.build());
    }

    private void createNotificationChannel(Context context, String channel_id) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getResources().getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
