package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.amlr.adaptadores.ListaActividadesAdapter;
import com.example.amlr.db.DbHelper;
import com.example.amlr.entidades.Actividades;

import java.util.ArrayList;
import java.util.List;

public class registro_act extends AppCompatActivity {

    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private ImageButton back;
    private String usuario, cpass;

    // Lista para almacenar las actividades
    private List<String> activityList;

    //PARA LA LISTA
    RecyclerView listaActividades;
    ArrayList<Actividades> listaArrayActividades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        listaActividades = findViewById(R.id.lista_actividades);
        listaActividades.setLayoutManager(new LinearLayoutManager(this));

        DbHelper dbHelper = new DbHelper(registro_act.this, "Cerradura.db", null, 6);
        listaArrayActividades = new ArrayList<>();

        ListaActividadesAdapter adapter = new ListaActividadesAdapter(dbHelper.mostrarAcciones());
        listaActividades.setAdapter(adapter);

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        back = findViewById(R.id.back);

        // Obtén datos del intent
        recibirDatos();

        // Inicializa la lista y carga las actividades desde la base de datos
        activityList = new ArrayList<>();

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registro_act.this, Menu.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("pass", cpass);
                startActivity(intent);
                finish();
            }
        });
        
    }

    // Método para recibir datos del intent
    private void recibirDatos() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            usuario = extras.getString("usuario");
            cpass = extras.getString("pass");
        }
    }



}
