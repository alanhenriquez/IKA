package com.ikalogic.ika.helpers;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

public class DayNightMode {
    //Variables-----------------------
    private final Context context;


    //Privates-----------------------------------
    private DayNightMode(Context context){
        this.context = context;
    }


    //Public static------------------------------------
    public static DayNightMode build(Context context){
        return new DayNightMode(context);
    }


    public boolean isDayMode(){
        int nightModeFlags = context.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch(nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                return true;
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                return false;
        }
        return false;
    }

}