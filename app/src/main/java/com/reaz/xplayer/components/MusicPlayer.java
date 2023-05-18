package com.reaz.xplayer.components;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.slider.Slider;
import com.reaz.xplayer.R;
import com.reaz.xplayer.services.AudioPlayer;
import com.reaz.xplayer.helpers.MusicArtworkshelper;
import com.reaz.xplayer.helpers.singleSongData;
import com.reaz.xplayer.helpers.utils;
import com.reaz.xplayer.interfaces.OnAudioPlayerStateChange;

public class MusicPlayer extends Fragment {
    private LinearLayout parent;
    private utils util = new utils();
    private float setOnY = 0;
    private ImageView artwork;
    private LinearLayout smallbar;
    private LinearLayout main_content_view;
    private MusicArtworkshelper musicArtworkshelper = new MusicArtworkshelper();
    AudioPlayer audioPlayer = new AudioPlayer();

    public MusicPlayer(LinearLayout layout){
        main_content_view = layout;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parent = (LinearLayout) inflater.inflate(R.layout.music_player, container, false);
        init();
        return parent;
    }
    public void init(){
        parent.post(new Runnable() {
            @Override
            public void run() {
                setOnY = parent.getHeight() - util.dpToPx(60);
                parent.setY(setOnY + util.dpToPx(60));
            }
        });
        smallbar = (LinearLayout) parent.findViewById(R.id.musicplayrsmallpart);
        artwork = (ImageView) parent.findViewById(R.id.musicplayerartwork);
        bottomNavTouchListeners();

        initiateAudioPlayer();

    }


