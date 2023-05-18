package com.reaz.xplayer.services;

import android.media.MediaPlayer;
import android.util.Log;

import com.reaz.xplayer.helpers.singleSongData;
import com.reaz.xplayer.interfaces.OnAudioPlayerStateChange;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AudioPlayer {
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private static String status = "stop";
    private static PlaylistUtils playlistUtils = new PlaylistUtils();
    private static ArrayList<OnAudioPlayerStateChange> listeners = new ArrayList<>();
    private static Timer timer = new Timer();
    public static singleSongData current;
    private static int PlaylistId = -1;
    private static boolean repeat = false;
    private static  boolean wait = false;
    private static boolean OnCompleteListenerSetupDone = false, progressTimerOn = false;
    public AudioPlayer(){
        if(!OnCompleteListenerSetupDone){
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(mp.getDuration()<=mp.getCurrentPosition()){
                        next();
                        Log.d("music oncomplete", "true");

                    }
                }
            });
            OnCompleteListenerSetupDone = true;
        }
        if(progressTimerOn==false){
            progressTimer();
            progressTimerOn = true;
        }
    }

    public void set(int songId, int PlaylistId) {
        this.PlaylistId = PlaylistId;
        current = playlistUtils.getSongs(PlaylistId, songId);
    }
    public void next(){
       current = playlistUtils.nextSong(PlaylistId, current.songId);
        play();
    }

    public void prev(){
        current = playlistUtils.prevSong(PlaylistId, current.songId);
        play();
    }

    public String play() {
        if(wait) return "wait";
        wait = true;
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(current.path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            musicChanged();
            status = "play";
            musicStateChange(status);
            wait = false;
            return null;
        }
        catch (Exception e){
            wait = false;
            next();
            return "Can't play this song";
        }
    }

    public void pause(){
        if(status=="play"){
            mediaPlayer.pause();
            status = "pause";
        }
        musicStateChange(status);
    }
    public String getStatus(){
        return status;
    }
    public void resume(){
        if(status=="pause"){
            mediaPlayer.start();
            status = "play";
        }
        musicStateChange(status);
    }
    public void setOnAudioPlayerStateChange(OnAudioPlayerStateChange listener){
        this.listeners.add(listener);
    }

    private void musicChanged(){
        if(current==null) return ;
        Log.d("music change", "true");
        for(OnAudioPlayerStateChange listener : listeners){
            listener.onMusicChange(current);
        }
    }

    private void musicStateChange(String status){
        for(OnAudioPlayerStateChange listener : listeners){
            listener.onMusicStateChange(status);
        }
    }

    private void musicProgressChange(){
        if(status=="stop") return ;
        for(OnAudioPlayerStateChange listener : listeners){
            listener.onMusicProgress(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
        }
    }
    private void progressTimer(){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                musicProgressChange();
            }
        };
        timer.scheduleAtFixedRate(task,0, 1000);
    }
    public void seekTo(float valueon1){
        int v = (int)((float)mediaPlayer.getDuration() * valueon1);
        v = Math.min(mediaPlayer.getDuration(), v);
        v = v-1000;
        mediaPlayer.seekTo(v);
        musicProgressChange();
    }
    public void seekForward(int sec){
        int currentPosition = mediaPlayer.getCurrentPosition();
        currentPosition+=(sec * 1000);
        currentPosition = Math.min(currentPosition, mediaPlayer.getDuration());
        mediaPlayer.seekTo(currentPosition);
        progressTimer();
    }
    public void seekToBackword(int sec){
        int currentPosition = mediaPlayer.getCurrentPosition();
        currentPosition-=(sec * 1000);
        currentPosition = Math.max(currentPosition, 0);
        mediaPlayer.seekTo(currentPosition);
        progressTimer();
    }
}
