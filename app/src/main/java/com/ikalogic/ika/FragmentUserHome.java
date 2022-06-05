package com.ikalogic.ika;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    TextView signUp;
    TextView userName;
    TextView userNameUser;
    TextView userMail;

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





        /* - Acceder a los elementos por sus id mediante el metodo onCreateView*/
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        getData(view);/*Generamos la data y la ponemos en los elementos respecto a sus id*/

        View menuBoton = view.findViewById(R.id.iconMenuHeader);
        View menuFooterOpUser = view.findViewById(R.id.contFooterOpUser);
        menuFooterOpUser.setVisibility(View.GONE);
        menuBoton.setOnClickListener(v -> {


            if (menuFooterOpUser.getVisibility() != View.GONE){
                menuFooterOpUser.setVisibility(View.GONE);
            }else {
                menuFooterOpUser.setVisibility(View.VISIBLE);
            }


        });

        return view;

    }

    /*-------------------------------------------------------------------------------*/




    /* - Funcion getData que obtiene los datos desde Firebase*/
    /*Obtenemos la informacion del usuario (nombre, correo)
     * La id de los demas elementos se encuentran en sus respectivos fragments*/
    private void getData (View v){
        /*la variable "v" nos sirve para poder ingresar a los elementos del fragment mediante sus id*/

        String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        userDataBase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    /*-----------------*/
                    String name = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
                    userName = v.findViewById(R.id.userName);
                    userNameUser = v.findViewById(R.id.userNameHeader);
                    userName.setText(name);
                    userName.setTypeface(null, Typeface.BOLD);
                    userNameUser.setText(name.toLowerCase(Locale.ROOT).trim());
                    userNameUser.setTypeface(null, Typeface.BOLD);

                    /*-----------------*/
                    String email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    userMail = v.findViewById(R.id.userMail);
                    userMail.setText(email);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /*Importante para importar la interfaz del Fragment. No eliminar*/
    public interface OnFragmentInteractionListener {
    }






    /*-------------------------------------------------------------------------------*/
}