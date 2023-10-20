package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

/**
 * Esta clase representa la actividad del menú principal de la aplicación.
 */
public class Menu extends AppCompatActivity {
    ShapeableImageView open,ver,change_pin,change_password;

    TextView sesion;

    String usuario,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Inicialización de elementos de la interfaz
        open = findViewById(R.id.abrirC);
        ver = findViewById(R.id.VerRegistro);
        change_pin = findViewById(R.id.CambiarPin);
        change_password = findViewById(R.id.CambiarContra);
        sesion = findViewById(R.id.Cerrar_Sesion);

        // Recibe los datos de usuario y contraseña
        recibirDatos();

        // Cambiar de activity a la principal al hacer clic en "Cerrar Sesión"
        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Cambia de activity al hacer clic en "Cambiar Contraseña y manda los atributos usuario y pass, que son
        // el usuario y la contraseña del usuario"
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,change_password.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });

        // Cambiar de activity al hacer clic en "Ver Registro"y manda los atributos usuario y pass, que son
        // el usuario y la contraseña del usuario"
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,registro_act.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,abrir_cerradura.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });

    }

    /**
     * Modulo:Recibir Datos
     * Método para recibir los datos de usuario y contraseña de la actividad anterior
     * variables globales: usuario, pass
     *
     * Autor:André
     * Fecha:08/10/2023
     */
    private void recibirDatos(){
        try {
            Bundle extras = getIntent().getExtras();
            usuario = extras.getString("usuario");
            pass = extras.getString("pass");

        }catch (Exception e){
            Toast.makeText(Menu.this, ""+e, Toast.LENGTH_LONG).show();

        }

    }




}