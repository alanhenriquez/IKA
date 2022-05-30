package com.ikalogic.ika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Informacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        View versiones = findViewById(R.id.infoTxTopic1);
        View galeria = findViewById(R.id.infoTxTopic2);

        versiones.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent ir = new Intent(getApplicationContext(), versionesDocu.class);
                startActivity(ir);
            }

        });

        galeria.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent ir = new Intent(getApplicationContext(), IMGalery.class);
                startActivity(ir);
            }

        });

    }
}