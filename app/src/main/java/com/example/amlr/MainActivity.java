package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Esta clase representa la actividad principal de la aplicación.
 */
public class MainActivity extends AppCompatActivity {
    Button ISesion,CCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            // Pausa la actividad durante 400 milisegundos para mostrar una pantalla de bienvenida.

            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Configura el tema de la actividad de bienvenida.

        setTheme(R.style.Welcome);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de elementos de la interfaz

        ISesion =  findViewById(R.id.bottom);
        CCuenta = findViewById(R.id.registrar);

        // Cambiar de activity al hacer clic en el botón "Iniciar Sesión"

        ISesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Iniciar_Sesion.class);
                startActivity(intent);
            }
        });

        // Cambiar de activity al hacer clic en el botón "Crear Cuenta"
        CCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Crear_Cuenta.class);
                startActivity(intent);
            }

        });
    }
}