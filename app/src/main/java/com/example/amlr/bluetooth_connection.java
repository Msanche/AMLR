package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class bluetooth_connection extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> deviceArrayAdapter;
    private ListView deviceListView;
    private ProgressBar progressBar;


    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;



    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);
        deviceListView = findViewById(R.id.deviceListView);
        deviceArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceListView.setAdapter(deviceArrayAdapter);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE); // Oculta la barra de progreso inicialmente

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    try {
        if (bluetoothAdapter == null) {
            // Tu dispositivo no admite Bluetooth
            Toast.makeText(this, "Bluetooth no está disponible en este dispositivo", Toast.LENGTH_LONG).show();
            finish();
        }

        // Verifica si el Bluetooth está habilitado y, si no lo está, solicita al usuario que lo habilite.
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        // Solicitar permisos de BLUETOOTH y BLUETOOTH_ADMIN en tiempo de ejecución.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.BLUETOOTH,
                    android.Manifest.permission.BLUETOOTH_ADMIN
            }, REQUEST_BLUETOOTH_PERMISSIONS);
        } else {
            // El permiso ya se concedió
            startBluetoothDiscovery();
        }
    }catch(Exception e){
        Toast.makeText(this, "Error:"+e, Toast.LENGTH_SHORT).show();
    }

    }

    // Método para iniciar el descubrimiento de dispositivos Bluetooth
    @SuppressLint("MissingPermission")
    private void startBluetoothDiscovery() {
        // Registra un BroadcastReceiver para descubrir dispositivos disponibles
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        // Inicia la búsqueda de dispositivos Bluetooth
        bluetoothAdapter.startDiscovery();

        // Muestra la barra de progreso mientras se realiza la búsqueda
        progressBar.setVisibility(View.VISIBLE);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    // Agrega el dispositivo a la lista
                    deviceArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    deviceArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @SuppressLint("MissingPermission")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detén la búsqueda de dispositivos y desregistra el BroadcastReceiver
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(receiver);
    }
}
