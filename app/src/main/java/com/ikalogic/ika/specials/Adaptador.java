package com.ikalogic.ika.specials;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikalogic.ika.adapters.Persona;
import com.ikalogic.ika.specials.Adaptador;

import java.util.ArrayList;

public class Adaptador{
    //Seccion de inicializacion de variables.___________________________________*
    ArrayList<Persona> personas;
    RecyclerView rv1;
    View btEliminar, btAgregar;
    String nombre, telefono;
    EditText et1, et2;
    Adaptador.AdaptadorPersona ap;
    int layoutFile, elem1, elem2;
    private final Context context;



    //Termina seccion de inicializacion de variables.___________________________$




    //Seccion de inicializacion del Adaptador.__________________________________*
    /*IMPORTANTE
     * Pasamos el nuevo valor del contexto.
     * Este se obtiene mas abajo en el codigo mediante:
     * public static Adaptador build(@NonNull Context context)
     * */
    private Adaptador(Context _context){
        this.context = _context;
    }




    /*IMPORTANTE
    * Trabajamos lo que se va a mostrar una
    * vez obtenidos los datos
    * */
    @SuppressLint("NotifyDataSetChanged")
    public void show(){


        personas= new ArrayList<>();
        personas.add(new Persona("Juan","2323232323"));
        LinearLayoutManager l=new GridLayoutManager(context,3);
        rv1.setLayoutManager(l);
        ap=new AdaptadorPersona();
        rv1.setAdapter(ap);


        btAgregar.setOnClickListener(view -> {
            Persona persona1=new Persona(et1.getText().toString(),et2.getText().toString());
            personas.add(0,persona1);
            et1.setText("");
            et2.setText("");
            ap.notifyDataSetChanged();
            rv1.scrollToPosition(personas.size()-1);
        });
        btEliminar.setOnClickListener(view -> {
            int pos=-1;
            for(int f=0;f<personas.size();f++)
            {
                if(personas.get(f).getNombre().equals(et1.getText().toString()))
                    pos=f;
            }
            if (pos!=-1)
            {
                personas.remove(pos);
                et1.setText("");
                et2.setText("");
                ap.notifyDataSetChanged();
                Toast.makeText(context,"Se elimino la persona",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(context,"No existe la persona",Toast.LENGTH_SHORT).show();
        });

    }




    /*IMPORTANTE
     * Obtenemos la informacion del componente seleccionado
     * del RecyclerView y trabajamos a nuestro gusto con ello
     * */
    public void mostrar(int pos) {
        et1.setText(personas.get(pos).getNombre());
        et2.setText(personas.get(pos).getTelefono());
    }




    //Termina seccion de inicializacion del Adaptador.__________________________&




    //Seccion del la clase del Adaptador._______________________________________*
    /*IMPORTANTE
     * Adaptamos la informacion en base a la clase personas,
     * es alli donde almacenaremos la informacion y llamaremos.
     * */
    private class AdaptadorPersona extends RecyclerView.Adapter<AdaptadorPersona.AdaptadorPersonaHolder> {


        /*Inflamos el layout seleccionado en el RecyclerView
        * mediante el onCreateViewHolder*/
        @NonNull
        @Override
        public AdaptadorPersona.AdaptadorPersonaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorPersona.AdaptadorPersonaHolder(LayoutInflater.from(context).inflate(layoutFile, parent, false));
        }

        /*Desplegamos la data en la posicion especifica del
        * RecyclerView*/
        @Override
        public void onBindViewHolder(@NonNull AdaptadorPersona.AdaptadorPersonaHolder holder, int position) {
            holder.imprimir(position);
        }

        /*Otorgamos el tamaño del ArrayList*/
        @Override
        public int getItemCount() {
            return personas.size();
        }


        class AdaptadorPersonaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tv1,tv2;




            /*_____________________________*/
            /*Brindamos el setOnClickListener a los elementos
            * cargados desde el layout.
            * Aqui es donde tambien los podemos ubicar*/
            public AdaptadorPersonaHolder(@NonNull View itemView) {
                super(itemView);
                tv1=itemView.findViewById(elem1);
                tv2=itemView.findViewById(elem2);
                itemView.setOnClickListener(this);
            }




            /*_____________________________*/
            /*Obtenemos la posicion del View del recyclerView al dar
            * click sobre el*/
            @Override
            public void onClick(View v) {
                mostrar(getLayoutPosition());
            }





            /*_____________________________*/
            /*Imprimimos sobre los componentes del layout nuestra
             * informacion, este es el resultado final*/
            @SuppressLint("SetTextI18n")
            public void imprimir(int position) {
                tv1.setText("Nombre completo: "+personas.get(position).getNombre());
                tv2.setText("Telefono : "+personas.get(position).getTelefono());
            }

        }
    }




    //Termina seccion del la clase del Adaptador._______________________________&




    //Seccion de obtencion de datos.____________________________________________*
    /*Obtener el contexto de la aplicacion.
     * - Una vez obtenido el contexto lo retornamos subiendolo
     * mediante: return new Adapter(context)*/
    public static Adaptador build(@NonNull Context context){ return new Adaptador(context);}




    /*Obtener el layout a imprimir.
     * - Una vez obtenido el layout lo retornamos subiendolo
     * mediante su propio valor con: return this
     * - Eso hara que se reinicie desde el comienzo con ese
     * nuevo valor tal variable*/
    public Adaptador layout(int layout){
        this.layoutFile = layout;
        return this;
    }




    /*Obtener los elementos del layout.
     * - Una vez obtenido los elementos los retornamos subiendolos
     * mediante: return this*/
    public Adaptador layoutViews(int view1, int view2){
        this.elem1 = view1;
        this.elem2 = view2;
        return this;
    }




    /*Obtener el boton de agregar y eliminar.
     * - Una vez obtenido los botones lo retornamos subiendolo
     * mediante: return this*/
    public Adaptador buttons(View agregar, View eliminar){
        this.btAgregar = agregar;
        this.btEliminar = eliminar;
        return this;
    }




    /*Obtener los elementos del Activity.
     * - Una vez obtenido los elementos los retornamos subiendolos
     * mediante: return this*/
    public Adaptador activityViews(EditText editText1, EditText editText2){
        this.et1 = editText1;
        this.et2 = editText2;
        return this;
    }




    /*Obtener el texto a ingresar.
     * - Una vez obtenido los valores de la persona lo retornamos subiendolo
     * mediante: return this*/
    public Adaptador addPersona(@NonNull String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
        return this;
    }




    /*Obtener el recyclerView.
     * - Una vez obtenido el recyclerView lo retornamos
     * mediante: return this*/
    public Adaptador recyclerView(RecyclerView recyclerView) {
        this.rv1 = recyclerView;
        return this;
    }




    //Seccion de obtencion de datos.____________________________________________&
}
