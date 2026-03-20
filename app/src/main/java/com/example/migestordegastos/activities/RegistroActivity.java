package com.example.migestordegastos.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.migestordegastos.R;
import com.example.migestordegastos.database.DatabaseHelper;

public class RegistroActivity extends AppCompatActivity {

    EditText etUsuario, etPassword, etConfirmarPassword;
    Button btnRegistrar;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etUsuario = findViewById(R.id.etUsuarioRegistro);
        etPassword = findViewById(R.id.etPasswordRegistro);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        databaseHelper = new DatabaseHelper(this);

        btnRegistrar.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {

        String usuario = etUsuario.getText().toString();
        String password = etPassword.getText().toString();
        String confirmar = etConfirmarPassword.getText().toString();

        if(usuario.isEmpty() || password.isEmpty() || confirmar.isEmpty()){
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(confirmar)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre_usuario", usuario);
        values.put("contrasena", password);

        long resultado = db.insert("usuarios", null, values);

        if(resultado != -1){
            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
        }
    }

}
