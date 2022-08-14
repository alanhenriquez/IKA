package com.ikalogic.ika;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.TaskStackBuilder;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentTransaction;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ikalogic.ika.exceptions.InitActivityScreenOrientPortrait;
import com.ikalogic.ika.helpers.DayNightMode;
import com.ikalogic.ika.helpers.DetectConection;
import com.ikalogic.ika.helpers.GetDataUser;
import com.ikalogic.ika.helpers.PermissionGD;
import com.ikalogic.ika.specials.Notify;

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

    private final static String CHANNEL_ID = "NOTIFICATION";
    private final static int NOTIFICATION_ID = 0;
    private PendingIntent pendingIntent;





    /*-------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        InitActivityScreenOrientPortrait.build(this);




        userAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        /*awesomeValidation.addValidation(this,R.id.txtEmailLog, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);
        awesomeValidation.addValidation(this,R.id.txtPasswordLog,".{6,}",R.string.invalid_password);*/






        DetectConection.build(getApplicationContext()).networkStateReceiver();
        if (Build.VERSION.SDK_INT >= 21){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorFooterMain));
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorHeaderMain));

        }


        PermissionGD.InicialPrecess
                .build(getApplicationContext(),this)
                .check(PermissionGD.Permissions.WRITE_EXTERNAL_STORAGE,PermissionGD.Permissions.WRITE_EXTERNAL_STORAGE_CODE);
        /*Botones y acciones*/
        fragmentUserHome = new FragmentUserHome();
        fragmentFeed = new FragmentFeed();
        fragmentShop = new FragmentShop();
        getSupportFragmentManager().beginTransaction().add(R.id.Fragments,fragmentUserHome).commit();//Primera fragment a mostrar
        getData();

    }

    /*-------------------------------------------------------------------------------*/


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        View footer = findViewById(R.id.footer);
        View icon1 = findViewById(R.id.footerHomeIcon);
        View icon2 = findViewById(R.id.footerStoreIcon);
        View icon3 = findViewById(R.id.footerAddIcon);
        View icon4 = findViewById(R.id.footerMenuIcon);
        View icon5 = findViewById(R.id.footerImgPhotoUser);

        Drawable icon1Tint = icon1.getBackground();
        icon1Tint = DrawableCompat.wrap(icon1Tint);

        Drawable icon2Tint = icon2.getBackground();
        icon2Tint = DrawableCompat.wrap(icon2Tint);

        Drawable icon3Tint = icon3.getBackground();
        icon3Tint = DrawableCompat.wrap(icon3Tint);

        Drawable icon4Tint = icon4.getBackground();
        icon4Tint = DrawableCompat.wrap(icon4Tint);

        Drawable icon5Tint = icon5.getBackground();
        icon5Tint = DrawableCompat.wrap(icon5Tint);




        switch (String.valueOf((DayNightMode.build(getApplicationContext()).isDayMode()))){
            case "true":
                //DIA
                if (Build.VERSION.SDK_INT >= 21){
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.colorFooterMain));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorHeaderMain));
                    getWindow().setColorMode(getResources().getColor(R.color.black));
                }
                DrawableCompat.setTint(icon1Tint,getResources().getColor(R.color.colorTextMain));
                DrawableCompat.setTint(icon2Tint,getResources().getColor(R.color.colorTextMain));
                DrawableCompat.setTint(icon3Tint,getResources().getColor(R.color.colorTextMain));
                DrawableCompat.setTint(icon4Tint,getResources().getColor(R.color.colorTextMain));
                DrawableCompat.setTint(icon5Tint,getResources().getColor(R.color.colorTextMain));
                footer.setBackgroundColor(getResources().getColor(R.color.colorFooterMain));
                icon1.setBackground(icon1Tint);
                icon2.setBackground(icon2Tint);
                icon3.setBackground(icon3Tint);
                icon4.setBackground(icon4Tint);
                icon5.setBackground(icon5Tint);
                msgToast("dia");
                break;
            case "false":
                //NOCHE
                if (Build.VERSION.SDK_INT >= 21){
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().setNavigationBarColor(getResources().getColor(R.color.nightColorBacground4));
                    getWindow().setStatusBarColor(getResources().getColor(R.color.nightColorBacground4));
                }
                DrawableCompat.setTint(icon1Tint,getResources().getColor(R.color.nightColorText1));
                DrawableCompat.setTint(icon2Tint,getResources().getColor(R.color.nightColorText1));
                DrawableCompat.setTint(icon3Tint,getResources().getColor(R.color.nightColorText1));
                DrawableCompat.setTint(icon4Tint,getResources().getColor(R.color.nightColorText1));
                DrawableCompat.setTint(icon5Tint,getResources().getColor(R.color.nightColorText1));
                footer.setBackgroundColor(getResources().getColor(R.color.nightColorBacground4));
                icon1.setBackground(icon1Tint);
                icon2.setBackground(icon2Tint);
                icon3.setBackground(icon3Tint);
                icon4.setBackground(icon4Tint);
                icon5.setBackground(icon5Tint);
                msgToast("noche");
                break;
        }



    }



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
    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.feedButton:
                transaction.replace(R.id.Fragments,fragmentFeed);
                break;
            case R.id.userHomeButton:
                transaction.replace(R.id.Fragments,fragmentUserHome);
                CreateNotificationChannel();
                CreateNotification();
                break;
            case R.id.storeButton:
                transaction.replace(R.id.Fragments,fragmentShop);
                break;
        }
        transaction.commit();
    }

    private void CreateNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notification";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void CreateNotification(){


        Intent intent = new Intent(this,UserHome.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(UserHome.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);


        // Example
        Notify.build(getApplicationContext())
                .setTitle("Jill Zhao")
                .setContent("Hi! So I meet you today?")
                .setSmallIcon(R.drawable.iconConfiguracion)
                .setLargeIcon(GetDataUser.loadOnImageString())
                .largeCircularIcon()
                .setPicture("https://p2.piqsels.com/preview/752/273/265/bay-birds-blue-bridge.jpg")
                .setColor(R.color.azul9).setAction(intent).setAutoCancel(true)
                .show();

    }





}

