package com.reaz.xplayer.helpers;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.reaz.xplayer.dialogs.SingleSongDialog;
import com.reaz.xplayer.services.AudioPlayer;

public class SingleSongHelpers {
    private LinearLayout parent;
    private Context context;
    private int songid, plalistid;
    public SingleSongHelpers(Context context, LinearLayout parent, int songid, int playlistId){
        this.parent = parent;
        this.context = context;
        this.songid = songid;
        this.plalistid = playlistId;
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioPlayer audioPlayer = new AudioPlayer();
                audioPlayer.set(songid, playlistId);
                String msg = audioPlayer.play();
                Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            }
        });

        parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // open dialog

                SingleSongDialog dialogbox = new SingleSongDialog(context, songid);
                dialogbox.openDialog();
                return true;
            }
        });
    }

    
}
