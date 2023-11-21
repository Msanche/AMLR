package com.example.amlr.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DbHelper2 extends SQLiteOpenHelper {

    Context context;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RegistroActividades.db";

    public static final String TABLE_ACTIVIDADES = "t_actividades";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FECHA_HORA = "fecha_hora";
    public static final String COLUMN_MOVIMIENTO = "movimiento";

    public DbHelper2(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ACTIVIDADES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_FECHA_HORA + " TEXT," +
                COLUMN_MOVIMIENTO + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVIDADES);
        onCreate(db);
    }

    // Método para insertar una actividad en la base de datos
    public long insertRegistro(String fechaHora, String movimiento) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FECHA_HORA, fechaHora);
        values.put(COLUMN_MOVIMIENTO, movimiento);

        return db.insert(TABLE_ACTIVIDADES, null, values);
    }

    // Método para obtener todas las actividades de la base de datos
    public Cursor getAllRegistros() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ACTIVIDADES, null, null, null, null, null, null);
    }

    // Método para obtener una actividad específica por su ID
    public Cursor getRegistroById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ACTIVIDADES, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
    }

    // Método para eliminar todas las actividades
    public void deleteAllRegistros() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACTIVIDADES, null, null);
    }

    // Método para eliminar una actividad específica por su ID
    public void deleteRegistro(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACTIVIDADES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Método para obtener la marca de tiempo actual
    private String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}


