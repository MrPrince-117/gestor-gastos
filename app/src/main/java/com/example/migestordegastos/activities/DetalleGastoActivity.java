package com.example.migestordegastos.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.migestordegastos.R;
import com.example.migestordegastos.database.DatabaseHelper;
public class DetalleGastoActivity extends AppCompatActivity {

    TextView tvConceptoDetalle, tvCategoriaDetalle, tvCantidadDetalle, tvFechaDetalle, tvDescripcionDetalle;
    ImageView imgCategoriaDetalle;
    Button btnEliminarGasto, btnEditarGasto;

    DatabaseHelper databaseHelper;

    int idGasto, idCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_gasto);

        // Inicializar vistas
        tvConceptoDetalle = findViewById(R.id.tvConceptoDetalle);
        tvCantidadDetalle = findViewById(R.id.tvCantidadDetalle);
        tvFechaDetalle = findViewById(R.id.tvFechaDetalle);
        tvDescripcionDetalle = findViewById(R.id.tvDescripcionDetalle);
        tvCategoriaDetalle = findViewById(R.id.tvCategoriaDetalle);
        imgCategoriaDetalle = findViewById(R.id.imgCategoriaDetalle);

        btnEliminarGasto = findViewById(R.id.btnEliminarGasto);
        btnEditarGasto = findViewById(R.id.btnEditarGasto);

        databaseHelper = new DatabaseHelper(this);

        // Botón atrás
        findViewById(R.id.btBack).setOnClickListener(v -> finish());

        // Obtener datos del intent
        idGasto = getIntent().getIntExtra("id", -1);
        idCategoria = getIntent().getIntExtra("id_categoria", -1);

        String concepto = getIntent().getStringExtra("concepto");
        double cantidad = getIntent().getDoubleExtra("cantidad", 0);
        String fecha = getIntent().getStringExtra("fecha");
        String descripcion = getIntent().getStringExtra("descripcion");

        // Mostrar datos básicos
        tvConceptoDetalle.setText(concepto);
        tvCantidadDetalle.setText(cantidad + " €");
        tvFechaDetalle.setText("Fecha: " + fecha);
        tvDescripcionDetalle.setText(descripcion);

        // Cargar categoría e icono
        cargarCategoria();

        // Botones
        btnEliminarGasto.setOnClickListener(v -> eliminarGasto());
        btnEditarGasto.setOnClickListener(v -> editarGasto());
    }

    // =========================
    // Obtener categoría e icono desde BD
    // =========================
    private void cargarCategoria(){

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT nombre, icono FROM categorias WHERE id=?",
                new String[]{String.valueOf(idCategoria)}
        );

        if(cursor.moveToFirst()){
            String nombre = cursor.getString(0);
            String icono = cursor.getString(1);

            tvCategoriaDetalle.setText("Categoría: " + nombre);

            int iconoId = getResources().getIdentifier(
                    icono,
                    "drawable",
                    getPackageName()
            );

            if(iconoId != 0){
                imgCategoriaDetalle.setImageResource(iconoId);
            }
        }

        cursor.close();
    }

    // =========================
    // Ir a editar gasto
    // =========================
    private void editarGasto(){

        Intent intent = new Intent(this, FormularioGastoActivity.class);

        intent.putExtra("modo", "editar");
        intent.putExtra("id", idGasto);
        intent.putExtra("concepto", tvConceptoDetalle.getText().toString());
        intent.putExtra("cantidad", tvCantidadDetalle.getText().toString().replace(" €",""));
        intent.putExtra("fecha", tvFechaDetalle.getText().toString().replace("Fecha: ",""));
        intent.putExtra("descripcion", tvDescripcionDetalle.getText().toString());

        startActivity(intent);
    }

    // =========================
    // Eliminar gasto
    // =========================
    private void eliminarGasto(){

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int resultado = db.delete("gastos", "id=?", new String[]{String.valueOf(idGasto)});

        if(resultado > 0){
            Toast.makeText(this, "Gasto eliminado", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
        }
    }
}
