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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {
    TextView tempV;
    EditText edPassword, edMail;
    FirebaseAuth userAuth;
    View btLogIn, btShowPass, btResetText;
    String email, password;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userAuth = FirebaseAuth.getInstance();

        btLogIn = findViewById(R.id.btnLoginUser);
        btLogIn.setOnClickListener(v -> {
            tempV = findViewById(R.id.txtEmailLog);
            email = tempV.getText().toString();

            tempV = findViewById(R.id.txtPasswordLog);
            password = tempV.getText().toString();

            if(!email.isEmpty() && !password.isEmpty()){
                login();
            }else{
                if(email.isEmpty() && !password.isEmpty()){
                    msgToast("Ingrese un correo electronico");
                }else if (!email.isEmpty()){
                    msgToast("Ingrese la contraseña");
                }else {
                    msgToast("Rellene los campos");
                }
            }

        });

        View signUp = findViewById(R.id.btnSingupLogin);
        signUp.setOnClickListener(view -> {

            Intent ir = new Intent(getApplicationContext(), SignUp.class);
            startActivity(ir);
        });

        edPassword = findViewById(R.id.txtPasswordLog);
        edMail = findViewById(R.id.txtEmailLog);
        btShowPass = findViewById(R.id.showPassword);
        btResetText = findViewById(R.id.resetText);
        ShowPassword(btShowPass,edPassword);
        ResetText(btResetText,edMail);

    }


    private void login (){
        userAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    LogInComplete();
                    msgToast("Acesso correcto");
                    finish();
                }else{
                    msgToast("Ingrese los datos correctos");
                }
            }
        });
    }

    private void LogInComplete(){
        Intent loged = new Intent(getApplicationContext(), UserHome.class);
        startActivity(loged);
    }

    private void ShowPassword (View elemTouch, EditText passwordToShow){
        elemTouch.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        passwordToShow.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        passwordToShow.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });
    }

    private void ResetText (View elemTouch, EditText textToReset){
        elemTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToReset.setText("");
            }
        });
    }

    private void msgToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }


}