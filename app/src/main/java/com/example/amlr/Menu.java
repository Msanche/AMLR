package com.example.amlr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Esta clase representa la actividad del menú principal de la aplicación.
 */
public class Menu extends AppCompatActivity {
    ShapeableImageView open, ver, change_pin, change_password;

    Button boton;
    TextView sesion;

    String usuario, pass;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice hc05Device;
    BluetoothSocket bluetoothSocket;
    OutputStream outputStream;
    InputStream inputStream;

    // UUID para el perfil SPP (Serial Port Profile)
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Inicialización de elementos de la interfaz
        boton = findViewById(R.id.boton);
        open = findViewById(R.id.abrirC);
        ver = findViewById(R.id.VerRegistro);
        change_pin = findViewById(R.id.CambiarPin);
        change_password = findViewById(R.id.CambiarContra);
        sesion = findViewById(R.id.Cerrar_Sesion);

        // Recibe los datos de usuario y contraseña
        recibirDatos();


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth no está disponible en este dispositivo", Toast.LENGTH_SHORT).show();
            } else {
                // Verificar si Bluetooth está habilitado y habilitarlo si es necesario
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                }
            }
            // Establecer la conexión Bluetooth con el dispositivo HC-05
            conectarHC05();
        } else {
            // No tienes los permisos necesarios, debes solicitarlos al usuario
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH}, 1);
        }
        } catch (Exception e) {
            // Manejar errores de conexión
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                led();
            }
        });

        // Cambiar de activity a la principal al hacer clic en "Cerrar Sesión"
        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Cambia de activity al hacer clic en "Cambiar Contraseña y manda los atributos usuario y pass, que son
        // el usuario y la contraseña del usuario"
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Menu.this, change_password.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("pass", pass);
                startActivity(intent);
            }
        });

        // Cambiar de activity al hacer clic en "Ver Registro"y manda los atributos usuario y pass, que son
        // el usuario y la contraseña del usuario"
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, registro_act.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("pass", pass);
                startActivity(intent);
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Menu.this, abrir_cerradura.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("pass", pass);
                startActivity(intent);
            }
        });

        change_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,CambiarPin.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this,abrir_cerradura.class);
                intent.putExtra("usuario",usuario);
                intent.putExtra("pass",pass);
                startActivity(intent);
            }
        });

    }

    public void led(){
        Intent intent = new Intent(Menu.this,ComunicacionBluetooth.class);
        intent.putExtra("usuario",usuario);
        intent.putExtra("pass",pass);
        startActivity(intent);
        finish();
        /*String command = "1"; // Puedes usar "0" para apagar el LED
        try {
            outputStream.write(command.getBytes());
        } catch (IOException e) {
            // Manejar errores de escritura
            Toast.makeText(this, "Error al enviar el comando: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El usuario concedió los permisos de Bluetooth, puedes continuar con la inicialización y la conexión.
            } else {
                // El usuario denegó los permisos, puedes mostrar un mensaje o tomar una acción adecuada.
                Toast.makeText(this, "Se requieren permisos Bluetooth para continuar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para establecer la conexión Bluetooth con el HC-05
    private void conectarHC05() {
        try {
            hc05Device = bluetoothAdapter.getRemoteDevice("98:D3:33:80:B4:69"); // Reemplaza con la dirección MAC de tu HC-05
        } catch (Exception e) {
            // Manejar errores de conexión
            Toast.makeText(this, "Error de conexión Bluetooth: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bluetoothSocket = hc05Device.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();
        } catch (IOException e) {
            // Manejar errores de conexión
            Toast.makeText(this, "Error de conexión Bluetooth: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Modulo:Recibir Datos
     * Método para recibir los datos de usuario y contraseña de la actividad anterior
     * variables globales: usuario, pass
     *
     * Autor:André
     * Fecha:08/10/2023
     */
    private void recibirDatos(){
        try {
            Bundle extras = getIntent().getExtras();
            usuario = extras.getString("usuario");
            pass = extras.getString("pass");

        }catch (Exception e){
            Toast.makeText(Menu.this, ""+e, Toast.LENGTH_LONG).show();

        }

    }




}