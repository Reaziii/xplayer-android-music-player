package com.reaz.xplayer.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.reaz.xplayer.R;
import com.reaz.xplayer.services.PlaylistUtils;
import com.reaz.xplayer.helpers.singleSongData;

public class SingleSongDialog {
    private Context context;
    private int songId;
    private Dialog dialog;
    private PlaylistUtils playlistUtils = new PlaylistUtils();
    private singleSongData song;
    public SingleSongDialog(Context context, int songId){
        this.context = context;
        this.songId = songId;
        dialog = new Dialog(context);
        song = playlistUtils.getSongDetails(songId);
    }

    public void openDialog(){
        dialog.setContentView(R.layout.single_song_dialogbox);
        ((TextView)dialog.findViewById(R.id.songtitledxx)).setText(song.title);
        Window window = dialog.getWindow();

        if(window!=null){
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) (context.getResources().getDisplayMetrics().widthPixels);
            window.setAttributes(layoutParams);
        }


        dialog.findViewById(R.id.addtoplaylistbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                AddToPlaylistDialog addToPlaylistDialog = new AddToPlaylistDialog(context, songId, song.title);
                addToPlaylistDialog.openDialog();
            }
        });
        dialog.show();
    }

}
