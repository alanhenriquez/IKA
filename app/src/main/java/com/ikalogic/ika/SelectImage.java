package com.ikalogic.ika;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.ikalogic.ika.adapters.ImageAdapter;
import com.ikalogic.ika.exceptions.InitActivityScreenOrientPortrait;
import com.ikalogic.ika.helpers.GetDataUser;
import com.ikalogic.ika.helpers.PermissionGD;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public class SelectImage extends AppCompatActivity {
    ImageView imageView;
    RecyclerView recyclerView;
    ArrayList<String> imagelist;
    StorageReference root;
    ProgressBar progressBar;
    ImageAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;
    private static final int CODIGO_PERMISOS_WRITE_EXTERNAL_STORAGE = 1 ;


    TextView mostrar;
    View regresar, confirmar;
    WifiManager wifiManager;

    FirebaseAuth userAuth;
    DatabaseReference userDataBase;

    /*-------------------------------------------------------------------------------*/
    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        //Solicitamos permisos__________________________________________________
        if (!(PermissionGD.isGranted(getApplicationContext(),PermissionGD.Permissions.WRITE_EXTERNAL_STORAGE))){
            PermissionGD.InicialPrecess.build(getApplicationContext(),SelectImage.this)
                    .check(PermissionGD.Permissions.WRITE_EXTERNAL_STORAGE,PermissionGD.Permissions.WRITE_EXTERNAL_STORAGE_CODE);
        }else {
            //Orientacion de la pantalla____________________________________________
            InitActivityScreenOrientPortrait.build(this);

            //Base de datos Firebase y acceso al usuario____________________________
            userAuth = FirebaseAuth.getInstance();
            userDataBase = FirebaseDatabase.getInstance().getReference();





            //Imagenes y RecyclerView + Adapter
            imageView = findViewById(R.id.imgPhotoUser);
            Glide.with(getApplicationContext()).load(GetDataUser.loadOnImageString()).into(imageView);
            imagelist=new ArrayList<>();
            recyclerView=findViewById(R.id.recyclerview);
            adapter=new ImageAdapter(imagelist,this,imageView);
            regresar = findViewById(R.id.regresar);
            confirmar = findViewById(R.id.confirmar);
            mostrar = findViewById(R.id.m);
            progressBar=findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);
            swipeRefreshLayout = ( SwipeRefreshLayout ) findViewById ( R.id.swiperefreshlayout ) ;


            swipeRefreshLayout.setOnRefreshListener(() -> {
                swipeRefreshLayout.setRefreshing(true);
                if (swipeRefreshLayout.isRefreshing()){
                    imagelist.clear();
                    String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    StorageReference listRef = FirebaseStorage.getInstance().getReference().child("Users/"+id+"/D40D-1E13:DCIM/Camera-11/");
                    listRef.listAll().addOnSuccessListener(listResult -> {

                        for(StorageReference file:listResult.getItems()){
                            file.getDownloadUrl().addOnSuccessListener(uri -> {
                                imagelist.add(uri.toString());
                                String h = "https://firebasestorage.googleapis.com/v0/b/ikaproyect.appspot.com/o/Users%2Fj7sIGgfkvMWLtk3NS6Ngkf89X1u2%2FD40D-1E13%3ADCIM%2FCamera-11%2F20211218_091915.jpg?alt=media&token=f6ee00bf-803e-43ca-bfc6-fbcafbab3a53";
                                Log.e("Itemvalue",uri.toString());
                                Log.e("ItemValueCompare", imagelist.get(0));
                            }).addOnSuccessListener(uri -> {
                                LinearLayoutManager l= new GridLayoutManager(getApplicationContext(),3);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(l);
                                progressBar.setVisibility(View.GONE);
                            }).addOnFailureListener(e -> msgToast(String.valueOf(e)));
                        }
                    });
                    swipeRefreshLayout.setRefreshing(false);
                }
            });


            String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("ImageData").child("uploadeds").addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (int i = 0;i<snapshot.getChildrenCount(); i++){
                            charge(i);
                        }
                        for (DataSnapshot child: snapshot.getChildren()) {

                            Map<String, Object> values = new HashMap<>();
                            values.put(child.getKey(),child.getValue());
                            Log.d("firebase", String.valueOf(values));

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            StorageReference listRef = FirebaseStorage.getInstance().getReference().child("Users/"+id+"/D40D-1E13:DCIM/Camera-11/");
            listRef.listAll().addOnSuccessListener(listResult -> {

                for(StorageReference file:listResult.getItems()){
                    file.getDownloadUrl().addOnSuccessListener(uri -> {
                        imagelist.add(uri.toString());
                        String h = "https://firebasestorage.googleapis.com/v0/b/ikaproyect.appspot.com/o/Users%2Fj7sIGgfkvMWLtk3NS6Ngkf89X1u2%2FD40D-1E13%3ADCIM%2FCamera-11%2F20211218_091915.jpg?alt=media&token=f6ee00bf-803e-43ca-bfc6-fbcafbab3a53";
                        Log.e("Itemvalue",uri.toString());
                        Log.e("ItemValueCompare", imagelist.get(0));
                    }).addOnSuccessListener(uri -> {
                        LinearLayoutManager l= new GridLayoutManager(getApplicationContext(),3);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(l);
                        progressBar.setVisibility(View.GONE);
                    }).addOnFailureListener(e -> msgToast(String.valueOf(e)));
                }
            });


            //Botones del Header para regresar y confirmar
            regresar.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            });
            confirmar.setOnClickListener(view ->{

                if (PermissionGD.isGranted(getApplicationContext(),PermissionGD.Permissions.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "True", Toast.LENGTH_SHORT).show();
                }else {
                    if (PermissionGD.isShouldShowRequest(getApplicationContext(), SelectImage.this, PermissionGD.Permissions.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(this, "Requiere permiso", Toast.LENGTH_SHORT).show();
                    }
                }
            /*String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/DCIM/"+ "IKA");
            try {
                if(!myDir.exists())
                {
                    myDir.mkdirs();
                }else {
                    Toast.makeText(this, "El directorio ya existe", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e){
                e.printStackTrace();
            }*/
            });



        }

    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), EditProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGD.RecuestPermission.build(getApplicationContext(),SelectImage.this).recuestResult(requestCode,permissions,grantResults);
    }




    /*-------------------------------------------------------------------------------*/






    public void charge(int num){
        Log.d("firebaseNum", String.valueOf(num));
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }





    private void setDataImageBase(Drawable link){

        Map<String, Object> data = new HashMap<>();
        data.put("ImageMain", String.valueOf(getImageUri(getApplicationContext(),drawableToBitmap(link))));
        String id = Objects.requireNonNull(userAuth.getCurrentUser()).getUid();
        userDataBase.child("Users").child(id).child("ImageData").child("imgPerfil").setValue(data).addOnCompleteListener(task1 -> msgToast("Datos actualizados"));

    }


    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        RunnableFuture<Boolean> futureRun = new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if ((networkInfo .isAvailable()) && (networkInfo .isConnected())) {
                    try {
                        HttpURLConnection urlc = (HttpURLConnection) (new URL("https://www.google.com/").openConnection());
                        urlc.setRequestProperty("User-Agent", "Test");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(1500);
                        urlc.connect();
                        Log.e("TAG", "checking internet connection *");
                        return (urlc.getResponseCode() == 200);

                    } catch (IOException e) {
                        Log.e(TAG, "Error checking internet connection ********", e);
                    }
                } else {
                    Log.d(TAG, "No network available! ********%");
                }
                return false;
            }
        });

        new Thread(futureRun).start();


        try {
            return futureRun.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void SpliterText(String texto, String toSplit){
        String[] separated = texto.split(toSplit);
        for (String s : separated) {
            Log.e("SpliterText", "Users/j7sIGgfkvMWLtk3NS6Ngkf89X1u2/D40D-1E13:DCIM/Camera-11/");
        }
    }

    /*Variable para generar el mensaje Toast*/
    private void msgToast(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }


}