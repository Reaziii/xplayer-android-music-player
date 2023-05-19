package com.reaz.xplayer.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.reaz.xplayer.MainActivity;

public class NotificationBroadcast extends BroadcastReceiver {
    public static final AudioPlayer audioPlayer = new AudioPlayer();
    @Override

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if ("ACTION_PLAY".equals(action)) {
                if(audioPlayer.getStatus()=="pause"){
                    audioPlayer.resume();
                }
                else{
                    audioPlayer.pause();
                }
            } else if ("ACTION_NEXT".equals(action)) {
                audioPlayer.next();
            }
            else if ("ACTION_PREV".equals(action)) {
                audioPlayer.prev();
            }
//            else if("ACTION_NOTIFICATION".equals(action)){
//                Intent intent1 = new Intent(context, MainActivity.class);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                context.startActivity(intent1);
//            }
        }
    }
}
