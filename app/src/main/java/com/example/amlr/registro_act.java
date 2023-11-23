package com.example.amlr;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.amlr.RegistroListener;
import com.example.amlr.db.DbHelper2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class registro_act extends AppCompatActivity implements RegistroListener{

    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private ImageButton back;
    private String usuario, cpass;
    private DbHelper2 dbHelper2;

    // Lista para almacenar las actividades
    private List<String> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        dbHelper2 = new DbHelper2(this);

        // Inicialización de elementos de la interfaz
        linearLayout = findViewById(R.id.linear_layout);
        scrollView = findViewById(R.id.scroll_view);
        back = findViewById(R.id.back);

        // Obtén datos del intent
        recibirDatos();

        // Agrega una actividad de ejemplo (puedes reemplazar esto con tus propias actividades)
        //agregarActividad("Se abrió la cerradura");

        // Cargar actividades existentes desde la base de datos
        cargarActividadesDesdeBD();

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

    // Método para agregar una actividad a la lista y actualizar la interfaz
    private void agregarActividad(String descripcion) {
        // Agregar la actividad a la lista
        activityList.add(getCurrentTimeStamp() + " - " + descripcion);

        // Actualizar la interfaz
        cargarActividades();
    }

    // Método para cargar las actividades en la interfaz
    private void cargarActividades() {
        // Limpiar el LinearLayout antes de agregar las nuevas actividades
        //linearLayout.removeAllViews();

        // Iterar sobre la lista de actividades y agregar CardViews
        for (String actividad : activityList) {
            // Crea un CardView (rectángulo) para representar la información
            final CardView cardView = new CardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(16, 16, 16, 16);
            cardView.setLayoutParams(cardParams);

            // Crea un TextView para mostrar información en el CardView
            TextView textView = new TextView(this);
            textView.setText(actividad);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            cardView.addView(textView);

            // Agrega el CardView al LinearLayout
            linearLayout.addView(cardView);
        }

        // Desplázate automáticamente al final de la lista después de agregar nuevas actividades
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    // Método para obtener la marca de tiempo actual
    private String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Implementación de los métodos de RegistroListener
    @Override
    public void onAperturaCerradura() {
        String movimiento = "Se abrió la cerradura";
        agregarActividadEnBD(movimiento);
    }

    @Override
    public void onBloqueoCerradura() {
        String movimiento = "Se bloqueó la cerradura";
        agregarActividadEnBD(movimiento);
    }

    private void agregarActividadEnBD(String movimiento) {
        String fechaHora = getCurrentTimeStamp();
        long id = dbHelper2.insertRegistro(fechaHora, movimiento);

        if (id != -1) {
            // Agregar la actividad a la interfaz si se insertó correctamente en la base de datos
            agregarActividad(fechaHora + " - " + movimiento);
        }
    }

    private void cargarActividadesDesdeBD() {
        Cursor cursor = dbHelper2.getAllRegistros();

        if (cursor.moveToFirst()) {
            do {
                String fechaHora = cursor.getString(cursor.getColumnIndex(DbHelper2.COLUMN_FECHA_HORA));
                String movimiento = cursor.getString(cursor.getColumnIndex(DbHelper2.COLUMN_MOVIMIENTO));
                agregarActividad(fechaHora + " - " + movimiento);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}
