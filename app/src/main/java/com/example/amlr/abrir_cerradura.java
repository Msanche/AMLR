package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class abrir_cerradura extends AppCompatActivity {

    EditText posicion1,posicion2,posicion3,posicion4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_cerradura);
        posicion1 = findViewById(R.id.Number1);
        posicion2 = findViewById(R.id.Number2);





    }
}