package com.example.migestordegastos.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gestor_gastos.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    // Tabla usuarios
    db.execSQL("CREATE TABLE usuarios (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre_usuario TEXT," +
            "contrasena TEXT)");

    // Tabla categorias
    db.execSQL("CREATE TABLE categorias (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre TEXT," +
            "icono TEXT)");

    // Tabla gastos
    db.execSQL("CREATE TABLE gastos (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "concepto TEXT," +
            "cantidad REAL," +
            "fecha TEXT," +
            "descripcion TEXT," +
            "id_categoria INTEGER," +
            "id_usuario INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS categorias");
        db.execSQL("DROP TABLE IF EXISTS gastos");

        onCreate(db);
    }

}
