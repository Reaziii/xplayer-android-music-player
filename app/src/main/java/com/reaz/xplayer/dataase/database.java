package com.reaz.xplayer.dataase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.reaz.xplayer.interfaces.OnDatabaseChange;

import java.util.ArrayList;

public class database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "xplayer.db";
    private static final int DATABASE_VERSION = 1;
    private static Context ctx;
    public static ArrayList<OnDatabaseChange> listeners = new ArrayList<>();
    public database(){
        super(ctx,DATABASE_NAME, null, DATABASE_VERSION);
    }
    public database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        ctx = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("database", "creating");
        String[] sqls = {
                "CREATE TABLE IF NOT EXISTS playlists(id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(10) NOT NULL);",
                "CREATE TABLE IF NOT EXISTS songs(id INTEGER PRIMARY KEY AUTOINCREMENT, name varchar(200) NOT NULL, path varchar(200) NOT NULL, artist varchar(200) NOT NULL, album varchar(200) NOT NULL, duration integer not null);",
                "CREATE TABLE IF NOT EXISTS playlistSongs(id INTEGER PRIMARY KEY AUTOINCREMENT, playlistId int NOT NULL, songId int not null);"
        };
        for(String sql : sqls){
            try{
                db.execSQL(sql);
            }catch (Exception e){
                Toast.makeText(ctx, "Database creation error", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void setOnDatabaseChanges(OnDatabaseChange listener){
        listeners.add(listener);
    }
}
