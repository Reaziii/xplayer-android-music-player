package com.reaz.xplayer.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reaz.xplayer.R;
import com.reaz.xplayer.components.HomeSingleItem;

public class Home extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        showList();
        return v;
    }
    private void showList(){
        String[][] __list = {{"All Songs","\uf001", "allsongscreen"}, {"Playlist", "\uf03a","playlistscreen"},{"Folder", "\uf07b", "folderscreen"},{"Album", "\uf8d9", "albumscreen"},{"Artists", "\uf7a6", "artistscreen"}};
        for(int i = 0;i<__list.length;i++){
            FragmentTransaction fm = getFragmentManager().beginTransaction();
            HomeSingleItem item = new HomeSingleItem();
            item.setName(__list[i][0]);
            item.setLogo(__list[i][1]);
            item.setPagename(__list[i][2]);
            fm.add(R.id.listitemshomepage, item);
            fm.commit();
        }

    }
}