package com.ikalogic.ika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    /*-------------------------------------------------------------------------------*/
    /*Variables*/
    private String email;
    EditText etMailRecoverPassword;
    View btSendEmail;

    /*Acceso a AwesomeValidation*/
    AwesomeValidation awesomeValidation;





    /*-------------------------------------------------------------------------------*/
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        /* Acceso para AwesomeValidacion
         * Estos accesos los encontraras en el build.gradle tanto de proyecto como app*/
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        /*awesomeValidation.addValidation(this,R.id.txtEmailForgotPassword, Patterns.EMAIL_ADDRESS,R.string.invalid_mail);*/





        /*Simples variables definidas accediendo a los id*/
        View btResetText = findViewById(R.id.resetText);
        etMailRecoverPassword = findViewById(R.id.txtEmailForgotPassword);
        btSendEmail = findViewById(R.id.recoverForgotPassword);





        /*Botones y acciones*/
        btSendEmail.setOnClickListener(v -> {
            if (ValidarEmail(etMailRecoverPassword)){
                email = etMailRecoverPassword.getText().toString();

                if(!email.isEmpty()){
                    SendEmail(email);
                }else {
                    msgToast("Rellene los campos");
                }
            }
        });/*Boton para enviar el mensaje de recuperar*/
        ResetText(btResetText, etMailRecoverPassword);/*Reiniciar texto*/

    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        sendEmailExitoso();
    }
    /*-------------------------------------------------------------------------------*/





    /*Enviamos el mensaje al gmail*/
    public void SendEmail(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                msgToast("Mensaje de recuperacion enviado a: " + email);
                sendEmailExitoso();
            }else {
                String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                dameToastdeerrorEmail(errorCode, etMailRecoverPassword);
            }
        });
    }

    /*Envio de mensaje exitoso del y regreso al login*/
    private void sendEmailExitoso (){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /*Reiniciamos el texto del campo de texto*/
    private void ResetText (View elemTouch, EditText textToReset){
        elemTouch.setOnClickListener(view -> textToReset.setText(""));
    }

    /*Variable para generar el mensaje Toast*/
    private void msgToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }

    /*Variable para generar el mensaje Toast del tipo de error*/
    private void dameToastdeerrorEmail(String error, EditText mail) {

        switch (error) {

            case "ERROR_INVALID_CUSTOM_TOKEN":
                msgToast("El formato del token personalizado es incorrecto. Por favor revise la documentación");
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                msgToast("El token personalizado corresponde a una audiencia diferente.");
                break;

            case "ERROR_INVALID_CREDENTIAL":
                msgToast("La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.");
                break;

            case "ERROR_INVALID_EMAIL":
                msgToast("El correo electrónico no es correcto.");
                /*mail.setError("La dirección de correo electrónico está mal formateada.");*/
                mail.requestFocus();
                break;

            case "ERROR_WRONG_PASSWORD":
                msgToast("La contraseña no es correcta");
                /*msgToast("La contraseña no es válida o el usuario no tiene contraseña.");*/
                /*password.setError("la contraseña es incorrecta ");*/
                /*password.requestFocus();*/
                /*password.setText("");*/
                break;

            case "ERROR_USER_MISMATCH":
                msgToast("Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente.");
                break;

            case "ERROR_REQUIRES_RECENT_LOGIN":
                msgToast("Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.");
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                msgToast("Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.");
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                msgToast("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                mail.requestFocus();
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                msgToast("Esta credencial ya está asociada con una cuenta de usuario diferente.");
                break;

            case "ERROR_USER_DISABLED":
                msgToast("La cuenta de usuario ha sido inhabilitada por un administrador.");
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
                msgToast("La credencial del usuario ha expirado. El usuario debe iniciar sesión nuevamente.");
                break;

            case "ERROR_USER_NOT_FOUND":
                msgToast("No hay registro de usuario.");
                break;

            case "ERROR_INVALID_USER_TOKEN":
                msgToast("La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.");
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                msgToast("Esta operación no está permitida. Debes habilitar este servicio en la consola.");
                break;

            case "ERROR_WEAK_PASSWORD":
                msgToast("La contraseña proporcionada no es válida.");
                /*password.setError("La contraseña no es válida, debe tener al menos 6 caracteres");*/
                /*password.requestFocus();*/
                break;

        }

    }

    /*Validar Email*/
    private boolean ValidarEmail(EditText args) {
        // Patrón para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        // El email a validar
        String email = args.getText().toString();
        Matcher mather = pattern.matcher(email);

        if (email.isEmpty()){
            args.requestFocus();
            msgToast("Ingrese un correo electronico");
            return false;
        }else {
            if (mather.find()) {
                /*El email ingresado es válido.*/
                return true;
            } else {
                msgToast("Su email ingresado es inválido.");
                return false;
            }
        }


    }

    /*-------------------------------------------------------------------------------*/
}