    private void bottomNavTouchListeners(){
        parent.setOnTouchListener(new View.OnTouchListener() {
            float prevRawY, prevY, parentY;
            float prefRaw, prefY;
            String movedto = "";
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        prevY = event.getY();
                        prevRawY = event.getRawY();
                        parentY = parent.getY();
                        movedto = "";
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float next = parent.getY() + event.getY() - prevY;
                        if(next>=0 && next<=setOnY){
                            parent.setY(parent.getY() + event.getY() - prevY);
                        }
                        animateWithToucMove(event);
                        if(prefY>event.getRawY()) movedto = "up";
                        else movedto = "down";
                        prefY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if(movedto=="") return true;
                        animatePlayer(movedto);

                }
                return false;
            }
        });

    }

    public void animateWithToucMove(MotionEvent event){
        float mid = parent.getHeight()/2;
        if(event.getRawY()<mid) return ;
        float height = parent.getHeight() - parent.getY();
        if(height>=mid) return ;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) smallbar.getLayoutParams();
        params.height = (int) height;
        smallbar.setLayoutParams(params);
        float width = util.dpToPx(105) + (parent.getWidth()- util.dpToPx(100)) * (height- util.dpToPx(60)) / (mid- util.dpToPx(60));
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) artwork.getLayoutParams();
        params2.width = (int) width;
        artwork.setLayoutParams(params2);
    }
    public void animatePlayer(String mov){
        float startY, endY;
        if(mov=="up"){
            startY = parent.getY();
            endY = 0;
        }
        else {
            startY = parent.getY();
            endY = setOnY;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(startY, endY);
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                parent.setY(value);
            }
        });
        animator.start();
        float mid = setOnY/2;
        float startHeight, endHeight;
        if(mov=="up"){
            startHeight = smallbar.getHeight();
            endHeight = mid;
        }
        else{
            startHeight = smallbar.getHeight();
            endHeight = util.dpToPx(60);
        }
        ValueAnimator animator1 = ValueAnimator.ofFloat(startHeight, endHeight);
        animator1.setDuration(200);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) smallbar.getLayoutParams();
                params.height = (int) value;
                smallbar.setLayoutParams(params);
                float width = util.dpToPx(100) + (parent.getWidth()- util.dpToPx(100)) * (value- util.dpToPx(60)) / (mid- util.dpToPx(60));
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) artwork.getLayoutParams();
                params2.width = (int) width;
                artwork.setLayoutParams(params2);
            }
        });
        animator1.start();

    }

    private void initiateAudioPlayer(){
        TextView smallplaypause = (TextView) parent.findViewById(R.id.smallplaypausebutton);
        TextView smallnextbutton = (TextView) parent.findViewById(R.id.smallnextbutton);
        TextView smallartistalbum = (TextView) parent.findViewById(R.id.smallartistalbum);
        TextView smallsongtitle = (TextView) parent.findViewById(R.id.smallsongtitle);
        ImageView artwork = (ImageView) parent.findViewById(R.id.musicplayerartwork);
        TextView playertitle = (TextView) parent.findViewById(R.id.playerfullscreentitle);
        TextView fullscreenplaypause = (TextView) parent.findViewById(R.id.playerfullscreenplaypause);
        TextView fullscreenalbuartist = (TextView) parent.findViewById(R.id.playerfullscreenartistandalbum);
        TextView fullscreenduration = (TextView) parent.findViewById(R.id.fullscreenmusicduration);
        TextView fullscreencurrentposition = (TextView) parent.findViewById(R.id.fullscreenmusicposition);
        Slider seekSlider = (Slider) parent.findViewById(R.id.fullscreenmusicslider);
        TextView fullscreepreviousbtn = (TextView) parent.findViewById(R.id.fullscreenmusicprevbutton);
        TextView fullscreenextsbtn = (TextView) parent.findViewById(R.id.fullscreenmusicnextbutton);
        TextView backwordbtn = (TextView) parent.findViewById(R.id.fullscreenmusicbackword);
        TextView forwardbtn = (TextView) parent.findViewById(R.id.fullscreenmusicforward);
        MusicArtworkshelper musicArtworkshelper = new MusicArtworkshelper();
        audioPlayer.setOnAudioPlayerStateChange(new OnAudioPlayerStateChange() {
            @Override
            public void onMusicChange(singleSongData song) {
                smallsongtitle.setText(song.title);
                smallartistalbum.setText(song.album + " - " + song.artist);
                fullscreenalbuartist.setText(song.album + " - " + song.artist);
                playertitle.setText(song.title);
                LoadArtworkTask task = new LoadArtworkTask(artwork, song.songId);
                task.execute();
                fullscreenduration.setText(util.formatSecondsToMinuteString(song.duration/1000));
            }
            @Override
            public void onMusicProgress(int currentposition, int duration) {
                fullscreencurrentposition.setText(String.valueOf(util.formatSecondsToMinuteString(currentposition/1000)));
                float value = (float)currentposition / (float)duration;
                value = (float) Math.min(value, 1.0);
                seekSlider.setValue(value);
            }

            @Override
            public void onMusicStateChange(String status) {
                if(status=="play"){
                    smallplaypause.setText("\uf04c");
                    fullscreenplaypause.setText("\uf04c");
                }
                else if(status=="pause"){
                    smallplaypause.setText("\uf04b");
                    fullscreenplaypause.setText("\uf04b");
                }
                if(status == "play" || status=="pause"){
                       if(parent.getY()>setOnY){
                           ValueAnimator animator = ValueAnimator.ofInt((int)parent.getY(), (int)setOnY);
                           animator.setDuration(200);
                           animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                               @Override
                               public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                                   int value = (int) animation.getAnimatedValue();
                                   parent.setY(value);
                                   RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) main_content_view.getLayoutParams();
                                   params.height = value;
                                   main_content_view.setLayoutParams(params);
                               }
                           });
                           animator.start();
                       }


                }
            }
        });

        Handler handler = new Handler();

        smallplaypause.setOnClickListener(handler);
        smallnextbutton.setOnClickListener(handler);
        fullscreenplaypause.setOnClickListener(handler);
        fullscreenextsbtn.setOnClickListener(handler);
        fullscreepreviousbtn.setOnClickListener(handler);
        backwordbtn.setOnClickListener(handler);
        forwardbtn.setOnClickListener(handler);


        seekSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                audioPlayer.seekTo((float)Math.min(slider.getValue(), 1.0));
            }
        });

    }

    private class LoadArtworkTask extends AsyncTask<Void, Void, Bitmap> {
        ImageView img;
        int songid;
        public LoadArtworkTask(ImageView img, int songid) {
            this.img = img;
            this.songid = songid;
        }
        @Override
        protected Bitmap doInBackground(Void... voids) {
            return musicArtworkshelper.retriveMusicArtwork(songid) ;
        }

        @Override
        protected void onPostExecute(Bitmap artwork) {
            if(artwork!=null){
                Glide.with(getContext()).load(artwork).placeholder(R.drawable.dms).error(R.drawable.dms).into(img);
            }
            else Glide.with(getContext()).load(R.drawable.dms).placeholder(R.drawable.dms).error(R.drawable.dms).into(img);

        }
    }


    class Handler implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.smallplaypausebutton || v.getId()==R.id.playerfullscreenplaypause){
                if(audioPlayer.getStatus()=="pause"){
                    audioPlayer.resume();
                }
                else{
                    audioPlayer.pause();
                }
            }
            if(v.getId()==R.id.smallnextbutton){
                audioPlayer.next();
            }
            if(v.getId()==R.id.fullscreenmusicnextbutton){
                audioPlayer.next();
            }
            if(v.getId()==R.id.fullscreenmusicprevbutton){
                audioPlayer.prev();
            }
            if(v.getId()==R.id.fullscreenmusicforward){
                audioPlayer.seekForward(10);
            }
            if(v.getId()==R.id.fullscreenmusicbackword){
                audioPlayer.seekToBackword(10);
            }

        }
    }





}