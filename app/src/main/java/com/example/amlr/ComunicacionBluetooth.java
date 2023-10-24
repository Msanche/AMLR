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

import java.util.ArrayList;
import java.util.Set;

public class ComunicacionBluetooth extends AppCompatActivity {
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunicacion_bluetooth);

        // Verificar y solicitar los permisos necesarios
        checkAndRequestBluetoothPermissions();

        // Inicializar el adaptador Bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Verificar si el dispositivo admite Bluetooth
        if (bluetoothAdapter == null) {
            // Tu dispositivo no admite Bluetooth
            finish();
        }

        // Obtener la lista de dispositivos emparejados
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
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        ArrayList<String> deviceNames = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceNames.add(device.getName());
                deviceNames.add(device.getAddress());
            }
        }

        // Mostrar la lista de dispositivos emparejados en el ListView
        ListView listView = findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceNames);
        listView.setAdapter(adapter);

        // Manejar la selección del dispositivo
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el dispositivo Bluetooth seleccionado
                String selectedDeviceName = deviceNames.get(position);
                BluetoothDevice selectedDevice = null;

                // Buscar el dispositivo correspondiente en la lista emparejada
                for (BluetoothDevice device : pairedDevices) {
                    if (ActivityCompat.checkSelfPermission(ComunicacionBluetooth.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    if (device.getName().equals(selectedDeviceName)) {
                        selectedDevice = device;
                        break;
                    }
                }

                if (selectedDevice != null) {
                    // Realiza acciones relacionadas con el dispositivo seleccionado
                    // Por ejemplo, puedes iniciar una nueva actividad para la comunicación Bluetooth
                    // y pasar el dispositivo seleccionado como dato adicional.
                    Intent intent = new Intent(ComunicacionBluetooth.this, Menu.class);
                    intent.putExtra("selectedDevice", selectedDevice);
                    startActivity(intent);
                }
            }
        });
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
        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_BLUETOOTH_PERMISSIONS);
    }
}