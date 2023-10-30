package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import com.example.amlr.db.DbHelper;

public class abrir_cerradura extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice hc05Device; // Agrega tu dispositivo HC-05 aquí
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private EditText[] editTexts;
    private Button removeButton;
    private String usuario;
    private String pass;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_cerradura);

        editTexts = new EditText[] { findViewById(R.id.Number1), findViewById(R.id.Number2), findViewById(R.id.Number3), findViewById(R.id.Number4) };
        removeButton = findViewById(R.id.Remove);

        final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (EditText editText : editTexts) {
                    editText.setText("");
                }
            }
        });

        recibirDatos(); // Asegúrate de haber definido el método recibirDatos para obtener usuario y pass.

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            // El dispositivo no admite Bluetooth, maneja esto según tus necesidades.
            Toast.makeText(this, "El dispositivo no admite Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                // El Bluetooth está deshabilitado, solicita habilitarlo o maneja esto según tus necesidades.
                Toast.makeText(this, "Habilita el Bluetooth para continuar", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // Bluetooth está habilitado, puedes continuar con la conexión al HC-05.

                // Debes configurar hc05Device con la dirección MAC de tu HC-05.
                hc05Device = bluetoothAdapter.getRemoteDevice("98:D3:33:80:B4:69");

                // Luego puedes intentar establecer una conexión con el dispositivo HC-05.
                try {
                    bluetoothSocket = hc05Device.createRfcommSocketToServiceRecord(MY_UUID);
                    bluetoothSocket.connect();
                    outputStream = bluetoothSocket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error al conectar con el HC-05", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Agrega aquí la función para recibir los datos de usuario y contraseña.
    private void recibirDatos() {
        try {
            Bundle extras = getIntent().getExtras();
            usuario = extras.getString("usuario");
            pass = extras.getString("pass");
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_LONG).show();
        }
    }

    // Agrega aquí la función para validar la contraseña con la base de datos.
    private boolean validarContrasena() {
        DbHelper dbHelper = new DbHelper(this, "Cerradura.db", null, 6);
        return dbHelper.validarUsuario(usuario, pass);
    }

    // Agrega aquí la función para enviar "1" al HC-05 a través de Bluetooth.
    private void enviarUnoPorBluetooth() {
        if (outputStream != null) {
            try {
                outputStream.write("1".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al enviar datos por Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al cerrar la conexión Bluetooth", Toast.LENGTH_SHORT).show();
        }
    }
}
