package com.ikalogic.ika.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ikalogic.ika.R;

import java.util.Objects;

public class GetDataUser {
    static String val, url;

    public static void loadOn(int option,  @NonNull TextView context ){
        String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    switch (option){
                        case 1:
                            /*-----------------*/
                            if (snapshot.hasChild("PerfilData")){
                                val = Objects.requireNonNull(snapshot.child("PerfilData").child("user").getValue()).toString();
                                context.setText(val);
                            }else {
                                context.setText("vacio");
                            }
                            break;
                        case 2:
                            /*-----------------*/
                            if (snapshot.hasChild("PerfilData")){
                                val = Objects.requireNonNull(snapshot.child("PerfilData").child("userName").getValue()).toString();
                                context.setText(val);
                            }else {
                                context.setText("vacio");
                            }

                            break;
                        case 3:
                            /*-----------------*/
                            if (snapshot.hasChild("PerfilData")){
                                val = Objects.requireNonNull(snapshot.child("PerfilData").child("userBiografia").getValue()).toString();
                                context.setText(val);
                            }else {
                                context.setText("vacio");
                            }
                            break;
                        case 4:
                            /*-----------------*/
                            if (snapshot.hasChild("CountData")){
                                val = Objects.requireNonNull(snapshot.child("CountData").child("userPassword").getValue()).toString();
                                context.setText(val);
                            }else {
                                context.setText("vacio");
                            }

                            break;
                        case 5:
                            /*-----------------*/
                            if (snapshot.hasChild("ImageData")){
                                val = Objects.requireNonNull(snapshot.child("ImageData").child("imgPerfil").child("ImageMain").getValue()).toString();
                                context.setText(val);
                            }else {
                                context.setText("vacio");
                            }
                            break;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void loadOn(int option,  @NonNull EditText context ){
        String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    switch (option){
                        case 1:
                            /*-----------------*/
                            if (snapshot.hasChild("PerfilData")){
                                val = Objects.requireNonNull(snapshot.child("PerfilData").child("user").getValue()).toString();
                                context.setText(val);
                            }else {
                                context.setText("vacio");
                            }
                            break;
                        case 2:
                            /*-----------------*/
                            if (snapshot.hasChild("PerfilData")){
                                val = Objects.requireNonNull(snapshot.child("PerfilData").child("userName").getValue()).toString();
                                context.setText(val);
                            }else {
                                context.setText("vacio");
                            }

                            break;
                        case 3:
                            /*-----------------*/
                            if (snapshot.hasChild("PerfilData")){
                                val = Objects.requireNonNull(snapshot.child("PerfilData").child("userBiografia").getValue()).toString();
                                context.setText(val);
                            }else {
                                context.setText("vacio");
                            }
                            break;
                        case 4:
                            /*-----------------*/
                            if (snapshot.hasChild("CountData")){
                                val = Objects.requireNonNull(snapshot.child("CountData").child("userPassword").getValue()).toString();
                                context.setText(val);
                            }else {
                                context.setText("vacio");
                            }

                            break;
                        case 5:
                            /*-----------------*/
                            if (snapshot.hasChild("ImageData")){
                                val = Objects.requireNonNull(snapshot.child("ImageData").child("imgPerfil").child("ImageMain").getValue()).toString();
                                context.setText(val);
                            }else {
                                context.setText("vacio");
                            }
                            break;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void loadOnImage(int option, Context contextApp,@NonNull ImageView context){
        String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    if (option == 1) {
                        /*-----------------*/
                        if (snapshot.hasChild("ImageData")) {
                            val = Objects.requireNonNull(snapshot.child("ImageData").child("imgPerfil").child("ImageMain").getValue()).toString();
                            Glide.with(contextApp).load(val).into(context);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String loadOnImageString(){
        String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                   url = Objects.requireNonNull(snapshot.child("ImageData").child("imgPerfil").child("ImageMain").getValue()).toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return url;
    }


}
