package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amlr.db.DbHelper;

public class Restablecer_password extends AppCompatActivity {
    Button cancel,Confirm;

    EditText pass,confirmpass;

    String correo;

    boolean passVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_password);

        recibirDatos();

        pass = findViewById(R.id.password);
        confirmpass = findViewById(R.id.confim_password);
        cancel = findViewById(R.id.CancelarRP);
        Confirm = findViewById(R.id.ContinuarRP);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Restablecer_password.this,Validar_correo.class);
                startActivity(intent);
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passw = pass.getText().toString();
                String Cpassword = confirmpass.getText().toString();

                if (passw.equals(Cpassword)){
                    try {

                        DbHelper dbHelper = new DbHelper(Restablecer_password.this,"Cerradura.db",null,6);

                    if (dbHelper.editar_usuario(correo,passw)){
                        Toast.makeText(Restablecer_password.this, "Password restablecida con exito", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Restablecer_password.this,Iniciar_Sesion.class);
                        startActivity(intent);
                        finish();
                    }

                    }catch (Exception e){
                        Toast.makeText(Restablecer_password.this, ""+e, Toast.LENGTH_LONG).show();

                    }
                }
                if (!passw.equals(Cpassword)){
                    Toast.makeText(Restablecer_password.this, "Las contraseñas no son iguales", Toast.LENGTH_LONG).show();
                }
            }
        });

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

        confirmpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right =2;
                if (motionEvent.getAction()==motionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>=confirmpass.getRight()-confirmpass.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = confirmpass.getSelectionEnd();
                        if (passVisible){
                            confirmpass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye_off,0);
                            //para ocultar la contraseña
                            confirmpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passVisible=false;
                        }else{
                            confirmpass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye,0);
                            //para mostrar la contraseña
                            confirmpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passVisible=true;
                        }
                        confirmpass.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });


    }

    private void recibirDatos(){
        try {
            Bundle extras = getIntent().getExtras();
            String d1 = extras.getString("correo");
            correo = d1;
        }catch (Exception e){
            Toast.makeText(Restablecer_password.this, ""+e, Toast.LENGTH_LONG).show();

        }

    }
}