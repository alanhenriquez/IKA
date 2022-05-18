package com.example.ika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //CODIGO DEL MENU
    @Override
    public boolean onCreateOptionsMenu(Menu mimenu){
        // al método inflate() le pasamos el nombre del menú
        getMenuInflater().inflate(R.menu.menu_principal, mimenu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem opcion_menu){
        // obtiene el Item del menú que se presionó
        int id = opcion_menu.getItemId();

        // -Determinamos a quien corresponde la opción.
        // y en base a ello trabajamos la accion
        //
        // -Respecto al cambio de activity esta trabaja en base
        // a las clases de las activity.

        if(id == R.id.optionIntegrantes){
            Intent ir = new Intent(getApplicationContext(), Informacion.class);
            startActivity(ir);
        }
        if(id == R.id.optionInformacion){
            Intent ir = new Intent(getApplicationContext(), VersionApp.class);
            startActivity(ir);
        }
        return super.onOptionsItemSelected(opcion_menu);

    }
    //---TERMINA CODIGO DEL MENU-----------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View bVerduras = findViewById(R.id.contVerdurasyFrutas);
        View bBebidas = findViewById(R.id.contBebidas);
        View bLacteos = findViewById(R.id.contLacteos);
        View bPan = findViewById(R.id.contPan);

        bVerduras.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent ir = new Intent(getApplicationContext(), FrutasVerduras.class);
                startActivity(ir);
            }

        });

        bBebidas.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent ir = new Intent(getApplicationContext(), Bebidas.class);
                startActivity(ir);
            }

        });

        bLacteos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent ir = new Intent(getApplicationContext(), Lacteos.class);
                startActivity(ir);
            }

        });

        bPan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent ir = new Intent(getApplicationContext(), Pan.class);
                startActivity(ir);
            }

        });





    }
}