package com.example.amlr.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.amlr.Restablecer_password;
import com.example.amlr.change_password;

import java.util.concurrent.ExecutionException;

public class DbHelper extends SQLiteOpenHelper {

    Context context;

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NOMBRE = "Cerradura.db";

    public static final String TABLE_USUARIOS = "t_usuarios";

    public static final String TABLE_REGISTRO = "t_registro";



    public DbHelper(@Nullable Context context, String nombre, SQLiteDatabase.CursorFactory factory , int version) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USUARIOS + "(" +
            "nombre TEXT PRIMARY KEY," +
            "correo TEXT NOT NULL," +
            "password TEXT NOT NULL," +
            "passwordC TEXT NOT NULL)");

    sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_REGISTRO + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre_us TEXT NOT NULL," +
            "intentos_f INTEGER NOT NULL," +
            "acciones TEXT NOT NULL," +
            "fecha_y_hora TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("DROP TABLE " + TABLE_USUARIOS);
    sqLiteDatabase.execSQL("DROP TABLE " + TABLE_REGISTRO);
    onCreate(sqLiteDatabase);
    }


    /**
     * Modulo:editar_usuario
     * Método para actualizar la contraseña de los usuarios en base al correo
     * @param correo
     * @param password
     * @return true si la contraseña se editó correctamente, false en caso de error.
     *
     * Autor:Ramses
     * Fecha:05/10/2023
     */
    public boolean editar_usuario(String correo, String password){
        boolean correcto = false;
        // Se crea un objeto DbHelper para acceder a la base de datos.
        DbHelper helper = new DbHelper(context,"Cerradura.db",null,6);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            // Se ejecuta una sentencia SQL para actualizar la contraseña del usuario.
            db.execSQL("UPDATE " +TABLE_USUARIOS+ " SET password = '" + password +"' WHERE correo = '" +
                    correo + "' ");
            correcto = true;
        }catch (Exception e){
            // En caso de error, se muestra un mensaje de error.
            e.toString();
            Toast.makeText(context, "db"+e, Toast.LENGTH_LONG).show();
            correcto = false;
        }finally {
            // Se cierra la base de datos.
            db.close();
        }

        return correcto;
    }


    /**
     * Metodo:changePassword
     * Este método permite cambiar la contraseña de un usuario en la base de datos en ase al usuario.
     *
     * @param user El nombre de usuario del usuario cuya contraseña se desea cambiar.
     * @param password La nueva contraseña que se asignará al usuario.
     * @return true si la contraseña se cambió correctamente, false en caso de error.
     *
     * Autor:Marko
     * Fecha:10/10/2023
     */
    public boolean changePassword(String user, String password){
        boolean correcto = false;

        // Se crea un objeto DbHelper para acceder a la base de datos.

        DbHelper helper = new DbHelper(context,"Cerradura.db",null,6);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            // Se ejecuta una sentencia SQL para actualizar la contraseña del usuario.
            db.execSQL("UPDATE " + TABLE_USUARIOS + " SET password = '" + password + "' WHERE nombre = '" + user + "' ");
            correcto = true;
        }catch (Exception e){
            // En caso de error, se muestra un mensaje de error.
            e.toString();
            Toast.makeText(context, "db"+e, Toast.LENGTH_LONG).show();
            correcto = false;
        }finally {
            // Se cierra la base de datos.
            db.close();
        }

        return correcto;
    }


    public boolean changePIN(String user, String password){
        boolean correcto = false;

        // Se crea un objeto DbHelper para acceder a la base de datos.

        DbHelper helper = new DbHelper(context,"Cerradura.db",null,6);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            // Se ejecuta una sentencia SQL para actualizar la contraseña del usuario.
            db.execSQL("UPDATE " + TABLE_USUARIOS + " SET passwordC = '" + password + "' WHERE nombre = '" + user + "' ");
            correcto = true;
        }catch (Exception e){
            // En caso de error, se muestra un mensaje de error.
            e.toString();
            Toast.makeText(context, "db"+e, Toast.LENGTH_LONG).show();
            correcto = false;
        }finally {
            // Se cierra la base de datos.
            db.close();
        }

        return correcto;
    }

    public boolean validarUsuario(String usuario, String pass) {
        // Crea una instancia de tu DbHelper
        DbHelper dbHelper = new DbHelper(context, "Cerradura.db", null, 6);

        // Obtén una instancia de la base de datos en modo lectura
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define la proyección (las columnas que deseas recuperar)
        String[] projection = {
                "nombre",
                "password"
        };

        // Filtra los resultados WHERE "nombre" = usuario AND "password" = pass
        String selection = "nombre = ? AND password = ?";
        String[] selectionArgs = {usuario, pass};

        // Realiza la consulta
        Cursor cursor = db.query(
                "t_usuarios",     // Nombre de la tabla
                projection,       // Columnas que deseas recuperar
                selection,        // Cláusula WHERE
                selectionArgs,    // Argumentos de la cláusula WHERE
                null,             // GROUP BY
                null,             // HAVING
                null              // ORDER BY
        );

        boolean usuarioValido = cursor.moveToFirst(); // Devuelve true si se encontró un registro

        // Cierra la conexión a la base de datos
        cursor.close();
        db.close();

        return usuarioValido;
    }


}
