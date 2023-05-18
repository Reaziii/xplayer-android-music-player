package com.reaz.xplayer.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reaz.xplayer.R;
import com.reaz.xplayer.dataase.PlaylistDB;

public class AddToPlaylistDialog {
    private Context context;
    private int songid;
    private Dialog dialog;
    private PlaylistDB playlistDB = new PlaylistDB();
    private String songName;
    LinearLayout root;
    public AddToPlaylistDialog(Context context, int songId, String songName){
        this.context = context;
        this.songid = songId;
        dialog = new Dialog(context);
        this.songName = songName;
    }

    public void openDialog(){
        dialog.setContentView(R.layout.add_to_playlistdialog);
        TextView title = (TextView)dialog.findViewById(R.id.songtitle);
        title.setText(songName);
        Window window = dialog.getWindow();
        if(window!=null){
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = (int) (context.getResources().getDisplayMetrics().widthPixels);
            window.setAttributes(layoutParams);
        }
        Cursor cursor = playlistDB.retriveAllPlaylist();
        root = dialog.findViewById(R.id.playlistitems);
        while(cursor.moveToNext()){
            AddToPlaylistDialog.playlistItem playlistItem = new AddToPlaylistDialog.playlistItem(cursor.getString(1), cursor.getInt(0));
            root.addView(playlistItem);
        }

        dialog.show();
    }

    class playlistItem extends LinearLayout{

        public playlistItem(String name, int id) {
            super(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.single_playlist_item_add_to_playlist_dialog, this, true);
            TextView text = v.findViewById(R.id.playlistname);
            text.setText(name);
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = playlistDB.addSongToPlaylist(id, songid);
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }



}
