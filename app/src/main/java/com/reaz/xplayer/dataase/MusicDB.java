package com.reaz.xplayer.dataase;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import com.reaz.xplayer.helpers.Permissions;
import com.reaz.xplayer.helpers.utils;

public class MusicDB {
    private database db = new database();
    private utils util = new utils();
    private static Context ctx;
    public MusicDB(Context ctx){
        this.ctx = ctx;
    }

    public MusicDB(){

    }

    public Cursor allSongs(){
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        String sql = "SELECT * FROM songs ORDER BY name ASC";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        return cursor;
    }

    public String getMusicPathBuId(int songid){
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        String sql = "SELECT * FROM songs WHERE id="+songid;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        if(cursor.getCount()==0) return null;
        while(cursor.moveToNext()){
            return  cursor.getString(2);
        }
        return null;
    }
    public void AddSong(String _name,String _path, String _album, String _artist,int duration){
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        Log.d("database-x", _name);
        String sql = "SELECT * FROM songs WHERE path='"+util.escapeString(_path)+"'";
        try{
            Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
            if(cursor.getCount()>0) return ;
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", _name);
            contentValues.put("path", _path);
            contentValues.put("album", _album);
            contentValues.put("artist", _artist);
            contentValues.put("duration", duration);
            if(duration<30000) throw new Exception("duration is to low");
            long id = sqLiteDatabase.insert("songs",null, contentValues);
            if(id==-1){
                throw new Exception("error");
            }
        }catch (Exception e){
            Log.d("error-xx", e.toString());
        }
    }
    public void storeAllsongs(){
        if(ContextCompat.checkSelfPermission(ctx, READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            return ;
        }
        Log.d("error-xx", "calling");

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        try (Cursor cursor = ctx.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
        )) {
            if (cursor != null) {
                int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int pathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

                //                int artwork = cursor.getColumnIndex(MediaStore.Audio.Media.AL)
                while (cursor.moveToNext()) {
                    String title = cursor.getString(titleColumn);
                    String artist = cursor.getString(artistColumn);
                    String album = cursor.getString(albumColumn);
                    String path = cursor.getString(pathColumn);
                    int duration = cursor.getInt(durationColumn);

                    AddSong(title, path, album, artist,duration );
                }
            }
        }

    }
}
