package com.reaz.xplayer.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.reaz.xplayer.R;
import com.reaz.xplayer.adapters.SongListRecycleViewAdapter;
import com.reaz.xplayer.components.MyToolbar;
import com.reaz.xplayer.dataase.PlaylistDB;
import com.reaz.xplayer.services.PlaylistUtils;
import com.reaz.xplayer.helpers.singleSongData;
import com.reaz.xplayer.helpers.utils;

import java.util.ArrayList;

public class SongsByList extends Fragment {
    private View v;
    private String playlistname = "All songs";
    private PlaylistUtils playlistUtils;
    private utils util = new utils();
    public SongsByList(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.songs_by_list, container, false);
        playlistUtils = new PlaylistUtils(getContext());
        init();
        return v;
    }
    public void setPlaylistname(String playlistname){
        this.playlistname = playlistname;
    }
    private void init(){
        LinearLayout p = (LinearLayout) v.findViewById(R.id.fortoolbarsection);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        MyToolbar toolbar = new MyToolbar(getContext(), playlistname,getContext().getDrawable(R.drawable.backwhite) );
        p.addView(toolbar, params);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.songlistitems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setHorizontalScrollBarEnabled(false);
        ArrayList<singleSongData> list = new ArrayList<>();
        if(playlistname=="All songs"){
            list = playlistUtils.retriveAllSongs();
        }
        else {
            list = playlistUtils.retriveSongsByPlaylist(playlistname);
        }
        PlaylistDB db = new PlaylistDB();
        int id = db.getPlaylistIdByName(playlistname);

        for(int i = 0;i<list.size();i++){
            singleSongData data = list.get(i);
            data.playlistId = id;
            list.set(i, data);
        }
        SongListRecycleViewAdapter adapter = new SongListRecycleViewAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);


    }
}