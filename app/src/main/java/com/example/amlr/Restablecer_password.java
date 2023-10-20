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


/**
 * Esta clase representa la actividad de restablecimiento de contraseña de la aplicación.
 */
public class Restablecer_password extends AppCompatActivity {
    Button cancel,Confirm;

    EditText pass,confirmpass;

    String correo;

    boolean passVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_password);
        // Recibe los datos de la actividad anterior
        recibirDatos();

        // Inicialización de elementos de la interfaz
        pass = findViewById(R.id.password);
        confirmpass = findViewById(R.id.confim_password);
        cancel = findViewById(R.id.CancelarRP);
        Confirm = findViewById(R.id.ContinuarRP);

        // Cambia a la activity Validar_correo al hacer clic en "Cancelar"
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Restablecer_password.this,Validar_correo.class);
                startActivity(intent);
            }
        });

        // Acción al hacer clic en "Confirmar"
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtiene la contraseña y la confirmación de contraseña ingresadas por el usuario
                String passw = pass.getText().toString();
                String Cpassword = confirmpass.getText().toString();

                // Verifica si las contraseñas son iguales
                if (passw.equals(Cpassword)){
                    try {
                        // Inicializa una instancia de DbHelper para manejar la base de datos
                        DbHelper dbHelper = new DbHelper(Restablecer_password.this,"Cerradura.db",null,6);
                        // Intenta editar la contraseña del usuario en la base de datos
                    if (dbHelper.editar_usuario(correo,passw)){
                        // Si la edición es exitosa, muestra un mensaje y redirige al inicio de sesión
                        Toast.makeText(Restablecer_password.this, "Password restablecida con exito", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Restablecer_password.this,Iniciar_Sesion.class);
                        startActivity(intent);
                        finish();
                    }

                    }catch (Exception e){
                        // Maneja posibles excepciones mostrando un mensaje de error
                        Toast.makeText(Restablecer_password.this, ""+e, Toast.LENGTH_LONG).show();

                    }
                }
                if (!passw.equals(Cpassword)){
                    // Muestra un mensaje si las contraseñas ingresadas no son iguales
                    Toast.makeText(Restablecer_password.this, "Las contraseñas no son iguales", Toast.LENGTH_LONG).show();
                }
            }
        });

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

    // Método para recibir los datos de la actividad anterior
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