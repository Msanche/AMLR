package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class abrir_cerradura extends AppCompatActivity {
    ImageButton back;

    String usuario,pass,cpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_cerradura);
        back = findViewById(R.id.back_button);
        recibirDatos();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(abrir_cerradura.this, Menu.class);
                // Envia los atributos usuario y pass a la activity Menu
                intent.putExtra("usuario", usuario);
                intent.putExtra("pass", cpass);
                startActivity(intent);
                finish();
            }
        });
    }

    private void recibirDatos() {
        try {
            Bundle extras = getIntent().getExtras();
            assert extras != null;
            usuario = extras.getString("usuario");
            pass = extras.getString("pass");
        } catch (Exception e) {
            // Maneja posibles excepciones mostrando un mensaje de error
            Toast.makeText(abrir_cerradura.this, "" + e, Toast.LENGTH_LONG).show();

        }
    }
}