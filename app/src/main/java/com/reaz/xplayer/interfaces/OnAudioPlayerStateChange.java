package com.reaz.xplayer.interfaces;

import com.reaz.xplayer.helpers.singleSongData;

public interface OnAudioPlayerStateChange {
    default void onMusicChange(singleSongData song){}
    default void onMusicProgress( int currentposition, int duration){}
    default void onMusicStateChange(String status){}

}
