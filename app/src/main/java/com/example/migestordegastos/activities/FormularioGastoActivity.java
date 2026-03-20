package com.example.migestordegastos.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.migestordegastos.R;
import com.example.migestordegastos.database.DatabaseHelper;
import com.example.migestordegastos.models.Categoria;

import java.util.ArrayList;
import java.util.List;


public class FormularioGastoActivity extends AppCompatActivity {

    EditText etConcepto, etCantidad, etFecha, etDescripcion;
    Spinner spCategorias;
    Button btnGuardarGasto;

    DatabaseHelper databaseHelper;

    List<Categoria> listaCategorias;

    String modo = "crear";
    int idGasto = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_gasto);

        // Inicializar vistas
        etConcepto = findViewById(R.id.etConcepto);
        etCantidad = findViewById(R.id.etCantidad);
        etFecha = findViewById(R.id.etFecha);
        etDescripcion = findViewById(R.id.etDescripcion);
        spCategorias = findViewById(R.id.spinnerCategorias);
        btnGuardarGasto = findViewById(R.id.btnGuardarGasto);

        databaseHelper = new DatabaseHelper(this);

        // Botón atrás
        findViewById(R.id.btBack).setOnClickListener(v -> finish());

        cargarCategorias();
        configurarFecha();

        // Detectar si estamos editando
        if(getIntent().hasExtra("modo")){
            modo = getIntent().getStringExtra("modo");

            if(modo.equals("editar")){
                cargarDatosEditar();
            }
        }

        // Guardar o actualizar
        btnGuardarGasto.setOnClickListener(v -> {
            if(modo.equals("editar")){
                actualizarGasto();
            }else{
                guardarGasto();
            }
        });
    }

    // =========================
    // Cargar datos en modo editar
    // =========================
    private void cargarDatosEditar(){

        idGasto = getIntent().getIntExtra("id",-1);

        etConcepto.setText(getIntent().getStringExtra("concepto"));
        etCantidad.setText(getIntent().getStringExtra("cantidad"));
        etFecha.setText(getIntent().getStringExtra("fecha"));
        etDescripcion.setText(getIntent().getStringExtra("descripcion"));

        btnGuardarGasto.setText("Actualizar gasto");
    }

    // =========================
    // Formato automático de fecha
    // =========================
    private void configurarFecha(){

        etFecha.addTextChangedListener(new android.text.TextWatcher() {

            boolean updating;

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(android.text.Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(updating) return;

                String texto = s.toString().replace("/", "");
                if(texto.length() > 6) return;

                StringBuilder formato = new StringBuilder();

                for(int i = 0; i < texto.length(); i++){
                    formato.append(texto.charAt(i));
                    if((i == 1 || i == 3) && i != texto.length() - 1){
                        formato.append("/");
                    }
                }

                updating = true;
                etFecha.setText(formato.toString());
                etFecha.setSelection(formato.length());
                updating = false;
            }
        });
    }

    // =========================
    // Obtener datos del formulario
    // =========================
    private boolean validarDatos(String concepto, String cantidad, String fecha){

        if(concepto.isEmpty() || cantidad.isEmpty() || fecha.isEmpty()){
            Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!fecha.matches("\\d{2}/\\d{2}/\\d{2,4}")){
            Toast.makeText(this,"Fecha inválida",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // =========================
    // Guardar gasto
    // =========================
    private void guardarGasto(){

        String concepto = etConcepto.getText().toString();
        String cantidadTexto = etCantidad.getText().toString();
        String fecha = etFecha.getText().toString();
        String descripcion = etDescripcion.getText().toString();

        if(!validarDatos(concepto, cantidadTexto, fecha)) return;

        double cantidad = Double.parseDouble(cantidadTexto);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("concepto", concepto);
        values.put("cantidad", cantidad);
        values.put("fecha", fecha);
        values.put("descripcion", descripcion);

        // Obtener categoría seleccionada
        Categoria categoria = listaCategorias.get(spCategorias.getSelectedItemPosition());
        values.put("id_categoria", categoria.getId());
        values.put("id_usuario", 1);

        long resultado = db.insert("gastos", null, values);

        if(resultado != -1){
            Toast.makeText(this, "Gasto guardado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // =========================
    // Actualizar gasto
    // =========================
    private void actualizarGasto(){

        String concepto = etConcepto.getText().toString();
        String cantidadTexto = etCantidad.getText().toString();
        String fecha = etFecha.getText().toString();
        String descripcion = etDescripcion.getText().toString();

        if(!validarDatos(concepto, cantidadTexto, fecha)) return;

        double cantidad = Double.parseDouble(cantidadTexto);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("concepto", concepto);
        values.put("cantidad", cantidad);
        values.put("fecha", fecha);
        values.put("descripcion", descripcion);

        Categoria categoria = listaCategorias.get(spCategorias.getSelectedItemPosition());
        values.put("id_categoria", categoria.getId());

        db.update("gastos", values, "id=?", new String[]{String.valueOf(idGasto)});

        Toast.makeText(this,"Gasto actualizado",Toast.LENGTH_SHORT).show();
        finish();
    }

    // =========================
    // Cargar categorías en spinner
    // =========================
    private void cargarCategorias(){

        listaCategorias = new ArrayList<>();
        List<String> nombres = new ArrayList<>();

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

        spCategorias.setAdapter(adapter);
    }
}
