package com.reaz.xplayer.components;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reaz.xplayer.R;
import com.reaz.xplayer.helpers.navigation;

public class SinglePlaylistItem extends Fragment {
    private String playlistname = "";
    private int playlistid;
    private boolean opened = false;
    private navigation _navigation = new navigation();
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.single_playlist_item, container, false);
        init();
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LinearLayout layout =  v.findViewById(R.id.maintabxyz);
//                if(!opened) layout.setX(-200);
//                else layout.setX(0);
//
//                opened = !opened;
//

                return true;
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _navigation.navigate("playlist."+playlistname);
            }
        });
        return v;
    }
    public void setPlaylistid(int id){
        this.playlistid = id;
    }

    private void init(){
        TextView title = (TextView)v.findViewById(R.id.playlistname);
        title.setText(playlistname);
    }

    public void setPlaylistName(String name){
        playlistname = name;
    }
}