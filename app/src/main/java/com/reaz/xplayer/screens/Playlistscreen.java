package com.reaz.xplayer.screens;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reaz.xplayer.R;
import com.reaz.xplayer.components.SinglePlaylistItem;
import com.reaz.xplayer.dataase.PlaylistDB;
import com.reaz.xplayer.dataase.database;
import com.reaz.xplayer.interfaces.OnDatabaseChange;

public class Playlistscreen extends Fragment {
    private View v;
    private String playlistname = "";
    private PlaylistDB playlistDB = new PlaylistDB();
    public database db = new database();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_playlistscreen, container, false);
        init();

        return v;
    }

    private void init(){
        showAllPlaylists();
        LinearLayout createButton = (LinearLayout) v.findViewById(R.id.createnewplaylist);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPlaylist();
            }
        });

        db.setOnDatabaseChanges(new OnDatabaseChange() {
            @Override
            public void onPlaylistChange() {
                showAllPlaylists();
            }
        });
    }

    private void createNewPlaylist(){
        Dialog dialog = new Dialog(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.create_playlist_dialog, null);
        dialog.setContentView(v);
        dialog.show();
        TextView okbtn = (TextView) dialog.findViewById(R.id.okay_button);
        TextView canclebtn = (TextView) dialog.findViewById(R.id.cancel_button);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = dialog.findViewById(R.id.newplaylistname);
                String name = text.getText().toString();
                String msg = playlistDB.createNewPlaylist(name);
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void showAllPlaylists(){
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        Fragment f = new SinglePlaylistItem();
        transaction.replace(R.id.playlistitems, f).commit();
        transaction = getParentFragmentManager().beginTransaction();
        transaction.remove(f).commit();
        Cursor cursor = playlistDB.retriveAllPlaylist();
        while(cursor.moveToNext()){
            transaction = getParentFragmentManager().beginTransaction();
            SinglePlaylistItem singlePlaylistItem = new SinglePlaylistItem();
            singlePlaylistItem.setPlaylistid(cursor.getInt(0));
            singlePlaylistItem.setPlaylistName(cursor.getString(1));
            transaction.add(R.id.playlistitems, (Fragment) singlePlaylistItem).commit();
        }
    }
}
