package com.ikalogic.ika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    /*-------------------------------------------------------------------------------*/
    /*Variables para texto, campos de texto y contenedores*/
    private EditText user;
    private EditText userName;
    private EditText userBiografia;
    private EditText userPassword;
    private String userString = " ";
    private String userNameString = " ";
    private String userBiografiaString = " ";
    private String userPasswordString = " ";
    View showPassword;


    /*Acceso a Firebase y AwesomeValidation*/
    FirebaseAuth userAuth;
    DatabaseReference userDataBase;





    /*-------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);





        /* Acceso a Instancias FireBase y a la AwesomeValidacion
         * Estos accesos los encontraras en el build.gradle tanto de proyecto como app*/
        userAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference();





        /*Simples variables antes definidas accediendo a los id*/
        user = findViewById(R.id.userEditProfile);
        userName = findViewById(R.id.userNameEditProfile);
        userBiografia = findViewById(R.id.userBiografiaEditProfile);
        userPassword = findViewById(R.id.userPasswordEditProfile);
        TextView saveDatosButton = findViewById(R.id.botonGuardarDatosEditProfile);
        TextView savePasswordButton = findViewById(R.id.botonGuardarPasswordEditProfile);
        showPassword = findViewById(R.id.showPassword);





        /*Botones y acciones*/
        getData();/*Carga previa de los datos*/
        ShowPassword(showPassword,userPassword);/*Mostramos la contraseña al presionar*/
        saveDatosButton.setOnClickListener(v ->{

            getString();
            setDataBase();
            Intent intent = new Intent(getApplicationContext(), UserHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        });/*Actualizamos los datos del perfil*/
        savePasswordButton.setOnClickListener(v ->{

            getString();
            setNewPassword();
            Intent intent = new Intent(getApplicationContext(), UserHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        });/*Actualizamos la contraseña*/
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), UserHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    /*-------------------------------------------------------------------------------*/





    /*Convertimos a string el contenido de los campos de texto*/
    private void getString(){
        userString = user.getText().toString();
        userNameString = userName.getText().toString();
        userBiografiaString = userBiografia.getText().toString();
        userPasswordString = userPassword.getText().toString();
    }

    /*Agregamos la informacion a la base de datos*/
    private void setDataBase(){
        Map<String, Object> data = new HashMap<>();
        data.put("user", userString);
        data.put("userName", userNameString);
        data.put("userMail", Objects.requireNonNull(userAuth.getCurrentUser()).getEmail());
        data.put("userPassword", userPasswordString);
        data.put("userBiografiaPerfil", userBiografiaString);

        String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        userDataBase.child("Users").child(id).setValue(data).addOnCompleteListener(task1 -> msgToast("Datos actualizados"));
    }

    /*Modificamos la nueva contraseña y agregamos registro a base de datos*/
    private void setNewPassword(){
        Objects.requireNonNull(userAuth.getCurrentUser()).updatePassword(userPasswordString);

        String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        userDataBase.child("Users").child(id).child("userPassword").setValue(userPasswordString).addOnCompleteListener(task1 -> msgToast("Tu contraseña ha sido actualizada"));
    }

    /*Funcion getData que obtiene los datos desde Firebase base de datos*/
    private void getData (){
        String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        userDataBase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String val;

                    /*-----------------*/
                    val = Objects.requireNonNull(snapshot.child("user").getValue()).toString();
                    user = findViewById(R.id.userEditProfile);
                    user.setText(val);

                    /*-----------------*/
                    val = Objects.requireNonNull(snapshot.child("userName").getValue()).toString();
                    userName = findViewById(R.id.userNameEditProfile);
                    userName.setText(val);

                    /*-----------------*/
                    val = Objects.requireNonNull(snapshot.child("userBiografiaPerfil").getValue()).toString();
                    userBiografia = findViewById(R.id.userBiografiaEditProfile);
                    userBiografia.setText(val);

                    /*-----------------*/
                    val = Objects.requireNonNull(snapshot.child("userPassword").getValue()).toString();
                    userPassword = findViewById(R.id.userPasswordEditProfile);
                    userPassword.setText(val);


                }else {
                    msgToast("Error");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                msgToast("Error de carga");
            }
        });
    }

    /*Mostramos la contraseña del campo de texto*/
    @SuppressLint("ClickableViewAccessibility")
    private void ShowPassword (View elemTouch, EditText passwordToShow){
        elemTouch.setOnTouchListener((v, event) -> {
            switch ( event.getAction() ) {
                case MotionEvent.ACTION_DOWN:
                    passwordToShow.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case MotionEvent.ACTION_UP:
                    passwordToShow.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    break;
            }
            return true;
        });
    }

    /*Variable para generar el mensaje Toast*/
    private void msgToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }




    /*-------------------------------------------------------------------------------*/
}