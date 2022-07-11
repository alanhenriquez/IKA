package com.ikalogic.ika;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.ikalogic.ika.adapters.ImageAdapter;
import com.ikalogic.ika.exceptions.InitActivityScreenOrientPortrait;
import com.ikalogic.ika.helpers.GetDataUser;
import com.ikalogic.ika.helpers.SpliterText;
import com.ikalogic.ika.helpers.URLValidator;
import com.ikalogic.ika.specials.Adaptador;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    FirebaseAuth userAuth;
    TextView mostrar;
    WifiManager wifiManager;

    /*-------------------------------------------------------------------------------*/
    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        userAuth = FirebaseAuth.getInstance();
        InitActivityScreenOrientPortrait.build(this);

        imageView = findViewById(R.id.imgPhotoUser);
        Glide.with(getApplicationContext()).load(GetDataUser.loadOnImageString()).into(imageView);

        imagelist=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerview);
        adapter=new ImageAdapter(imagelist,this);
        View regresar = findViewById(R.id.regresar);
        mostrar = findViewById(R.id.m);



        String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("ImageData").child("uploadeds").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
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
                    Log.e("Itemvalue",uri.toString());
                }).addOnSuccessListener(uri -> {
                    LinearLayoutManager l= new GridLayoutManager(getApplicationContext(),3);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(l);
                }).addOnFailureListener(e -> {
                    // Error, Image not uploaded
                    msgToast("Error en la carga " + e.getMessage());
                });
            }
        });




        







        isOnline(getApplicationContext());





        regresar.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditProfile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });




    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), EditProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    /*-------------------------------------------------------------------------------*/












    
    
    
    
    
    
    


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