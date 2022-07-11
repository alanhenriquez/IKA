package com.ikalogic.ika.exceptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

@SuppressLint("SourceLockedOrientationActivity")
public class InitActivityScreenOrientPortrait {
    /*Ejemplo de uso:
    * InitActivityScreenOrientPortrait.build(this)*/


    @SuppressLint("StaticFieldLeak")
    static Activity contexto;
    // Evite las rotaciones de pantalla (utilice la configuración android:screenOrientation de los manifiestos)
    // Establézcalo en nosensor o portrait

    public static void build(Activity context) {
        contexto = context;
        // Establecer ventana a pantalla completa
        contexto.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics metrics = new DisplayMetrics();
        contexto.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Prueba si es VISUAL en modo retrato simplemente comprobando su tamaño
        boolean bIsVisualPortrait = ( metrics.heightPixels >= metrics.widthPixels );

        if( !bIsVisualPortrait ) {
            // Cambie la orientación para que coincida con el modo de Portrait
            if( contexto.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ) {
                contexto.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            else {
                contexto.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
                //En caso de estar en LandScape cambiara a Portrait
            }
        } else {
            contexto.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            //En caso de estar en Portrait se mantendra
        }
    }


}
