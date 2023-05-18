package com.reaz.xplayer.interfaces;

public interface OnDatabaseChange {
    default void onMusicDbChange(){}
    default void onPlaylistChange(){}
    default void onDatabaseChange(){}
    default void onPlaylistSongUpdate(){}

}
