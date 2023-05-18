package com.reaz.xplayer.helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.reaz.xplayer.screens.Home;
import com.reaz.xplayer.screens.Playlistscreen;
import com.reaz.xplayer.screens.SongsByList;

import java.util.Map;
import java.util.Stack;

public class navigation {
    private static Context ctx;
    private static Stack<String> history = new Stack<>();
    private static FragmentManager fragmentManager;
    private static Map<String,String> toolbarTitle;
    private static int layoutid;
    Fragment gg;
    public navigation(){

    }
    public navigation(FragmentManager fm, Context context, int layoutid){
        fragmentManager = fm;
        ctx = context;
        this.layoutid = layoutid;
        gg = new SongsByList();
    }

    public void navigate(String name){
        history.push(name);
        Fragment f;
        if(name=="homescreen"){
            f = new Home();
        }
        else if(name=="playlistscreen"){
            f = new Playlistscreen();
        }
        else if(name=="allsongscreen"){
            f = new SongsByList();
        }
        else if(name.substring(0, 9).equals("playlist.")){
            SongsByList songsByList = new SongsByList();
            songsByList.setPlaylistname(name.substring(9));
            f = (Fragment) songsByList;
        }
        else{
            history.pop();
            Toast.makeText(ctx, "Screen not found", Toast.LENGTH_SHORT).show();
            return ;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(layoutid,f);
        transaction.commit();
    }
    public String atScreen(){
        if(history.empty()) return "Library";
        return history.peek();
    }
    public boolean goBack(){
        if(history.size()==1){
            return false;
        }
        history.pop();
        String name = history.peek();
        Fragment f;
        if(name=="homescreen"){
            f = new Home();
        }
        else if(name=="playlistscreen"){
            f = new Playlistscreen();
        }
        else if(name=="allsongscreen"){
            f = gg;
        }
        else if(name.substring(0, 9).equals("playlist.")){
            SongsByList songsByList = new SongsByList();
            songsByList.setPlaylistname(name.substring(9));
            f = (Fragment) songsByList;
        }
        else{
            history.pop();
            Toast.makeText(ctx, "Screen not found", Toast.LENGTH_SHORT).show();
            return false;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(layoutid,f);
        transaction.commit();
        return true;
    }

}
