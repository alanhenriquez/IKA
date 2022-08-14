package com.ikalogic.ika.specials;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.TranslateAnimation;

public class LoaderOffLine {
    Context context;
    View view;

    private LoaderOffLine(Context context){
        this.context = context;
    }

    public static LoaderOffLine build(Context context){return new LoaderOffLine(context);}

    public LoaderOffLine into(View view){
        this.view = view;
        return this;
    }

    public void show(){
        TranslateAnimation animationOffLine = new TranslateAnimation(0.0f,100.0f,0.0f,0.0f);
        animationOffLine.setDuration(3000);
        view.setAnimation(animationOffLine);
    }



}
