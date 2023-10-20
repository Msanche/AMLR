package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button ISesion,CCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Welcome);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ISesion =  findViewById(R.id.bottom);
        CCuenta = findViewById(R.id.registrar);

        ISesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Iniciar_Sesion.class);
                startActivity(intent);
            }
        });

        CCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Crear_Cuenta.class);
                startActivity(intent);
            }

        });
    }
}