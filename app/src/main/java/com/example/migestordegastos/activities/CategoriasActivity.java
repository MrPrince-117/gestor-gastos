package com.example.migestordegastos.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.migestordegastos.R;
import com.example.migestordegastos.adapters.CategoriaAdapter;
import com.example.migestordegastos.database.DatabaseHelper;
import com.example.migestordegastos.models.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriasActivity extends AppCompatActivity {

    EditText etNombreCategoria;
    Button btnAgregarCategoria, btnSeleccionarIcono;
    ListView listViewCategorias;

    DatabaseHelper databaseHelper;

    List<Categoria> listaCategorias;
    CategoriaAdapter adapter;

    // Icono por defecto
    String iconoSeleccionado = "ic_comida";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        // Inicializar vistas
        etNombreCategoria = findViewById(R.id.etNombreCategoria);
        btnAgregarCategoria = findViewById(R.id.btnAgregarCategoria);
        btnSeleccionarIcono = findViewById(R.id.btnSeleccionarIcono);
        listViewCategorias = findViewById(R.id.listViewCategorias);

        databaseHelper = new DatabaseHelper(this);

        // Botón atrás
        findViewById(R.id.btBack).setOnClickListener(v -> finish());

        // Seleccionar icono
        btnSeleccionarIcono.setOnClickListener(v -> abrirSelectorIconos());

        // Añadir categoría
        btnAgregarCategoria.setOnClickListener(v -> agregarCategoria());

        // Cargar lista
        cargarCategorias();
    }

    // =========================
    // Abrir pantalla de iconos
    // =========================
    private void abrirSelectorIconos(){

        Intent intent = new Intent(this, SeleccionarIconoActivity.class);
        startActivityForResult(intent, 1);
    }

    // =========================
    // Añadir categoría
    // =========================
    private void agregarCategoria(){

        String nombre = etNombreCategoria.getText().toString();

        if(nombre.isEmpty()){
            Toast.makeText(this,"Introduce un nombre",Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("icono", iconoSeleccionado);

        db.insert("categorias", null, values);

        etNombreCategoria.setText("");

        cargarCategorias(); // refrescar lista
    }

    // =========================
    // Cargar categorías
    // =========================
    private void cargarCategorias(){

        listaCategorias = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categorias", null);

        if(cursor.moveToFirst()){
            do{
                listaCategorias.add(new Categoria(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                ));
            }while(cursor.moveToNext());
        }

        cursor.close();

        adapter = new CategoriaAdapter(this, listaCategorias);
        listViewCategorias.setAdapter(adapter);
    }

    // =========================
    // Recibir icono seleccionado
    // =========================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            iconoSeleccionado = data.getStringExtra("icono");

            Toast.makeText(this, "Icono: " + iconoSeleccionado, Toast.LENGTH_SHORT).show();
        }
    }
}




