package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amlr.db.DbHelper;

public class Iniciar_Sesion extends AppCompatActivity {
    EditText pass,user;
    TextView olvide;
    boolean passVisible;

    Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        pass = findViewById(R.id.confim_password);
        user = findViewById(R.id.nombre_user);
        entrar = findViewById(R.id.ContinuarRP);
        olvide = findViewById(R.id.forget);



        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right =2;
                if (motionEvent.getAction()==motionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>=pass.getRight()-pass.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = pass.getSelectionEnd();
                        if (passVisible){
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye_off,0);
                            //para ocultar la contraseña
                            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passVisible=false;
                        }else{
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye,0);
                            //para mostrar la contraseña
                            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passVisible=true;
                        }
                        pass.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validación(user,pass);
            }
        });

        olvide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Iniciar_Sesion.this,Validar_correo.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void Inicio(View view){
        Intent intent = new Intent(Iniciar_Sesion.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean validación(EditText user,EditText password){
        DbHelper helper = new DbHelper(Iniciar_Sesion.this,"Cerradura.db",null,6);

        String name = user.getText().toString();
        String pass = password.getText().toString();

        Cursor fila = helper.getReadableDatabase().rawQuery("Select nombre,password from t_usuarios where nombre='" + name + "' and password='"
                + pass + "'",null);
        if (fila.moveToFirst()){
            //El nombre se repite
            Intent intent = new Intent(Iniciar_Sesion.this,Menu.class);
            intent.putExtra("usuario",name);
            intent.putExtra("pass",pass);
            startActivity(intent);
            finish();
            return true;

        }else{
            Toast.makeText(Iniciar_Sesion.this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}