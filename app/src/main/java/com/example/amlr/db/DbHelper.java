package com.example.amlr.db;

import android.content.Context;
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

    public boolean editar_usuario(String correo, String password){
        boolean correcto = false;

        DbHelper helper = new DbHelper(context,"Cerradura.db",null,6);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            db.execSQL("UPDATE " +TABLE_USUARIOS+ " SET password = '" + password +"' WHERE correo = '" +
                    correo + "' ");
            correcto = true;
        }catch (Exception e){
            e.toString();
            Toast.makeText(context, "db"+e, Toast.LENGTH_LONG).show();
            correcto = false;
        }finally {
            db.close();
        }

        return correcto;
    }

    public boolean changePassword(String user, String password){
        boolean correcto = false;

        DbHelper helper = new DbHelper(context,"Cerradura.db",null,6);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_USUARIOS + " SET password = '" + password + "' WHERE nombre = '" + user + "' ");
            correcto = true;
        }catch (Exception e){
            e.toString();
            Toast.makeText(context, "db"+e, Toast.LENGTH_LONG).show();
            correcto = false;
        }finally {
            db.close();
        }

        return correcto;
    }


}
