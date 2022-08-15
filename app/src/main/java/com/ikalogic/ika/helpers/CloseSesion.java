package com.ikalogic.ika.helpers;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

public class CloseSesion {
    //Variables-----------------------
    private final Context context;
    private String mensaje = null;


    //Privates-----------------------------------
    private CloseSesion(Context context){
        this.context = context;
    }


    //Public static------------------------------------
    public static CloseSesion build(Context context){
        return new CloseSesion(context);
    }


    //Public CloseSesion-----------------------------
    public String message(String _mensaje){
        mensaje = _mensaje;
        return mensaje;
    }


    //Public void------------------------------------
    public void firebaseClose(Class<?> returnedTo){
        FirebaseAuth.getInstance().signOut();
        ChangeActivity.build(context,returnedTo).start();
        if (mensaje != null){
            msgToast.build(context).message(mensaje);
        }else {
            msgToast.build(context).message("Cerraste sesión");
        }

    }
}
