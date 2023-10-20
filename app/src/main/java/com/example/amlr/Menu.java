package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

public class Menu extends AppCompatActivity {
    ShapeableImageView open,ver,change_pin,change_password;

    TextView sesion;

    String usuario,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        open = findViewById(R.id.abrirC);
        ver = findViewById(R.id.VerRegistro);
        change_pin = findViewById(R.id.CambiarPin);
        change_password = findViewById(R.id.CambiarContra);
        sesion = findViewById(R.id.Cerrar_Sesion);

        recibirDatos();


        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,change_password.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,registro_act.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });

    }

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