package com.reaz.xplayer.helpers;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class Drawer {
    private static DrawerLayout drawerLayout;
    public Drawer(){

    }
    public Drawer(DrawerLayout layout){
        drawerLayout = layout;
    }

    public void open(){
        if(!drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
    public void close(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public boolean isOpen(){
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

}
