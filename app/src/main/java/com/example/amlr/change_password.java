package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amlr.db.DbHelper;

public class change_password extends AppCompatActivity {

    EditText actualPassword,newPassword,confirmPassword;

    Button continuar,cancelar;

    boolean passVisible;

    String usuario,pass,cpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        recibirDatos();

        actualPassword = findViewById(R.id.actual_password);
        newPassword = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confim_password);
        continuar = findViewById(R.id.ContinuarRP);
        cancelar = findViewById(R.id.CancelarRP);

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

        continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apass = actualPassword.getText().toString();
                if (apass.equals(pass)){
                    String npass=newPassword.getText().toString();
                    cpass=confirmPassword.getText().toString();
                    if (!npass.isEmpty()||!cpass.isEmpty() ) {
                        if (npass.equals(cpass)) {
                            try {
                                DbHelper dbHelper = new DbHelper(change_password.this,"Cerradura.db",null,6);
                                if (dbHelper.changePassword(usuario,cpass)) {
                                    Toast.makeText(change_password.this, "Contraseña actualizada con exito", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(change_password.this, Menu.class);
                                    intent.putExtra("correo", usuario);
                                    intent.putExtra("pass", cpass);
                                    startActivity(intent);
                                    finish();
                                }
                            }catch (Exception e){
                                Toast.makeText(change_password.this, ""+e, Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(change_password.this, "LOS CAMPOS \"NUEVA CONTRASEÑA\" Y \"CONFIRMA TU CONTRASEÑA\" NO SON IGUALES", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(change_password.this, "NO DEJES CAMPOS VACIOS", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(change_password.this, "CONTRASEÑA INCORRECTA", Toast.LENGTH_LONG).show();

                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(change_password.this, Menu.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("pass", cpass);
                startActivity(intent);
                finish();
            }
        });
    }

    private void recibirDatos() {
        try {
            Bundle extras = getIntent().getExtras();
            assert extras != null;
            usuario = extras.getString("usuario");
             pass = extras.getString("pass");
        } catch (Exception e) {
            Toast.makeText(change_password.this, "" + e, Toast.LENGTH_LONG).show();

        }
    }
}