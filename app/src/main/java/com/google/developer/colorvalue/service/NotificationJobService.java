package com.google.developer.colorvalue.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.developer.colorvalue.MainActivity;
import com.google.developer.colorvalue.R;

public class NotificationJobService extends JobService {

    public static final int NOTIFICATION_ID = 18;

    private static final int PENDING_INTENT_ID = 19;
    private static final String NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    private AsyncTask mNotificationTask;

    @Override
    public boolean onStartJob(final JobParameters params) {
        mNotificationTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = NotificationJobService.this;
                remindUser(context);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(params,false);
            }
        };
        // TODO notification

        mNotificationTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (mNotificationTask != null) mNotificationTask.cancel(true);
        return true;
    }

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void remindUser(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.notification_channel_id),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_dialog_info)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.time_to_practice))
                .setContentText(context.getString(R.string.it_is_time_to_practice))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.it_is_time_to_practice)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_dialog_info);
        return largeIcon;
    }

}