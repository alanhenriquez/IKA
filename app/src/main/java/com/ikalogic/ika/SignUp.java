package com.ikalogic.ika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText etSignUpName;
    private EditText etSignUpMail;
    private EditText etSignUpPassword;
    private Button btSignUp;

    /*Variables de los datos a registrar*/
    private String name = "";
    private String mail = "";
    private String password = "";
    FirebaseAuth userAuth;
    DatabaseReference userDataBase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference();
        etSignUpName = (EditText) findViewById(R.id.etSignUpName);
        etSignUpMail = (EditText) findViewById(R.id.etSignUpMail);
        etSignUpPassword = (EditText) findViewById(R.id.etSignUpPassword);
        btSignUp = (Button) findViewById(R.id.btSignUp);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etSignUpName.getText().toString();
                mail = etSignUpMail.getText().toString();
                password = etSignUpPassword.getText().toString();

                if (!name.isEmpty() && !mail.isEmpty() && !password.isEmpty()){
                    registerUser();
                    msgToast("Registro realizado");
                }
                else {
                    msgToast("Complete el registro");
                }

            }
        });
    }

    private void registerUser(){
        //Autenticaremos al usuario mediante su correo y contraseña
        userAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){

                    Map<String, Object> data = new HashMap<>();
                    data.put("email", mail);
                    data.put("password", password);
                    data.put("name", name);

                    String id = userAuth.getCurrentUser().getUid();
                    userDataBase.child("Users").child(id).setValue(data).addOnCompleteListener(task1 -> {
                        finish();
                        SignUpComplete();
                        msgToast("Usuario creado con exito");

                    });

                }else{
                    msgToast("No es posible crear usuario");
                }
            }
        });


    }

    private void SignUpComplete(){
        Intent finished = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(finished);
    }

    private void msgToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }

}