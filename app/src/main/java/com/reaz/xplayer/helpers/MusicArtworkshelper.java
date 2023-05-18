package com.reaz.xplayer.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.reaz.xplayer.dataase.MusicDB;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MusicArtworkshelper {
    public static Map<Integer, Bitmap> artworks = new HashMap<>();
    private utils helper = new utils();
    private MusicDB musicDB = new MusicDB();


    public Bitmap retriveCompressedMusicArtwork(int songid) {
        if(artworks.containsKey(songid)) return artworks.get(songid);
        String path = musicDB.getMusicPathBuId(songid);
        File file = new File(path);
        if(!file.exists()) {
            Log.d("file", path);
            return null;
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try{
            retriever.setDataSource(path);
            byte[] artwork = retriever.getEmbeddedPicture();
            if(artwork==null){
                throw new IOException("artwork is null");
            }
            retriever.release();
            Bitmap artworkbitmap = compressArtwork(BitmapFactory.decodeByteArray(artwork, 0, artwork.length));
            artworks.put(songid, artworkbitmap);
            return artworkbitmap;
        }catch (IOException e){
            return null;
        }
    }

    public Bitmap retriveMusicArtwork(int songid) {
        String path = musicDB.getMusicPathBuId(songid);
        File file = new File(path);
        if(!file.exists()) {
            Log.d("file", path);
            return null;
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try{
            retriever.setDataSource(path);
            byte[] artwork = retriever.getEmbeddedPicture();
            if(artwork==null){
                throw new IOException("artwork is null");
            }
            retriever.release();
            Bitmap ret= BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            return ret;
        }catch (IOException e){
            return null;
        }
    }

    public Bitmap compressArtwork(Bitmap originalBitmap){
        int desiredWidth = helper.dpToPx(40);
        int desiredHeight = helper.dpToPx(20);
        float aspectRatio = (float) originalBitmap.getWidth() / originalBitmap.getHeight();
        int newWidth, newHeight;
        if (aspectRatio > 1) {
            newWidth = desiredWidth;
            newHeight = (int) (desiredWidth / aspectRatio);
        } else {
            newWidth = (int) (desiredHeight * aspectRatio);
            newHeight = desiredHeight;
        }
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
    }

    public Bitmap retriveBlured(Bitmap originalBitmap){
        int desiredWidth = helper.dpToPx(1);
        int desiredHeight = helper.dpToPx(1);
        float aspectRatio = (float) originalBitmap.getWidth() / originalBitmap.getHeight();
        int newWidth, newHeight;
        if (aspectRatio > 1) {
            newWidth = desiredWidth;
            newHeight = (int) (desiredWidth / aspectRatio);
        } else {
            newWidth = (int) (desiredHeight * aspectRatio);
            newHeight = desiredHeight;
        }
        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
    }

}
