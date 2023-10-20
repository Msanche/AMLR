package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amlr.db.DbHelper;

public class Validar_correo extends AppCompatActivity {
    Button Canc,Cont;

    EditText correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_correo);

        Canc = findViewById(R.id.CancelarRP);
        Cont = findViewById(R.id.ContinuarRP);
        correo = findViewById(R.id.Mail);

        Canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Validar_correo.this,Iniciar_Sesion.class);
                startActivity(intent);
            }
        });

        Cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = correo.getText().toString();
                DbHelper dbHelper = new DbHelper(Validar_correo.this,"Cerradura.db",null,6);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                Cursor fila = db.rawQuery("Select correo from t_usuarios where correo='" + mail + "'",null);
                if (fila.moveToFirst()){
                    //El correo existe
                    Intent intent = new Intent(Validar_correo.this,Restablecer_password.class);
                    intent.putExtra("correo",mail);
                    startActivity(intent);

                }else{
                    Toast.makeText(Validar_correo.this, "CORREO INVALIDO", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}