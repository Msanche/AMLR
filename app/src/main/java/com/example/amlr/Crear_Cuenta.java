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


/**
 * Esta clase representa la actividad para crear una cuenta de usuario.
 */
public class Crear_Cuenta extends AppCompatActivity {

    boolean passVisible,passCVisible;
    ImageButton Back;
    EditText pass,passC,user,mail;

    Button Crear;

    // Instancia de DbHelper para manejar la base de datos
    DbHelper helper = new DbHelper(Crear_Cuenta.this,"Cerradura.db",null,6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        
        // Inicialización de elementos de la interfaz
        Back = findViewById(R.id.back);
        pass = findViewById(R.id.confim_password);
        passC = findViewById(R.id.PasswordCerradura);
        Crear = findViewById(R.id.ContinuarRP);
        user = findViewById(R.id.nombre_user);
        mail = findViewById(R.id.Mail);

        // Acción para mostrar/ocultar contraseña al tocar el icono del ojo en el campo "Contraseña"

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

        // Acción para mostrar/ocultar contraseña al tocar el icono del ojo en el campo "Confirmar Contraseña"
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

        // Acción al hacer clic en "Crear"
        Crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener datos del formulario
                String name = user.getText().toString();
                String correo = mail.getText().toString();
                String password = pass.getText().toString();
                String passwordC = passC.getText().toString();
                if (name.isEmpty() || correo.isEmpty() || password.isEmpty() || passwordC.isEmpty()) {
                    // Mostrar mensaje si hay campos vacíos
                    Toast.makeText(Crear_Cuenta.this, "NO DEJES CAMPOS VACIOS", Toast.LENGTH_LONG).show();
                } else {
                    Cursor fila = helper.getReadableDatabase().rawQuery("Select nombre from t_usuarios where nombre='" + name + "'",null);
                    if (fila.moveToFirst()){
                        // Mostrar mensaje si el nombre de usuario ya existe
                        Toast.makeText(Crear_Cuenta.this, "EL NOMBRE DE USUARIO YA EXISTE", Toast.LENGTH_LONG).show();

                    }else{
                        // Comprobar si el correo es válido
                        if (valmail(mail)){
                            DbHelper dbHelper = new DbHelper(Crear_Cuenta.this,"Cerradura.db",null,6);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            if (db != null) {
                                // Mostrar mensaje si la base de datos se creó correctamente
                                Toast.makeText(Crear_Cuenta.this, "BASE DE DATOS CREADA", Toast.LENGTH_LONG).show();
                            } else {
                                // Mostrar mensaje si hubo un error al crear la base de datos
                                Toast.makeText(Crear_Cuenta.this, "ERROR AL CREAR LA BASE DE DATOS", Toast.LENGTH_LONG).show();
                            }
                            ContentValues datosUsuario = new ContentValues();
                            datosUsuario.put("nombre", name);
                            datosUsuario.put("correo",correo);
                            datosUsuario.put("password",password);
                            datosUsuario.put("passwordC",passwordC);
                            try {
                                // Insertar los datos del usuario en la base de datos
                                helper.getWritableDatabase().insert("t_usuarios",null,datosUsuario);
                                helper.getWritableDatabase().close();
                                // Mostrar mensaje de éxito y redirigir a la actividad principal
                                Toast.makeText(Crear_Cuenta.this, "Usuario creado con exito", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Crear_Cuenta.this,MainActivity.class);
                                startActivity(intent);
                            }catch (Exception e){
                                // Manejar posibles excepciones y mostrar un mensaje de error
                                Toast.makeText(Crear_Cuenta.this, "Error"+ e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                }
                }
            }
        });
    }
    // Método para regresar a la actividad principal
    public void Inicio(View view){
        Intent intent = new Intent(Crear_Cuenta.this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * Metodo:valmail
     * Método para validar si el correo es válido.
     *
     * @param email Campo de correo electrónico.
     * @return Verdadero si el correo es válido, falso de lo contrario.
     * Autor: Lilia
     * Fecha: 19/10/2023
     */
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