package com.ikalogic.ika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ikalogic.ika.exceptions.InitActivityScreenOrientPortrait;
import com.ikalogic.ika.helpers.GetDataUser;

import java.io.IOException;
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

    //ImageView para la imagen de usuario
    Uri imageUri;
    View changeImageUser;//Boton para selecionar imagen
    ImageView contImageUser, userImageProfile;//Contenedor con la imagen del usuario
    int SELECT_PICTURE = 200;// constant to compare the activity result code







    /*Acceso a Firebase y AwesomeValidation*/
    FirebaseAuth userAuth;
    DatabaseReference userDataBase;




    /*-------------------------------------------------------------------------------*/
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        InitActivityScreenOrientPortrait.build(this);


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

        changeImageUser = findViewById(R.id.selectImageEditProfile);
        contImageUser = findViewById(R.id.imgPhotoUserEditProfile);





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
        changeImageUser.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), SelectImage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });/*Elegimos la nueva imagen de usuario*/
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), UserHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    /*-------------------------------------------------------------------------------*/





    /*--------------------*/
    /*Codigo de la seleccion de imagen y envio a la base de datos*/
    private void openGallery(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        // pass the constant to compare it with the returned requestCode
        // noinspection deprecation
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE
                && data != null && data.getData() != null) {

            /*Obtenemos la informacion de la imagen seleccionada*/
            imageUri = data.getData();

            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                contImageUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }


            if (imageUri != null) {

                // Code for showing progressDialog while uploading
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Subiendo...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false); 
                progressDialog.setOnCancelListener(new Dialog.OnCancelListener() {
                    @Override public void onCancel(DialogInterface dialog) {
                        // DO SOME STUFF HERE
                    }
                });

                // Defining the child of storageReference
                String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
                StorageReference folder = FirebaseStorage.getInstance().getReference().child("Users").child(id);
                final StorageReference file_name = folder.child(imageUri.getLastPathSegment());
                file_name.putFile(imageUri).addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Cargando al " + (int)progress + "%");
                }).addOnSuccessListener(taskSnapshot -> file_name.getDownloadUrl().addOnSuccessListener(uri -> {
                    //Enviamos a la base de datos la url de la imagen
                    setDataImageBase(String.valueOf(uri));
                    progressDialog.dismiss();
                    msgToast("Se subio correctamente");
                })).addOnFailureListener(e -> {
                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    msgToast("Error en la carga " + e.getMessage());
                });

            }
        }
    }

    /*Agregamos la Url de la imagen a la base de datos*/
    private void setDataImageBase(String link){
        Map<String, Object> data = new HashMap<>();
        data.put("ImageMain", link);
        String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        userDataBase.child("Users").child(id).child("ImageData").child("imgPerfil").setValue(data).addOnCompleteListener(task1 -> msgToast("Datos actualizados"));

    }
    /*Termina codigo de la seleccion de imagen y envio a la base de datos*/
    /*--------------------*/


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
        data.put("userBiografia", userBiografiaString);

        String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        userDataBase.child("Users").child(id).child("PerfilData").setValue(data).addOnCompleteListener(task1 -> msgToast("Datos actualizados"));
    }



    /*Modificamos la nueva contraseña y agregamos registro a base de datos*/
    private void setNewPassword(){
        Objects.requireNonNull(userAuth.getCurrentUser()).updatePassword(userPasswordString);

        String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        userDataBase.child("Users").child(id).child("CountData").child("userPassword").setValue(userPasswordString).addOnCompleteListener(task1 -> msgToast("Tu contraseña ha sido actualizada"));
    }



    /*Funcion getData que obtiene los datos desde Firebase base de datos*/
    private void getData (){
        user = findViewById(R.id.userEditProfile);
        userName = findViewById(R.id.userNameEditProfile);
        userBiografia = findViewById(R.id.userBiografiaEditProfile);
        userPassword = findViewById(R.id.userPasswordEditProfile);
        userImageProfile = findViewById(R.id.imgPhotoUserEditProfile);

        GetDataUser.loadOn(1,user);
        GetDataUser.loadOn(2,userName);
        GetDataUser.loadOn(3,userBiografia);
        GetDataUser.loadOn(4,userPassword);
        Glide.with(getApplicationContext()).load(GetDataUser.loadOnImageString()).into(userImageProfile);


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