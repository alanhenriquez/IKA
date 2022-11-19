package com.ikalogic.ika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ikalogic.ika.helpers.ChangeActivity;
import com.ikalogic.ika.helpers.CloseSesion;
import com.ikalogic.ika.helpers.msgToast;

public class Configuracion extends AppCompatActivity {
    /*-------------------------------------------------------------------------------*/
    /*Variables para texto, campos de texto y contenedores*/
    View goBack,logOut,adminCount, editCount;





    /*-------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        goBack = findViewById(R.id.goBack);
        logOut = findViewById(R.id.sesionContent1);
        adminCount = findViewById(R.id.cuentaContent1);
        editCount = findViewById(R.id.cuentaContent2);



        /*Botones y acciones*/
        goBack.setOnClickListener(view -> {
            ChangeActivity.build(getApplicationContext(),UserHome.class).start();
        });
        logOut.setOnClickListener(view -> {
            CloseSesion.build(getApplicationContext()).firebaseClose(Login.class);
        });/*Registrarse si no tienes cuenta*/
        adminCount.setOnClickListener(view -> {
            ChangeActivity.build(getApplicationContext(),ConfigAdministrarCuenta.class).start();
        });
        editCount.setOnClickListener(view -> {
            ChangeActivity.build(getApplicationContext(),EditProfile.class).start();
        });

    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        ChangeActivity.build(getApplicationContext(),UserHome.class).start();
    }
    /*-------------------------------------------------------------------------------*/










    /*-------------------------------------------------------------------------------*/

}