package com.ikalogic.ika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;


public class UserHome extends AppCompatActivity {

    /*-------------------------------------------------------------------------------*/
    /*Variables para texto, campos de texto y contenedores*/
    TextView signUp;
    TextView userName;
    TextView userNameUser;
    TextView userMail;

    /*Acceso a Firebase y AwesomeValidation*/
    AwesomeValidation awesomeValidation;
    FirebaseAuth userAuth;
    DatabaseReference userDataBase;





    /*-------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        /* Acceso a Instancias FireBase y a la AwesomeValidacion
         * Estos accesos los encontraras en el build.gradle tanto de proyecto como app*/
        userAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        /*awesomeValidation.addValidation(this,R.id.txtEmailLog, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.txtPasswordLog,".{6,}",R.string.invalid_password);*/

        getData();

        signUp = findViewById(R.id.cerrarSesion);
        signUp.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            CerrarSesion();
        });/*Registrarse si no tienes cuenta*/

    }
    /*-------------------------------------------------------------------------------*/





    /*Obtenemos la informacion del usuario (nombre, correo)*/
    private void getData (){
        String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        userDataBase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    /*-----------------*/
                    String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    userName = findViewById(R.id.userName);
                    userNameUser = findViewById(R.id.userNameHeader);
                    userName.setText(name);
                    userName.setTypeface(null, Typeface.BOLD);
                    userNameUser.setText(name.toLowerCase(Locale.ROOT).trim());
                    userNameUser.setTypeface(null, Typeface.BOLD);

                    /*-----------------*/
                    String email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    userMail = findViewById(R.id.userMail);
                    userMail.setText(email);

                }else{
                    msgToast("Referencia nula");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*Cerramos la sesion y volvemos al login*/
    private void CerrarSesion (){
        Intent loged = new Intent(getApplicationContext(), Login.class);
        loged.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loged);
        finish();
    }

    /*Variable para generar el mensaje Toast*/
    private void msgToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }

}

