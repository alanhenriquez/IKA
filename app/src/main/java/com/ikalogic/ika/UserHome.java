package com.ikalogic.ika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;
import java.util.Objects;


public class UserHome extends AppCompatActivity
implements
        FragmentUserHome.OnFragmentInteractionListener,
        FragmentFeed.OnFragmentInteractionListener,
        FragmentShop.OnFragmentInteractionListener{

    /*-------------------------------------------------------------------------------*/
    /*Variables para texto, campos de texto y contenedores*/
    TextView signUp;

    FragmentUserHome fragmentUserHome;
    FragmentFeed fragmentFeed;
    FragmentShop fragmentShop;

    /*Acceso a Firebase y AwesomeValidation*/
    AwesomeValidation awesomeValidation;
    FirebaseAuth userAuth;
    DatabaseReference userDataBase;





    /*-------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);


        userAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        /*awesomeValidation.addValidation(this,R.id.txtEmailLog, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.txtPasswordLog,".{6,}",R.string.invalid_password);*/






        /*Botones y acciones*/
        fragmentUserHome = new FragmentUserHome();
        fragmentFeed = new FragmentFeed();
        fragmentShop = new FragmentShop();
        getSupportFragmentManager().beginTransaction().add(R.id.Fragments,fragmentUserHome).commit();//Primera fragment a mostrar
        getData();


    }

    /*-------------------------------------------------------------------------------*/




    private void getData (){
        String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        userDataBase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String val;


                    /*-----------------*/
                    val = Objects.requireNonNull(snapshot.child("ImageData").child("imgPerfil").child("ImageMain").getValue()).toString();
                    ImageView userImageProfile = findViewById(R.id.footerImgPhotoUser);
                    Glide.with(getApplicationContext()).load(val).into(userImageProfile);




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



    /*Variable para generar el mensaje Toast*/
    private void msgToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }


    /*Metodo para crear la accion Onclick del los botones del footer.
    * Esto se encuentra iniciado dentro de cada boton en el xml*/
    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.feedButton:
                transaction.replace(R.id.Fragments,fragmentFeed);
                break;
            case R.id.userHomeButton:
                transaction.replace(R.id.Fragments,fragmentUserHome);
                break;
            case R.id.storeButton:
                transaction.replace(R.id.Fragments,fragmentShop);
                break;
        }
        transaction.commit();
    }


}

