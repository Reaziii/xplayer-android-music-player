package com.reaz.xplayer.helpers;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;

public class utils {
    private static Context ctx;
    private static WindowManager windowManager;
    private static View v;
    public utils(){}
    public utils(Context context, WindowManager wm, View v){
        ctx = context;
        this.v = v;
        windowManager = wm;
    }
    public int dpToPx(float dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    public int getDisplayHeight(){
        Display display = windowManager. getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d("height rr", String.valueOf(height));
        return height;
    }

    public String escapeString(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '\'':
                    builder.append("''");
                    break;
                case '\"':
                    builder.append("\\\"");
                    break;
                case '\\':
                    builder.append("\\\\");
                    break;
                default:
                    builder.append(c);
                    break;
            }
        }

        return builder.toString();
    }

    public String unescapeString(String str) {
        StringBuilder sb = new StringBuilder();
        boolean escape = false;
        for (char c : str.toCharArray()) {
            if (escape) {
                sb.append(c);
                escape = false;
            } else if (c == '\\') {
                escape = true;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public String formatSecondsToMinuteString(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        // Format the minutes and seconds as two-digit strings
        String minutesString = String.format("%02d", minutes);
        String secondsString = String.format("%02d", remainingSeconds);

        // Concatenate the formatted minutes and seconds with a colon separator
        return minutesString + ":" + secondsString;
    }

    public Bitmap retriveMusicArtwork(String path) {
        File file = new File(path);
        if(!file.exists()) {
            Log.d("file", path);
            return null;
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try{
            Log.d("errorx", path);
            retriever.setDataSource(path);
            byte[] artwork = retriever.getEmbeddedPicture();
            if(artwork==null){
                throw new IOException("artwork is null");
            }
            retriever.release();
            return compressArtwork(BitmapFactory.decodeByteArray(artwork, 0, artwork.length));
        }catch (IOException e){
            Log.e("errorx", "Error checking file path", e);
            return null;
        }

    }

    public Bitmap compressArtwork(Bitmap originalBitmap){
        int desiredWidth = dpToPx(40);
        int desiredHeight = dpToPx(20);
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
