package com.ikalogic.ika;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ikalogic.ika.helpers.ChangeActivity;

public class ConfigAdministrarCuenta extends AppCompatActivity {
    View goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_administrar_cuenta);
        goBack = findViewById(R.id.goBack);

        goBack.setOnClickListener(view -> {
            ChangeActivity.build(getApplicationContext(),Configuracion.class).start();
        });

    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        ChangeActivity.build(getApplicationContext(),Configuracion.class).start();
    }
}