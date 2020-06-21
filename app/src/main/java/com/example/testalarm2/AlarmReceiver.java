package com.example.testalarm2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationid = intent.getStringExtra("id");
        String text = intent.getStringExtra("drug");

        Log.e("약번호 넘어오자...", String.valueOf(notificationid));
        Log.e("약이름 넘어오자...",text);

        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, Integer.parseInt(notificationid),mainIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "drugId");
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.P) {

            builder.setSmallIcon(R.drawable.ic_drug_icon);

            String chaanelName = "약쏙";
            String description = "매일 정해진 시간에 알림합니다. ";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("drugId", chaanelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        else
            builder.setSmallIcon(R.mipmap.ic_launcher);

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("약쏙")
                .setContentText(text+"을(를) 복용할시간에요:)")
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setContentInfo("INFO")
                .setDefaults(Notification.DEFAULT_VIBRATE);



        if(notificationManager !=null){


            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "My:Tag"
            );
            wakeLock.acquire(5000);
            notificationManager.notify(Integer.parseInt(notificationid), builder.build());


        }


       /* String text = intent.getStringExtra("drug");
        int notificationId = intent.getIntExtra("id",0);

        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentPIntent = PendingIntent.getActivity(context,notificationId,activityIntent,0);

        String channelId = "channelid";
        String chaeelname = "약쏙";
        String description = "약 복용시간에 알림합니다.";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId, chaeelname,NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);

            if(notificationManager !=null){
                notificationManager.createNotificationChannel(channel);
            }

        }
        else

        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_drug_icon)
                .setContentTitle("약쏙")
                .setContentText(text+"을(를) 복용할 시간이에요.")
                .setPriority(Notification.VISIBILITY_PRIVATE)
                .setContentIntent(contentPIntent)
                .setContentInfo("INFO")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setWhen(System.currentTimeMillis())
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

*/



    }
}
