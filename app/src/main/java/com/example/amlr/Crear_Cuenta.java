package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.amlr.db.DbHelper;

public class Crear_Cuenta extends AppCompatActivity {

    boolean passVisible,passCVisible;
    ImageButton Back;
    EditText pass,passC,user,mail;

    Button Crear;

    DbHelper helper = new DbHelper(Crear_Cuenta.this,"Cerradura.db",null,6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        Back = findViewById(R.id.back);
        pass = findViewById(R.id.confim_password);
        passC = findViewById(R.id.PasswordCerradura);
        Crear = findViewById(R.id.ContinuarRP);
        user = findViewById(R.id.nombre_user);
        mail = findViewById(R.id.Mail);


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
        passC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right =2;
                if (motionEvent.getAction()==motionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>=passC.getRight()-passC.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = passC.getSelectionEnd();
                        if (passCVisible){
                            passC.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye_off,0);
                            //para ocultar la contraseña
                            passC.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passCVisible=false;
                        }else{
                            passC.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye,0);
                            //para mostrar la contraseña
                            passC.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passCVisible=true;
                        }
                        passC.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        Crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = user.getText().toString();
                String correo = mail.getText().toString();
                String password = pass.getText().toString();
                String passwordC = passC.getText().toString();
                if (name.isEmpty() || correo.isEmpty() || password.isEmpty() || passwordC.isEmpty()) {
                    Toast.makeText(Crear_Cuenta.this, "NO DEJES CAMPOS VACIOS", Toast.LENGTH_LONG).show();

                } else {

                    Cursor fila = helper.getReadableDatabase().rawQuery("Select nombre from t_usuarios where nombre='" + name + "'",null);
                    if (fila.moveToFirst()){
                        //El nombre se repite
                        Toast.makeText(Crear_Cuenta.this, "EL NOMBRE DE USUARIO YA EXISTE", Toast.LENGTH_LONG).show();

                    }else{
                        //El nombre no se repite
                        if (valmail(mail)){
                            DbHelper dbHelper = new DbHelper(Crear_Cuenta.this,"Cerradura.db",null,6);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            if (db != null) {
                                Toast.makeText(Crear_Cuenta.this, "BASE DE DATOS CREADA", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Crear_Cuenta.this, "ERROR AL CREAR LA BASE DE DATOS", Toast.LENGTH_LONG).show();
                            }
                            ContentValues datosUsuario = new ContentValues();
                            datosUsuario.put("nombre", name);
                            datosUsuario.put("correo",correo);
                            datosUsuario.put("password",password);
                            datosUsuario.put("passwordC",passwordC);
                            try {
                                helper.getWritableDatabase().insert("t_usuarios",null,datosUsuario);
                                helper.getWritableDatabase().close();
                                Toast.makeText(Crear_Cuenta.this, "Usuario creado con exito", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Crear_Cuenta.this,MainActivity.class);
                                startActivity(intent);
                            }catch (Exception e){
                                Toast.makeText(Crear_Cuenta.this, "Error"+ e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                }
                }
            }
        });
    }
    public void Inicio(View view){
        Intent intent = new Intent(Crear_Cuenta.this,MainActivity.class);
        startActivity(intent);
    }

    private boolean valmail(EditText email){
        String correo = email.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            return true;
        }else{
            Toast.makeText(Crear_Cuenta.this, "INGRESA UN CORREO VALIDO", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}