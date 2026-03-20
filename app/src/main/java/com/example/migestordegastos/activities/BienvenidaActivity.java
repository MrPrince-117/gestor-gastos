package com.example.migestordegastos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.migestordegastos.R;

public class BienvenidaActivity extends AppCompatActivity {

    Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        btnEntrar = findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // para no volver atrás
        });
    }
}
