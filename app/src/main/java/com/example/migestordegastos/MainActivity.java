package com.example.migestordegastos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.migestordegastos.activities.CategoriasActivity;
import com.example.migestordegastos.activities.DetalleGastoActivity;
import com.example.migestordegastos.activities.EstadisticasActivity;
import com.example.migestordegastos.activities.FormularioGastoActivity;
import com.example.migestordegastos.adapters.GastoAdapter;
import com.example.migestordegastos.database.DatabaseHelper;
import com.example.migestordegastos.models.Categoria;
import com.example.migestordegastos.models.Gasto;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Vistas
    ListView listViewGastos;
    Button btnAgregarGasto, btnCategorias, btnEstadisticas;
    Button btnOrdenFecha, btnOrdenCantidad;
    Spinner spinnerFiltro;

    // Base de datos
    DatabaseHelper databaseHelper;

    // Listas
    List<Gasto> listaGastos;
    List<Categoria> listaCategorias;

    // Adaptadores
    GastoAdapter adapter;

    // Orden actual
    String ordenActual = "fecha";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        listViewGastos = findViewById(R.id.listViewGastos);
        btnAgregarGasto = findViewById(R.id.btnAgregarGasto);
        btnCategorias = findViewById(R.id.btnCategorias);
        btnEstadisticas = findViewById(R.id.btnEstadisticas);
        spinnerFiltro = findViewById(R.id.spinnerFiltroCategorias);
        btnOrdenFecha = findViewById(R.id.btnOrdenFecha);
        btnOrdenCantidad = findViewById(R.id.btnOrdenCantidad);

        databaseHelper = new DatabaseHelper(this);

        // Cargar datos iniciales
        cargarCategoriasFiltro();
        cargarGastos();

        // Botones de orden
        btnOrdenFecha.setOnClickListener(v -> {
            ordenActual = "fecha";
            cargarGastos();
        });

        btnOrdenCantidad.setOnClickListener(v -> {
            ordenActual = "cantidad";
            cargarGastos();
        });

        // Navegación
        btnAgregarGasto.setOnClickListener(v ->
                startActivity(new Intent(this, FormularioGastoActivity.class)));

        btnCategorias.setOnClickListener(v ->
                startActivity(new Intent(this, CategoriasActivity.class)));

        btnEstadisticas.setOnClickListener(v ->
                startActivity(new Intent(this, EstadisticasActivity.class)));

        // Click en un gasto → ir a detalle
        listViewGastos.setOnItemClickListener((parent, view, position, id) -> {

            Gasto gasto = listaGastos.get(position);

            Intent intent = new Intent(this, DetalleGastoActivity.class);
            intent.putExtra("id", gasto.getId());
            intent.putExtra("concepto", gasto.getConcepto());
            intent.putExtra("cantidad", gasto.getCantidad());
            intent.putExtra("fecha", gasto.getFecha());
            intent.putExtra("descripcion", gasto.getDescripcion());
            intent.putExtra("id_categoria", gasto.getIdCategoria());

            startActivity(intent);
        });

        // Filtro por categoría
        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    cargarGastos();
                }else{
                    int idCategoria = listaCategorias.get(position - 1).getId();
                    cargarGastosPorCategoria(idCategoria);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarGastos(); // recarga al volver a la pantalla
    }

    // =========================
    // Cargar todos los gastos
    // =========================
    private void cargarGastos(){

        listaGastos = obtenerGastos(null);

        adapter = new GastoAdapter(this, listaGastos);
        listViewGastos.setAdapter(adapter);
    }

    // =========================
    // Cargar gastos por categoría
    // =========================
    private void cargarGastosPorCategoria(int idCategoria){

        listaGastos = obtenerGastos(idCategoria);

        adapter = new GastoAdapter(this, listaGastos);
        listViewGastos.setAdapter(adapter);
    }

    // =========================
    // Método común para obtener gastos
    // =========================
    private List<Gasto> obtenerGastos(Integer idCategoria){

        List<Gasto> lista = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query;
        String[] args = null;

        if(idCategoria == null){
            query = "SELECT * FROM gastos ORDER BY " + ordenActual + " DESC";
        } else {
            query = "SELECT * FROM gastos WHERE id_categoria=? ORDER BY " + ordenActual + " DESC";
            args = new String[]{String.valueOf(idCategoria)};
        }

        Cursor cursor = db.rawQuery(query, args);

        if(cursor.moveToFirst()){
            do{
                lista.add(new Gasto(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getDouble(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getInt(6)
                ));
            }while(cursor.moveToNext());
        }

        cursor.close();
        return lista;
    }

    // =========================
    // Cargar categorías en spinner
    // =========================
    private void cargarCategoriasFiltro(){

        listaCategorias = new ArrayList<>();
        List<String> nombres = new ArrayList<>();
        nombres.add("Todas");

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM categorias", null);

        if(cursor.moveToFirst()){
            do{
                listaCategorias.add(new Categoria(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                ));
                nombres.add(cursor.getString(1));
            }while(cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nombres
        );

        spinnerFiltro.setAdapter(adapter);
    }
}
