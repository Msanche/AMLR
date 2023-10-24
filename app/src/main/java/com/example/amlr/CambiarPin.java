package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.amlr.db.DbHelper;

public class CambiarPin extends AppCompatActivity {

    EditText actualPassword,newPassword,confirmPassword;

    Button continuar,cancelar;

    ImageButton backs;

    boolean passVisible;

    String usuario,pass,cpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_pin);
        // Recibe los datos de la actividad anterior
        recibirDatos();

        // Inicialización de elementos de la interfaz
        actualPassword = findViewById(R.id.actual_password);
        newPassword = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confim_password);
        continuar = findViewById(R.id.ContinuarRP);
        cancelar = findViewById(R.id.CancelarRP);
        backs = findViewById(R.id.back);

        // Acción para mostrar/ocultar contraseña al tocar el icono del ojo en el campo "Contraseña Actual"
        actualPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right =2;
                if (motionEvent.getAction()==motionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>=actualPassword.getRight()-actualPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = actualPassword.getSelectionEnd();
                        if (passVisible){
                            actualPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye_off,0);
                            //para ocultar la contraseña
                            actualPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passVisible=false;
                        }else{
                            actualPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye,0);
                            //para mostrar la contraseña
                            actualPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passVisible=true;
                        }
                        actualPassword.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });


        // Acción para mostrar/ocultar contraseña al tocar el icono del ojo en el campo "Nueva Contraseña"
        newPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right =2;
                if (motionEvent.getAction()==motionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>=newPassword.getRight()-newPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = newPassword.getSelectionEnd();
                        if (passVisible){
                            newPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye_off,0);
                            //para ocultar la contraseña
                            newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passVisible=false;
                        }else{
                            newPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye,0);
                            //para mostrar la contraseña
                            newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passVisible=true;
                        }
                        newPassword.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });


        // Acción para mostrar/ocultar contraseña al tocar el icono del ojo en el campo "Confirmar Contraseña"
        confirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right =2;
                if (motionEvent.getAction()==motionEvent.ACTION_UP){
                    if (motionEvent.getRawX()>=confirmPassword.getRight()-confirmPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = confirmPassword.getSelectionEnd();
                        if (passVisible){
                            confirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye_off,0);
                            //para ocultar la contraseña
                            confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passVisible=false;
                        }else{
                            confirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.custom_key_icon,0,R.drawable.eye,0);
                            //para mostrar la contraseña
                            confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passVisible=true;
                        }
                        confirmPassword.setSelection(selection);
                        return true;
                    }
                }

                return false;
            }
        });


        // Acción al hacer clic en "Continuar"
        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apass = actualPassword.getText().toString();
                if (apass.equals(pass)){
                    String npass=newPassword.getText().toString();
                    cpass=confirmPassword.getText().toString();
                    if (!npass.isEmpty()||!cpass.isEmpty() ) {
                        if(npass.length()==4) {
                            if (npass.equals(cpass)) {
                                try {
                                    // Inicializa una instancia de DbHelper para manejar la base de datos
                                    DbHelper dbHelper = new DbHelper(CambiarPin.this, "Cerradura.db", null, 6);
                                    if (dbHelper.changePIN(usuario, npass)) {
                                        // Si el cambio de contraseña es exitoso, muestra un mensaje y redirige al menú
                                        Toast.makeText(CambiarPin.this, "PIN actualizado con exito", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(CambiarPin.this, Menu.class);
                                        intent.putExtra("correo", usuario);
                                        intent.putExtra("pass", cpass);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (Exception e) {
                                    // Maneja posibles excepciones mostrando un mensaje de error
                                    Toast.makeText(CambiarPin.this, "" + e, Toast.LENGTH_LONG).show();
                                }

                            } else {
                                // Muestra un mensaje si las contraseñas ingresadas no son iguales.
                                Toast.makeText(CambiarPin.this, "LOS CAMPOS \"NUEVO PIN\" Y \"CONFIRMA TU PIN\" NO SON IGUALES", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            //Muestra un mensaje si el PIN es diferente a 4 digitos
                            Toast.makeText(CambiarPin.this,"INTRODUCE UN PIN DE 4 DIGITOS", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        // Muestra un mensaje si se dejan campos vacíos
                        Toast.makeText(CambiarPin.this, "NO DEJES CAMPOS VACIOS", Toast.LENGTH_LONG).show();
                    }

                }else{
                    // Muestra un mensaje si la contraseña actual es incorrecta
                    Toast.makeText(CambiarPin.this, "CONTRASEÑA INCORRECTA", Toast.LENGTH_LONG).show();

                }
            }
        });


        // cambiar a la activity Menu al hacer clic en "Cancelar"
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CambiarPin.this, Menu.class);
                // Envia los atributos usuario y pass a la activity Menu
                intent.putExtra("usuario", usuario);
                intent.putExtra("pass", cpass);
                startActivity(intent);
                finish();
            }
        });

        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CambiarPin.this, Menu.class);
                // Envia los atributos usuario y pass a la activity Menu
                intent.putExtra("usuario", usuario);
                intent.putExtra("pass", cpass);
                startActivity(intent);
                finish();
            }
        });
    }

    // Método para recibir los datos de la actividad anterior
    private void recibirDatos() {
        try {
            Bundle extras = getIntent().getExtras();
            assert extras != null;
            usuario = extras.getString("usuario");
            pass = extras.getString("pass");
        } catch (Exception e) {
            // Maneja posibles excepciones mostrando un mensaje de error
            Toast.makeText(CambiarPin.this, "" + e, Toast.LENGTH_LONG).show();

        }
    }
}