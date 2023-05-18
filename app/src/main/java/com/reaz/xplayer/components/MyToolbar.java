package com.reaz.xplayer.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.reaz.xplayer.R;
import com.reaz.xplayer.helpers.Drawer;
import com.reaz.xplayer.helpers.navigation;
import com.reaz.xplayer.helpers.utils;

import java.util.zip.Inflater;

public class MyToolbar extends LinearLayout {
    private LinearLayout parent;
    private Context ctx;
    private navigation _navigation = new navigation();
    private String __title="hello";
    private Drawable __navigationIcon;
    private Drawer drawer = new Drawer();
    public MyToolbar(Context context, String title, Drawable drawable){
        super(context);
        ctx = context;
        __title = title;
        __navigationIcon = drawable;
        init(null);
    }
    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        init(attrs);
    }
    private void init(AttributeSet attributeSet){
        LayoutInflater inflater = LayoutInflater.from(ctx);
        parent = (LinearLayout) inflater.inflate(R.layout.drawer_toolbar, this, true);
        TextView title = (TextView) parent.findViewById(R.id.titletext);
        ImageView _navigationIcon = (ImageView) findViewById(R.id.menubutton);
        if(attributeSet!=null){
            Log.d("hello ", "woro");
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.MyToolbar);
            String _title = typedArray.getString(R.styleable.MyToolbar_title);
            Drawable navigationIcon =(Drawable) typedArray.getDrawable(R.styleable.MyToolbar_navigationIcon);
            title.setText(_title);
            _navigationIcon.setImageDrawable(navigationIcon);
            typedArray.recycle();
        }
        else{
            Log.d("hello ", __title);
            if(__title!=null){
                title.setText(__title);
            }
            if(__navigationIcon!=null){
                _navigationIcon.setImageDrawable(__navigationIcon);
            }
        }

        _navigationIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_navigation.atScreen()=="homescreen"){
                    drawer.open();
                }
                else{
                    _navigation.goBack();
                }
            }
        });
    }

    public void setTitle(String name){
        __title = name;
        Log.d("hello", __title);
    }
    public void setNavigationIcon(Drawable drawable){
        __navigationIcon = drawable;
    }

}
