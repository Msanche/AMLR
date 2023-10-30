package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class ComunicacionBluetooth extends AppCompatActivity {
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<BluetoothDevice> pairedDevices;
    private ArrayAdapter<String> deviceNamesAdapter;

    String usuario,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicacion_bluetooth);

        recibirDatos();

        // Verificar y solicitar los permisos necesarios
        checkAndRequestBluetoothPermissions();

        // Inicializar el adaptador Bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Verificar si el dispositivo admite Bluetooth
        if (bluetoothAdapter == null) {
            // Tu dispositivo no admite Bluetooth
            finish();
            return;
        }

        // Obtener la lista de dispositivos emparejados
        pairedDevices = new ArrayList<>();
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
        Set<BluetoothDevice> pairedDeviceSet = bluetoothAdapter.getBondedDevices();
        pairedDevices.addAll(pairedDeviceSet);

        // Inicializar el adaptador de la lista de dispositivos
        ListView listView = findViewById(R.id.list);
        deviceNamesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(deviceNamesAdapter);

        // Llenar la lista de dispositivos emparejados
        for (BluetoothDevice device : pairedDevices) {
            deviceNamesAdapter.add(device.getName() + " (" + device.getAddress() + ")");
        }

        // Manejar la selección del dispositivo
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el dispositivo Bluetooth seleccionado
                BluetoothDevice selectedDevice = pairedDevices.get(position);

                // Realiza acciones relacionadas con el dispositivo seleccionado
                // Por ejemplo, puedes iniciar una nueva actividad para la comunicación Bluetooth
                // y pasar el dispositivo seleccionado como dato adicional.
                Intent intent = new Intent(ComunicacionBluetooth.this, Menu.class);
                intent.putExtra("selectedDevice", selectedDevice);
                startActivity(intent);
            }
        });
    }
    private void recibirDatos(){
        try {
            Bundle extras = getIntent().getExtras();
            usuario = extras.getString("usuario");
            pass = extras.getString("pass");

        }catch (Exception e){
            Toast.makeText(ComunicacionBluetooth.this, ""+e, Toast.LENGTH_LONG).show();

        }

    }

    private void checkAndRequestBluetoothPermissions() {
        // Verificar si ya se han concedido los permisos
        int bluetoothPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH);
        int bluetoothAdminPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN);

        if (bluetoothPermission == PackageManager.PERMISSION_GRANTED &&
                bluetoothAdminPermission == PackageManager.PERMISSION_GRANTED) {
            // Los permisos ya han sido concedidos
            return;
        }

        // Solicitar permisos si no se han concedido
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_BLUETOOTH_PERMISSIONS);
    }
}


