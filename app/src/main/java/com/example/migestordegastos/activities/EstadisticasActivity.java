package com.example.migestordegastos.activities;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.migestordegastos.R;
import com.example.migestordegastos.database.DatabaseHelper;
public class EstadisticasActivity extends AppCompatActivity {

    TextView tvTotalGastado, tvNumeroGastos, tvGastoMedio, tvGastosPorCategoria;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        // Inicializar vistas
        tvTotalGastado = findViewById(R.id.tvTotalGastado);
        tvNumeroGastos = findViewById(R.id.tvNumeroGastos);
        tvGastoMedio = findViewById(R.id.tvGastoMedio);
        tvGastosPorCategoria = findViewById(R.id.tvGastosPorCategoria);

        databaseHelper = new DatabaseHelper(this);

        // Botón atrás
        findViewById(R.id.btBack).setOnClickListener(v -> finish());

        // Cargar datos
        cargarEstadisticas();
        cargarGastosPorCategoria();
    }

    // =========================
    // Estadísticas generales
    // =========================
    private void cargarEstadisticas(){

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(cantidad), COUNT(id), AVG(cantidad) FROM gastos",
                null
        );

        if(cursor.moveToFirst()){

            double total = cursor.getDouble(0);
            int numero = cursor.getInt(1);
            double media = cursor.getDouble(2);

            tvTotalGastado.setText(total + " €");
            tvNumeroGastos.setText(String.valueOf(numero));
            tvGastoMedio.setText(media + " €");
        }

        cursor.close();
    }

    // =========================
    // Estadísticas por categoría
    // =========================
    private void cargarGastosPorCategoria(){

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT categorias.nombre, COUNT(gastos.id) " +
                        "FROM gastos " +
                        "INNER JOIN categorias ON gastos.id_categoria = categorias.id " +
                        "GROUP BY categorias.nombre",
                null
        );

        StringBuilder resultado = new StringBuilder();

        if(cursor.moveToFirst()){
            do{
                resultado.append("• ")
                        .append(cursor.getString(0))
                        .append(": ")
                        .append(cursor.getInt(1))
                        .append("\n");
            }while(cursor.moveToNext());
        }

        cursor.close();

        tvGastosPorCategoria.setText(resultado.toString());
    }
}
