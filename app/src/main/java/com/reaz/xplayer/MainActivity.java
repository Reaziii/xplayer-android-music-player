package com.reaz.xplayer;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.reaz.xplayer.components.MusicPlayer;
import com.reaz.xplayer.dataase.MusicDB;
import com.reaz.xplayer.dataase.database;
import com.reaz.xplayer.helpers.Drawer;
import com.reaz.xplayer.helpers.Permissions;
import com.reaz.xplayer.helpers.navigation;
import com.reaz.xplayer.helpers.utils;
import com.reaz.xplayer.services.MediaPlayerNotificationPlayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Drawer drawer;
    private navigation _navigation;
    private static Stack<String> history = new Stack<>();
    private database db;
    private Permissions permissionsHelper = new Permissions();
    FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
    private NotificationManagerCompat notificationManagerCompat;
    private String CHANNEL_ID = "media_channel_1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utils util = new utils(this, getWindowManager(), (View) findViewById(R.id.main_activity));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer = new Drawer(drawerLayout);
         _navigation = new navigation(getSupportFragmentManager(), this, R.id.main_container);
        _navigation.navigate("homescreen");
        db = new database(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MusicPlayer musicPlayer = new MusicPlayer((LinearLayout) findViewById(R.id.main_container));
        transaction.add(R.id.main_active_relative_layout, musicPlayer).commit();
        permissionsHelper.AskForStoragePermission(this, this);
        MusicDB musicDB = new MusicDB(this);
        musicDB.storeAllsongs();
        MediaPlayerNotificationPlayer mediaPlayerNotificationPlayer = new MediaPlayerNotificationPlayer(this,this);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            MusicDB musicDB = new MusicDB(this);
            musicDB.storeAllsongs();
        }
        else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    123);
        }

    }




    @Override
    public void onBackPressed(){
        if(drawer.isOpen()){
            drawer.close();
        }else{
            if(!_navigation.goBack()){
                super.onBackPressed();
            }

        }
    }


    @Override
    public void onDetachedFromWindow() {
        Log.d("music", "closed");
        super.onDetachedFromWindow();
    }
}