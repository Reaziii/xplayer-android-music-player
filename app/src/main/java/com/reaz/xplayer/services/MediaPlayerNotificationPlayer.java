package com.reaz.xplayer.services;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.reaz.xplayer.R;
import com.reaz.xplayer.helpers.MusicArtworkshelper;
import com.reaz.xplayer.helpers.singleSongData;
import com.reaz.xplayer.interfaces.OnAudioPlayerStateChange;

public class MediaPlayerNotificationPlayer {
    private static NotificationManagerCompat notificationManagerCompat;
    private static AudioPlayer audioPlayer = new AudioPlayer();
    private static MusicArtworkshelper musicArtworkshelper = new MusicArtworkshelper();

    public MediaPlayerNotificationPlayer(Context context, Activity activity){
            notificationManagerCompat = NotificationManagerCompat.from(context);
            String CHANNEL_ID = "my_channel_id";
            Bitmap largeImage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.dms);
            Intent intent = new Intent(context, NotificationBroadcast.class);
            intent.setAction("ACTION_PREV");
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent, 0);
            intent.setAction("ACTION_PLAY");
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 1, intent, 0);
            intent.setAction("ACTION_NEXT");
            PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, 2, intent, 0);
            intent.setAction("ACTION_NOTIFICATION");
            NotificationCompat.Builder channel = new NotificationCompat.Builder(activity.getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.mipmap.logo_round)
                    .setLargeIcon(largeImage)
                    .setPriority(NotificationManager.IMPORTANCE_NONE)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setAutoCancel(true)
                    .setProgress(100000, 1000, false)
                    .setOngoing(true)
                    .setSound(null)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle());


            audioPlayer.setOnAudioPlayerStateChange(new OnAudioPlayerStateChange() {
                @Override
                public void onMusicChange(singleSongData song) {
                    channel.setContentText(song.album + " - " + song.title);
                    channel.setContentTitle(song.title);
                    channel.setProgress(100, 10, false);
                    channel.setLargeIcon(musicArtworkshelper.retriveMusicArtwork(song.songId));
                }
                @Override
                public void onMusicStateChange(String status) {
                    if(status=="play"){
                        channel.clearActions();
                        channel.addAction(R.drawable.skipprevwhite, "prev", pendingIntent1)
                                .addAction(R.drawable.pausewhite, "play", pendingIntent2)
                                .addAction(R.drawable.skipnextwhite, "next", pendingIntent3);
                    }
                    else{
                        channel.setPriority(NotificationManager.IMPORTANCE_NONE);
                        channel.clearActions();
                        channel.addAction(R.drawable.skipprevwhite, "prev", pendingIntent1)
                                .addAction(R.drawable.playwhite, "play", pendingIntent2)
                                .addAction(R.drawable.skipnextwhite, "next", pendingIntent3);
                    }
                    channel.setPriority(NotificationManager.IMPORTANCE_NONE);
                    notificationManagerCompat.notify(3334, channel.build());
                }
            });

    }



}
