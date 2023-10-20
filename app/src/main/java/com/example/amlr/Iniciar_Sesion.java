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

/**
 * Esta clase representa la actividad de inicio de sesión de la aplicación.
 */
public class Iniciar_Sesion extends AppCompatActivity {
    // Elementos de la interfaz de usuario

    EditText pass,user;
    TextView olvide;
    boolean passVisible;

    Button entrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        // Inicialización de elementos de la interfaz

        pass = findViewById(R.id.confim_password);
        user = findViewById(R.id.nombre_user);
        entrar = findViewById(R.id.ContinuarRP);
        olvide = findViewById(R.id.forget);


        // Manejo de la visibilidad de la contraseña

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
        // Acción al hacer clic en el botón de entrada

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validación(user,pass);
            }
        });
        // Acción al hacer clic en "¿Olvidaste tu contraseña?"

        olvide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Iniciar_Sesion.this,Validar_correo.class);
                startActivity(intent);
                finish();
            }
        });
    }
    // Método para volver a la pagina de inicio
    public void Inicio(View view){
        Intent intent = new Intent(Iniciar_Sesion.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Modulo:validación
     * Realiza la validación de usuario y contraseña en la base de datos.
     *
     * @param user     Campo de texto de nombre de usuario.
     * @param password Campo de texto de contraseña.
     * @return `true` si la validación es exitosa, `false` en caso contrario.
     *
     * Autor:Marko
     * Fecha:12/10/2023
     */
    public boolean validación(EditText user,EditText password){
        DbHelper helper = new DbHelper(Iniciar_Sesion.this,"Cerradura.db",null,6);

        String name = user.getText().toString();
        String pass = password.getText().toString();

        Cursor fila = helper.getReadableDatabase().rawQuery("Select nombre,password from t_usuarios where nombre='" + name + "' and password='"
                + pass + "'",null);
        if (fila.moveToFirst()){
            // El nombre se encuentra en la base de datos
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