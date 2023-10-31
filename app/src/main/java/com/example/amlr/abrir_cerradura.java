package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import com.example.amlr.db.DbHelper;

public class abrir_cerradura extends AppCompatActivity {

    private EditText[] editTexts = new EditText[4]; // Array para almacenar los EditText

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice hc05Device; // Agrega tu dispositivo HC-05 aquí
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private Button removeButton;
    private String usuario;
    private String pass;

    ImageButton back;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_cerradura);

        editTexts[0] = findViewById(R.id.Number1);
        editTexts[1] = findViewById(R.id.Number2);
        editTexts[2] = findViewById(R.id.Number3);
        editTexts[3] = findViewById(R.id.Number4);

        // Configura los Click Listeners para los botones numéricos
        Button buttonZero = findViewById(R.id.ButtonZero);
        Button removeButton = findViewById(R.id.Remove);

        buttonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber("0");
            }
        });

        Button buttonOne = findViewById(R.id.ButtonOne);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber("1");
            }
        });

        Button buttonTwo = findViewById(R.id.ButtonTwo);
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber("2");
            }
        });

        Button buttonThree = findViewById(R.id.ButtonThree);
        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber("3");
            }
        });

        Button buttonFour = findViewById(R.id.ButtonFour);
        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber("4");
            }
        });

        Button buttonFive = findViewById(R.id.ButtonFive);
        buttonFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber("5");
            }
        });

        Button buttonSix = findViewById(R.id.ButtonSix);
        buttonSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber("6");
            }
        });

        Button buttonSeven = findViewById(R.id.ButtonSeven);
        buttonSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber("7");
            }
        });

        Button buttonEight = findViewById(R.id.ButtonEight);
        buttonEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber("8");
            }
        });

        Button buttonNine = findViewById(R.id.ButtonNine);
        buttonNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertNumber("9");
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeNumber();
            }
        });

        back = findViewById(R.id.back_button);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(abrir_cerradura.this, Menu.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("pass", pass);
                startActivity(intent);
            }
        });

        editTexts[3].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validarPIN(); // Llama a la función de validación cuando se ingrese el número en el cuarto EditText.
                    return true;
                }
                return false;
            }
        });
    }

    private void insertNumber(String number) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().isEmpty()) {
                editText.setText(number);
                break; // Inserta el número en el primer EditText vacío y sale del bucle
            }
        }
    }

    private void removeNumber() {
        for (int i = editTexts.length - 1; i >= 0; i--) {
            EditText editText = editTexts[i];
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                editText.setText(""); // Elimina el número del último EditText con contenido y sale del bucle
                break;
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
    private void validarPIN() {
        StringBuilder pinBuilder = new StringBuilder();

        // Construye el PIN a partir de los EditText
        for (EditText editText : editTexts) {
            pinBuilder.append(editText.getText().toString());
        }

        String enteredPIN = pinBuilder.toString();

        // Realiza la validación del PIN con el campo passwordC de la base de datos
        DbHelper dbHelper = new DbHelper(this, "Cerradura.db", null, 6);
        boolean pinValido = dbHelper.validarPIN(usuario, enteredPIN);

        if (pinValido) {
            // El PIN ingresado es válido
            enviarUnoPorBluetooth(); // Enviar "1" por Bluetooth
        } else {
            Toast.makeText(this, "PIN incorrecto", Toast.LENGTH_SHORT).show();
        }
    }
}
