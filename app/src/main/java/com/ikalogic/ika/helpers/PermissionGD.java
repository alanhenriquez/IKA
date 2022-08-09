package com.ikalogic.ika.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ikalogic.ika.EditProfile;
import com.ikalogic.ika.R;
import com.ikalogic.ika.SelectImage;
import com.ikalogic.ika.specials.LoaderOffLine;

public class PermissionGD{
    /*
    Principales:
    ___________________________________
        - Llamamos la clase: PermissionGD
        - Solicitamos su construccion con:
          ---> PermissionGD.build(Context context, Activity activity)

          Sus valores a llamar son:
          + Context context: Pasar contexto de la aplicacion.
          + Activity activity: Pasar la Activity actual.

    Especiales:
    ___________________________________
        - Solicitamos el permiso. La lista de permisos disponible se encontrara llamando
          las clases publicas de PermissionGD, esta la solicitamos con:
          ---> recuest(String permission, int code)

          Sus valores a llamar son:
          + String permission: Permiso a solicitar.
          + int code: Codigo del permiso.

          Se recomienda utilizar unicamente los Strings de permisos dentro de PermissionGD,
          sus configuraciones internas funcionan en base a ello.

    ___________________________________
        - Solicitamos los resultados del permiso, para identificar si el permiso esta
          disponible o fue denegado por el usuario.
          Su metodo a llamar es:
          ---> recuestResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)

          Sus valores a llamar son:
          + (int requestCode): Codigo de consulta.
          + @NonNull String[] permissions: El permiso solicitado.
          + @NonNull int[] grantResults: Resultados.

          Se recomienda utilizar el predeterminado "@Override" ubicandolo en la clase a utilizar para
          llamar a estos datos:
          + @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
              super.onRequestPermissionsResult(requestCode, permissions, grantResults);
              PermissionGD.build(getApplicationContext(),Tu clase.this).recuestResult(requestCode,permissions,grantResults);
            }

    */


    //Public static class----------------------------------

    public static class InicialPrecess extends AppCompatActivity{
        /*Proceso inicial de evaluacion en caso de que se quieran
         * hacer las cosas de forma menos manual.*/

        //Variables-----------------------
        private final Context context;
        private final Activity activity;
        private Class<?> cls = null;

        //Privates-----------------------------------
        private InicialPrecess(Context _context, Activity activity){
            this.context = _context;
            this.activity = activity;
        }

        //Public static------------------------------------
        public static InicialPrecess build(Context context, Activity activity){
            return new InicialPrecess(context,activity);
        }

        //Public RecuestShould------------------------------------

        public InicialPrecess returnTo(@NonNull Class<?> returnTo){
            this.cls = returnTo;
            return this;
        }

        //Public void------------------------------------
        public void check(String permission, int code){
            if (!(PermissionGD.isGranted(context,permission))) {
                if (PermissionGD.isShouldShowRequest(context,activity,permission)){
                    activity.setContentView(R.layout.permision_nodetected);
                    Toast.makeText(context, "isShouldShowRecuest", Toast.LENGTH_SHORT).show();
                    View denied = findViewById(R.id.permisoDenied);
                    View granted = findViewById(R.id.permisoGranted);

                    granted.setOnClickListener(view -> {
                        PermissionGD.RecuestPermission.build(context,activity).recuest(permission,code);
                        Toast.makeText(context, "Reload", Toast.LENGTH_SHORT).show();
                    });
                    denied.setOnClickListener(view -> {
                        if (cls != null){
                            ChangeActivity.build(context,cls).start();
                        } else{
                            Log.e("PermissionGD","Falta implementar: returnTo(Class<?> returnTo)");
                        }
                    });
                } else {
                    activity.setContentView(R.layout.permision_nodetected);
                    PermissionGD.RecuestPermission.build(context,activity).recuest(permission,code);
                    Toast.makeText(context, "Reload 1", Toast.LENGTH_SHORT).show();
                }

            }
        }


    }

    public static class RecuestPermission extends AppCompatActivity{
        /*Solicitud de permisos y sus respuestas
        * sobre los permisos solicitados al usuario.*/
        //Variables-----------------------

        private final Context context;
        private final Activity activity;

        //Privates-----------------------------------

        private RecuestPermission(Context _context, Activity activity){
            this.context = _context;
            this.activity = activity;
        }

        //Public static------------------------------------

        public static RecuestPermission build(Context context, Activity activity){
            return new RecuestPermission(context,activity);
        }

        //Public void------------------------------------

        public void recuest(String permission, int code){
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{permission},code);
            }
        }

        public void recuestResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
            switch (requestCode) {
                case Permissions.WRITE_EXTERNAL_STORAGE_CODE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Concedido
                        Toast.makeText(context, "Concedido", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "Requerido el permiso de app", Toast.LENGTH_SHORT).show();
                    }
                    break;
                // Aquí más casos dependiendo de los permisos
                // case OTRO_CODIGO_DE_PERMISOS...
                default:
                    throw new IllegalStateException("Unexpected value: " + requestCode);
            }
        }

    }

    public static class Permissions{

        public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
        public static final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    }



    //Public static boolean--------------------------------

    public static boolean isGranted(Context context, String permission){
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isShouldShowRequest(Context context, Activity activity, String permission){
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        }
        return false;
    }



    //Lista de Permisos------------------------------------















}
