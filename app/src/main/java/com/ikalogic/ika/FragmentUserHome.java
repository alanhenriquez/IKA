package com.ikalogic.ika;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUserHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUserHome extends Fragment {

    /*Importantes de usar aqui:
    *
    * - Variables
    * - Iniciar variables en el metodo onCreat
    * - Acceder a los elementos por sus id mediante el metodo onCreateView
    * - Funcion getData que obtiene los datos desde Firebase*/

    /*-------------------------------------------------------------------------------*/
    /* - Variables*/
    /*Variables para texto, campos de texto y contenedores*/
    private TextView user;
    private TextView userName;
    private TextView userBiografia;
    private TextView userMail;
    private ImageView userImageProfile;

    /*Acceso a Firebase y AwesomeValidation*/
    FirebaseAuth userAuth;
    DatabaseReference userDataBase;





    /*-------------------------------------------------------------------------------*/
    /*Si buscas codigo de los elementos lo encontraras en el UserHome.java
    * Aqui solo hay el llamado de la interfaz del fragment*/

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentUserHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentUserHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUserHome newInstance(String param1, String param2) {
        FragmentUserHome fragment = new FragmentUserHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





        /* - Iniciar variables en el metodo onCreat*/
        /* Acceso a Instancias FireBase
         * Estos accesos los encontraras en el build.gradle tanto de proyecto como app*/
        userAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        /* Acceder a los elementos por sus id mediante el metodo onCreateView*/
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        /*Generamos la data y la ponemos en los elementos respecto a sus id*/





        View menuBoton = view.findViewById(R.id.iconMenuHeader);
        View opConfiguracion = view.findViewById(R.id.configuracionOpMenu);
        View opEditProfile = view.findViewById(R.id.editProfileOpMenu);
        View menuFooterOpUser = view.findViewById(R.id.contFooterOpUser);



        getData(view);

        opConfiguracion.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), Configuracion.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });/*Cambiamos a la activity de Configuracion*/
        opEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EditProfile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
        return view;

    }
    /*-------------------------------------------------------------------------------*/


    private void funAnimation(){

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