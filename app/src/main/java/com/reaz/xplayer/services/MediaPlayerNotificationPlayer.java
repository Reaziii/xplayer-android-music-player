package com.reaz.xplayer.services;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.reaz.xplayer.R;

public class MediaPlayerNotificationPlayer {
    Context ctx;
    private static final String CHANNEL_ID = "music_player_channel";
    private static final String CHANNEL_NAME = "Music Player";
    private static AudioPlayer player = new AudioPlayer();

    private static MediaSessionCompat mediaSession;
    private static NotificationCompat.Builder builder;
    public MediaPlayerNotificationPlayer(Context context, Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        mediaSession = new MediaSessionCompat(context, "hello");
        builder = new NotificationCompat.Builder(context, CHANNEL_ID);
       if(player.current!=null){
           builder.setContentTitle("Not playing")
                   .setContentText(player.current.title)
                   .setSmallIcon(R.drawable.dms)
                   .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                           .setMediaSession(mediaSession.getSessionToken())
                           .setShowActionsInCompactView(0, 1, 2)) // Customize which actions are shown in compact view
                   .setPriority(NotificationCompat.PRIORITY_DEFAULT);
           RemoteViews notificationLayout = new RemoteViews(activity.getPackageName(), R.layout.notficatioin_player);
           NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
           notificationManager.notify(123, builder.build());
       }

    }
}
