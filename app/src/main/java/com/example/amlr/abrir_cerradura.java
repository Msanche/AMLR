package com.example.amlr;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.io.InputStream;
import android.os.Handler;
import com.example.amlr.db.DbHelper;

public class abrir_cerradura extends AppCompatActivity{

    private EditText[] editTexts = new EditText[4]; // Array para almacenar los EditText
    //PARA MANEJO DE BLOQUEOS
    private int failedAttempts = 0;
    private boolean isLocked = false;
    private Handler handler = new Handler();
    //TERMINA MANEJO DE BLOQUEOS
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice hc05Device; // Agrega tu dispositivo HC-05 aquí
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private String usuario;
    private String pass;

    ImageButton back;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abrir_cerradura);

        recibirDatos(); // Asegúrate de haber definido el método recibirDatos para obtener usuario y pass.

        editTexts[0] = findViewById(R.id.Number1);
        editTexts[1] = findViewById(R.id.Number2);
        editTexts[2] = findViewById(R.id.Number3);
        editTexts[3] = findViewById(R.id.Number4);

        // Configura los Click Listeners para los botones numéricos
        Button buttonZero = findViewById(R.id.ButtonZero);
        Button removeButton = findViewById(R.id.Remove);
        Button buttonDone = findViewById(R.id.buttonDone);
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


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            // El dispositivo no admite Bluetooth, maneja esto según tus necesidades.
            Toast.makeText(this, "El dispositivo no admite Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        } else {
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
                    if (bluetoothSocket == null || !bluetoothSocket.isConnected()) {
                        // Intenta conectar solo si no hay una conexión Bluetooth activa
                        try {
                            hc05Device = bluetoothAdapter.getRemoteDevice("98:D3:33:80:B4:69");
                            //hc05Device = bluetoothAdapter.getRemoteDevice("20:16:07:25:19:87");
                            // Luego puedes intentar establecer una conexión con el dispositivo HC-05.
                            bluetoothSocket = hc05Device.createRfcommSocketToServiceRecord(MY_UUID);
                            bluetoothSocket.connect();
                            outputStream = bluetoothSocket.getOutputStream();

                            // Obtener el InputStream del BluetoothSocket
                            InputStream inputStream = bluetoothSocket.getInputStream();

                            // Configurar un tiempo de espera en milisegundos
                            int timeout = 10000; // 10 segundos

                            // Utilizar un Handler para imponer el límite de tiempo
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Este código se ejecutará después de 'timeout' milisegundos

                                }
                            }, timeout);

                            // Continuar con la lectura de datos desde inputStream
                            // ...
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error al conectar con el HC-05" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }



        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarPIN();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(abrir_cerradura.this, Menu.class);
                intent.putExtra("usuario", usuario);
                intent.putExtra("pass", pass);
                startActivity(intent);
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

    //*******************************************************************************************************************************************************
    private void enviarUnoPorBluetooth(char data) {
        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Mueve la desconexión Bluetooth aquí, para cerrar la conexión al salir de la actividad
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
        if (isLocked) {
            return; // Si la cerradura está bloqueada, no valides el PIN.
        }
        StringBuilder pinBuilder = new StringBuilder();

        // Construye el PIN a partir de los EditText
        for (EditText editText : editTexts) {
            pinBuilder.append(editText.getText().toString());
        }

        String enteredPIN = pinBuilder.toString();

        Toast.makeText(this, "PIN introducido: " + enteredPIN, Toast.LENGTH_SHORT).show();  // Agrega este log para verificar el PIN introducido

        // Realiza la validación del PIN con el campo passwordC de la base de datos
        DbHelper dbHelper = new DbHelper(this, "Cerradura.db", null, 6);
        boolean pinValido = dbHelper.validarPIN(usuario, enteredPIN);

        if (pinValido) {
            Toast.makeText(this, "PIN válido", Toast.LENGTH_SHORT).show(); // Agrega este log para verificar que el PIN es válido
            // El PIN ingresado es válido
            //**********************************************************************************************************************************************
            enviarUnoPorBluetooth('1'); // Enviar "1" por Bluetooth
            Toast.makeText(this, "Se hizo el enviarUnoPorBluetooth", Toast.LENGTH_SHORT).show();
            //Insertar en base de datos
            String actividad = "Se abrió la cerradura";
            String fechaHora = getCurrentTimeStamp();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            /*if (db != null) {
                // Mostrar mensaje si la base de datos se creó correctamente
                Toast.makeText(Crear_Cuenta.this, "BASE DE DATOS CREADA", Toast.LENGTH_LONG).show();
            } else {
                // Mostrar mensaje si hubo un error al crear la base de datos
                Toast.makeText(Crear_Cuenta.this, "ERROR AL CREAR LA BASE DE DATOS", Toast.LENGTH_LONG).show();
            }
             */
            ContentValues datosActividad = new ContentValues();
            datosActividad.put("accion", actividad);
            datosActividad.put("fecha_y_hora", fechaHora);
            try {
                // Insertar los datos del usuario en la base de datos
                dbHelper.getWritableDatabase().insert("t_registro", null, datosActividad);
                dbHelper.getWritableDatabase().close();
                // Mostrar mensaje de éxito y redirigir a la actividad principal
                Toast.makeText(abrir_cerradura.this, "Datos agregados", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Manejar posibles excepciones y mostrar un mensaje de error
                Toast.makeText(abrir_cerradura.this, "Error al insertar datos" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            // Incrementa el contador de intentos fallidos
            failedAttempts++;

            if (failedAttempts >= 3) {
                bloquearCerradura();
            } else {
                Toast.makeText(this, "PIN incorrecto. Intento " + failedAttempts + " de 3.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void bloquearCerradura() {
        isLocked = true;
        Toast.makeText(this, "La cerradura se bloqueará durante 30 segundos", Toast.LENGTH_SHORT).show();
        //Insertar en base de datos
        DbHelper dbHelper = new DbHelper(this, "Cerradura.db", null, 6);
        String actividad = "Se bloqueó la cerradura";
        String fechaHora = getCurrentTimeStamp();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
            /*if (db != null) {
                // Mostrar mensaje si la base de datos se creó correctamente
                Toast.makeText(Crear_Cuenta.this, "BASE DE DATOS CREADA", Toast.LENGTH_LONG).show();
            } else {
                // Mostrar mensaje si hubo un error al crear la base de datos
                Toast.makeText(Crear_Cuenta.this, "ERROR AL CREAR LA BASE DE DATOS", Toast.LENGTH_LONG).show();
            }
             */
        ContentValues datosActividad = new ContentValues();
        datosActividad.put("accion", actividad);
        datosActividad.put("fecha_y_hora", fechaHora);
        try {
            // Insertar los datos del usuario en la base de datos
            dbHelper.getWritableDatabase().insert("t_registro", null, datosActividad);
            dbHelper.getWritableDatabase().close();
            // Mostrar mensaje de éxito y redirigir a la actividad principal
            Toast.makeText(abrir_cerradura.this, "Datos agregados", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Manejar posibles excepciones y mostrar un mensaje de error
            Toast.makeText(abrir_cerradura.this, "Error al insertar datos" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        // Inicia un temporizador para desbloquear la cerradura después de 30 segundos
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                desbloquearCerradura();
            }
        }, 30000);
    }


    private void desbloquearCerradura() {
        isLocked = false;
        failedAttempts = 0;
        Toast.makeText(this, "La cerradura se ha desbloqueado", Toast.LENGTH_SHORT).show();
    }

    // Método para obtener la marca de tiempo actual
    private String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

}
