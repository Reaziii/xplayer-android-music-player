package com.reaz.xplayer.dataase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.reaz.xplayer.helpers.utils;
import com.reaz.xplayer.interfaces.OnDatabaseChange;

import java.util.ArrayList;

public class PlaylistDB {
    private SQLiteDatabase db;
    private database _database;
    private utils util = new utils();
    public PlaylistDB(){
        _database = new database();
        db = _database.getWritableDatabase();
    }
    public String createNewPlaylist(String name){
        String ret = "playlist created";
        name = util.escapeString(name);
        String sql = "SELECT * FROM playlists WHERE name='"+name+"'";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount()>0){
            return "Playlist exists";
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        int id = (int) db.insert("playlists", null, contentValues);
        if(id==-1) return "creating playlist failed";
        onPlaylistChanges();
        return ret;
    }
    public Cursor retriveAllPlaylist(){
        Cursor ret;
        String sql = "SELECT * FROM playlists";
        ret = db.rawQuery(sql, null);
        return ret;
    }
    public String addSongToPlaylist(int playlistid, int songid){
        String sql = "SELECT * FROM  playlistSongs WHERE playlistId="+String.valueOf(playlistid)+" and songId="+String.valueOf(songid)+";";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount()>0) return "Already exists";
        ContentValues contentValues = new ContentValues();
        contentValues.put("playlistId", playlistid);
        contentValues.put("songId", songid);
        int id = (int) db.insert("playlistSongs", null, contentValues);
        if(id==-1){
            return "Something went wrong";
        }
        onPlaylistsSongUpdate();
        return "Added song to playlist";
    }

    private void onPlaylistChanges(){
        for(OnDatabaseChange listener : _database.listeners){
            try{
                listener.onPlaylistChange();
            }catch (Exception e){

            }
        }
    }


    private void onPlaylistsSongUpdate(){
        for(OnDatabaseChange listener : _database.listeners){
            try{
                listener.onPlaylistSongUpdate();
            }catch (Exception e){

            }
        }
    }

    public Cursor retrivePlaylistWithSongsId(){
        String sql = "SELECT * FROM playlistSongs";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;

    }
    public int getPlaylistIdByName(String name){
        String sql = "SELECT * FROM playlists WHERE name='"+name+"';";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount()==0){
            Log.d("idx", "hello world");
            return 1000;
        }
        while(cursor.moveToNext()){
            return  cursor.getInt(0);
        }
        return 1000;

    }
}
