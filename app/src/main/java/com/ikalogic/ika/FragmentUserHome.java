package com.ikalogic.ika;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ikalogic.ika.helpers.ChangeActivity;
import com.ikalogic.ika.helpers.DayNightMode;

import java.util.Objects;

public class FragmentUserHome extends Fragment {

    private TextView user, userName, userBiografia, userMail;
    private ImageView userImageProfile;
    View menuBoton, opConfiguracion, opEditProfile, menuFooterOpUser, header, body;
    FirebaseAuth userAuth;
    DatabaseReference userDataBase;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);


        menuBoton = view.findViewById(R.id.iconMenuHeader);
        opConfiguracion = view.findViewById(R.id.configuracionOpMenu);
        opEditProfile = view.findViewById(R.id.editProfileOpMenu);
        menuFooterOpUser = view.findViewById(R.id.contFooterOpUser);
        header = view.findViewById(R.id.header);


        getData(view);
        opConfiguracion.setOnClickListener(v -> {
            ChangeActivity.build(view.getContext(),Configuracion.class);
        });/*Cambiamos a la activity de Configuracion*/
        opEditProfile.setOnClickListener(v -> {
            ChangeActivity.build(view.getContext(),EditProfile.class);
        });/*Cambiamos a la activity de EditProfile*/
        menuFooterOpUser.setVisibility(View.GONE);
        menuBoton.setOnClickListener(v -> {

            TranslateAnimation animationShowUp1 = new TranslateAnimation(0.0f,0.0f,1000.0f,0.0f);
            animationShowUp1.setDuration(350);

            TranslateAnimation animationHideDown1 = new TranslateAnimation(0.0f,0.0f,0.0f,1000.0f);
            animationHideDown1.setDuration(500);

            if (menuFooterOpUser.getVisibility() != View.GONE){
                menuFooterOpUser.setVisibility(View.GONE);
                menuFooterOpUser.startAnimation(animationHideDown1);
            }else {
                menuFooterOpUser.setVisibility(View.VISIBLE);
                menuFooterOpUser.startAnimation(animationShowUp1);
            }

        });


        user = view.findViewById(R.id.userUserHome);
        userName = view.findViewById(R.id.userNameUserHome);
        userBiografia = view.findViewById(R.id.userBiografiaUserHome);
        userMail = view.findViewById(R.id.userMailUserHome);
        userImageProfile = view.findViewById(R.id.imgPhotoUser);
        body = view.findViewById(R.id.main);
        DayNight(view);

        return view;
    }

    /*-------------------------------------------------------------------------------*/


    private void DayNight(View v){

        /*Drawable icon1Tint = menuBoton.getBackground();
        icon1Tint = DrawableCompat.wrap(icon1Tint);

        Drawable icon2Tint = opConfiguracion.getBackground();
        icon2Tint = DrawableCompat.wrap(icon2Tint);

        Drawable icon3Tint = opEditProfile.getBackground();
        icon3Tint = DrawableCompat.wrap(icon3Tint);*/


        switch (String.valueOf((DayNightMode.build(v.getContext()).isDayMode()))){
            case "true":
                //DIA
                /*DrawableCompat.setTint(icon1Tint,getResources().getColor(R.color.colorTextMain));
                DrawableCompat.setTint(icon2Tint,getResources().getColor(R.color.colorTextMain));
                DrawableCompat.setTint(icon3Tint,getResources().getColor(R.color.colorTextMain));*/
                body.setBackgroundColor(getResources().getColor(R.color.colorBackgroundMain));
                header.setBackgroundColor(getResources().getColor(R.color.colorFooterMain));
                /*menuBoton.setBackground(icon1Tint);
                opConfiguracion.setBackground(icon2Tint);
                opEditProfile.setBackground(icon3Tint);*/
                break;
            case "false":
                //NOCHE
                /*DrawableCompat.setTint(icon1Tint,getResources().getColor(R.color.nightColorText1));
                DrawableCompat.setTint(icon2Tint,getResources().getColor(R.color.nightColorText1));
                DrawableCompat.setTint(icon3Tint,getResources().getColor(R.color.nightColorText1));*/
                body.setBackgroundColor(getResources().getColor(R.color.nightColorBacground5));
                header.setBackgroundColor(getResources().getColor(R.color.nightColorBacground4));
                /*menuBoton.setBackground(icon1Tint);
                opConfiguracion.setBackground(icon2Tint);
                opEditProfile.setBackground(icon3Tint);*/
                break;
        }
    }

    /*Funcion getData que obtiene los datos desde Firebase base de datos*/
    private void getData (View v){
        String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        userDataBase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String val;

                    val = Objects.requireNonNull(snapshot.child("PerfilData").child("user").getValue()).toString();
                    user = v.findViewById(R.id.userUserHome);
                    user.setText(val);

                    /*-----------------*/
                    val = Objects.requireNonNull(snapshot.child("PerfilData").child("userName").getValue()).toString();
                    userName = v.findViewById(R.id.userNameUserHome);
                    userName.setText(val);

                    /*-----------------*/
                    val = Objects.requireNonNull(snapshot.child("PerfilData").child("userBiografia").getValue()).toString();
                    userBiografia = v.findViewById(R.id.userBiografiaUserHome);
                    userBiografia.setText(val);

                    /*-----------------*/
                    val = Objects.requireNonNull(snapshot.child("CountData").child("userMail").getValue()).toString();
                    userMail = v.findViewById(R.id.userMailUserHome);
                    userMail.setText(val);

                    /*-----------------*/
                    val = Objects.requireNonNull(snapshot.child("ImageData").child("imgPerfil").child("ImageMain").getValue()).toString();
                    userImageProfile = v.findViewById(R.id.imgPhotoUser);
                    Glide.with(v).load(val).into(userImageProfile);






                }else {
                    msgToast("Error",v);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                msgToast("Error de carga",v);
            }
        });
    }



    /*Importante para importar la interfaz del Fragment. No eliminar*/
    public interface OnFragmentInteractionListener {

    }



    /*Variable para generar el mensaje Toast*/
    private void msgToast(String message, View v) {
        Toast.makeText(v.getContext(),message, Toast.LENGTH_LONG).show();
    }

    /*-------------------------------------------------------------------------------*/
}