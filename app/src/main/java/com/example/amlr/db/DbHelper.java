package com.example.amlr.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.amlr.Restablecer_password;
import com.example.amlr.change_password;
import com.example.amlr.entidades.Actividades;

import java.util.ArrayList;
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

    /*sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_REGISTRO + "(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre_us TEXT NOT NULL," +
            "intentos_f INTEGER NOT NULL," +
            "acciones TEXT NOT NULL," +
            "fecha_y_hora TEXT NOT NULL)");
    }*/
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_REGISTRO + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "accion TEXT NOT NULL," +
                "fecha_y_hora TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("DROP TABLE " + TABLE_USUARIOS);
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

    public boolean validarPIN(String usuario, String pin) {
        boolean pinValido = false;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT passwordC FROM " + TABLE_USUARIOS + " WHERE nombre = ?", new String[]{usuario});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String passwordC = cursor.getString(cursor.getColumnIndex("passwordC"));
            if (pin.equals(passwordC)) {
                pinValido = true;
            }
        }

        cursor.close();
        db.close();

        return pinValido;
    }

    public ArrayList<Actividades> mostrarAcciones(){
        DbHelper helper = new DbHelper(context,"Cerradura.db",null,6);
        SQLiteDatabase db = helper.getWritableDatabase();

        ArrayList<Actividades> listaActividades = new ArrayList<>();
        Actividades actividad = null;
        Cursor cursorActividades = null;

        cursorActividades = db.rawQuery("SELECT * FROM " + TABLE_REGISTRO, null);

        if(cursorActividades.moveToFirst()){
            do{
                actividad = new Actividades();
                actividad.setId(cursorActividades.getInt(0));
                actividad.setAccion(cursorActividades.getString(1));
                actividad.setFecha_y_hora(cursorActividades.getString(2));
                listaActividades.add(actividad);
            }while(cursorActividades.moveToNext());
        }

        cursorActividades.close();

        return listaActividades;
    }


}
