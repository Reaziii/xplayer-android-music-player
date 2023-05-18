package com.reaz.xplayer.components;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reaz.xplayer.R;
import com.reaz.xplayer.helpers.navigation;

public class HomeSingleItem extends Fragment {
    private String name = "", logo = "", pagename;
    private navigation _navigation = new navigation();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_single_item, container, false);
        TextView _name = (TextView) v.findViewById(R.id.homesingleitemname);
        _name.setText(name);
        TextView _logo = (TextView) v.findViewById(R.id.homesingleitemlogo);
        _logo.setText(logo);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _navigation.navigate(pagename);
            }
        });
        return v;
    }
    public void setName(String name) {this.name = name;};
    public void setLogo(String logo) {this.logo = logo;};
    public void setPagename(String name) {this.pagename = name;};

}