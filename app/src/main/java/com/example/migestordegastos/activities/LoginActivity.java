package com.example.migestordegastos.activities;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.migestordegastos.MainActivity;
import com.example.migestordegastos.R;
import com.example.migestordegastos.database.DatabaseHelper;
public class LoginActivity extends AppCompatActivity {

    EditText etUsuario, etPassword;
    Button btnLogin;
    TextView tvIrRegistro;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvIrRegistro = findViewById(R.id.tvIrRegistro);

        databaseHelper = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> loginUsuario());

        tvIrRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }

    private void loginUsuario(){

        String usuario = etUsuario.getText().toString();
        String password = etPassword.getText().toString();

        if(usuario.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Introduce usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query = "SELECT * FROM usuarios WHERE nombre_usuario=? AND contrasena=?";
        Cursor cursor = db.rawQuery(query, new String[]{usuario, password});

        if(cursor.moveToFirst()){

            Toast.makeText(this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

            finish();

        }else{

            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();

        }

        cursor.close();
    }
}
