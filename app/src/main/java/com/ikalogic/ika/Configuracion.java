package com.ikalogic.ika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Configuracion extends AppCompatActivity {
    /*-------------------------------------------------------------------------------*/
    /*Variables para texto, campos de texto y contenedores*/
    TextView signUp;





    /*-------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        /*Simples variables definidas accediendo a los id*/
        signUp = findViewById(R.id.cerrarSesion);





        /*Botones y acciones*/
        signUp.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            CerrarSesion();
        });/*Registrarse si no tienes cuenta*/

    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        returnActivity();
    }
    /*-------------------------------------------------------------------------------*/





    /*Cerramos la sesion y volvemos al login*/
    private void CerrarSesion (){
        Intent loged = new Intent(getApplicationContext(), Login.class);
        loged.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loged);
        finish();
    }

    /*Regresar a UserHome*/
    private void returnActivity (){
        Intent intent = new Intent(getApplicationContext(), UserHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /*-------------------------------------------------------------------------------*/

}