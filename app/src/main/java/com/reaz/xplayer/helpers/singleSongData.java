package com.reaz.xplayer.helpers;

import android.graphics.Bitmap;

public class singleSongData {
    utils utils = new utils();
    public String title, album, artist, path;
    public int duration;
    public Bitmap artwork;
    public Bitmap blured;
    public boolean artworkNotFound = false;
    public int songId;
    public int playlistId;
    public singleSongData(String title, String album, String artist, int duration, String path,int songId){
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
        this.songId = songId;
    }
}
