package com.reaz.xplayer.services;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.reaz.xplayer.dataase.MusicDB;
import com.reaz.xplayer.dataase.PlaylistDB;
import com.reaz.xplayer.dataase.database;
import com.reaz.xplayer.helpers.singleSongData;
import com.reaz.xplayer.interfaces.OnDatabaseChange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaylistUtils {
    private static Context ctx;
    private static MusicDB musicDB;
    public static ArrayList<singleSongData> songs = new ArrayList<>();
    public static Map<Integer, Integer> IdWithSongs = new HashMap<>();
    public static Map<Integer, ArrayList<Integer>> playlistSongsId = new HashMap<>();
    private static database db;
    private PlaylistDB playlistDB;
    public PlaylistUtils(Context ctx){
        this.ctx = ctx;
        musicDB = new MusicDB(ctx);
        playlistDB = new PlaylistDB();
        db = new database(ctx);
        retriveSongs();
        retrivePlaylistSongs();

        db.setOnDatabaseChanges(new OnDatabaseChange() {
            @Override
            public void onPlaylistSongUpdate() {
                retrivePlaylistSongs();
            }
        });
    }
    public PlaylistUtils(){}
    private void retriveSongs(){
        Cursor cursor = musicDB.allSongs();
        songs.clear();
        while (cursor.moveToNext()){
            String title = cursor.getString(1);
            String album = cursor.getString(4);
            String artist = cursor.getString(3);
            String path = cursor.getString(2);
            int duration = cursor.getInt(5);
            int songid = cursor.getInt(0);
            Log.d("itx -d", String.valueOf(songid));
            singleSongData songData = new singleSongData(title, album, artist, duration,path, songid);
            IdWithSongs.put(songid, songs.size());
            songs.add(songData);
        }
    }
    private void retrivePlaylistSongs(){
        Cursor cursor = playlistDB.retrivePlaylistWithSongsId();
        playlistSongsId = new HashMap<>();
        playlistSongsId = new HashMap<>();
        playlistSongsId.put(1000, new ArrayList<>());
        while(cursor.moveToNext()){
            ArrayList<Integer> list = playlistSongsId.get(cursor.getInt(1));
            if(list==null) list = new ArrayList<>();
            list.add(cursor.getInt(2));
            playlistSongsId.put(cursor.getInt(1),list);
        }
        ArrayList<Integer> temp = new ArrayList<>();
        for(singleSongData song : songs){
            temp.add(song.songId);
        }
        playlistSongsId.put(1000, temp);

    }
    public ArrayList<singleSongData> retriveSongsByPlaylist(String name){
        ArrayList<singleSongData> list = new ArrayList<>();
        int id = playlistDB.getPlaylistIdByName(name);
        ArrayList<Integer> idxx = playlistSongsId.get(id);
        if(idxx==null) return list;
        for(int i : idxx){
            int idx = IdWithSongs.get(i);
            list.add(songs.get(idx));
        }
        return list;
    }
    public ArrayList<singleSongData> retriveAllSongs(){
        return songs;
    }
    public singleSongData getSongDetails(int id){
        return songs.get(IdWithSongs.get(id));
    }
    public singleSongData nextSong(int playlistid, int songid){
        int idx = playlistSongsId.get(playlistid).indexOf(songid);
        idx++;
        idx%=playlistSongsId.get(playlistid).size();
        idx = playlistSongsId.get(playlistid).get(idx);
        return  songs.get(IdWithSongs.get(idx));
    }
    public singleSongData getSongs(int playlistid, int songid){
        int idx = playlistSongsId.get(playlistid).indexOf(songid);
        idx%=playlistSongsId.get(playlistid).size();
        idx = playlistSongsId.get(playlistid).get(idx);
        return  songs.get(IdWithSongs.get(idx));
    }
    public singleSongData prevSong(int playlistid, int songid){
        int idx = playlistSongsId.get(playlistid).indexOf(songid);
        idx--;
        idx = Math.max(0, idx);
        idx = playlistSongsId.get(playlistid).get(idx);
        return  songs.get(IdWithSongs.get(idx));
    }
}
