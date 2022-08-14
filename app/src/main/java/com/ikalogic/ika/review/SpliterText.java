package com.ikalogic.ika.review;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class SpliterText {

    //VARIABLES-----------------------------------------------------------------
    Context context;
    String text, split;
    String[] conjunto;
    TextView textView = null;
    EditText editText = null;


    //OBTENCION DEL CONTEXTO----------------------------------------------------

    private SpliterText(Context _context){
        this.context = _context;
    }

    public static SpliterText build(@NonNull Context context){ return new SpliterText(context); }

    public void show(){
        conjunto = text.split(split);
        if (editText != null){
            editText.setText(Arrays.toString(conjunto));
        }else if (textView != null){
            textView.setText(Arrays.toString(conjunto));
        }else {
            Toast.makeText(context, Arrays.toString(conjunto), Toast.LENGTH_SHORT).show();
            Log.e("SpliterText", Arrays.toString(conjunto));
        }
    }


    //GETERS Y SETERS-----------------------------------------------------------
    public SpliterText set(@NonNull String texto, @NonNull String split){
        this.text = texto;
        this.split = split;
        this.conjunto = this.text.split(this.split);
        return this;
    }

    public SpliterText into(TextView variable){
        this.textView = variable;
        return this;
    }

    public SpliterText into(EditText variable){
        this.editText = variable;
        return this;
    }

    public String getResult(){
        if (textView != null){
            textView.setText(Arrays.toString(conjunto));
        }else if (editText != null){
            editText.setText(Arrays.toString(conjunto));
        }else {
            return Arrays.toString(conjunto);
        }
        return null;
    }

}
