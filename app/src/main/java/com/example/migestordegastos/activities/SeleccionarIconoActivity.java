package com.example.migestordegastos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.migestordegastos.R;

public class SeleccionarIconoActivity extends AppCompatActivity {

    GridLayout gridIconos;

    int[] iconos = {
            R.drawable.avion,
            R.drawable.cumple,
            R.drawable.viaje,
            R.drawable.entrenamiento,
            R.drawable.delivery,
            R.drawable.comida,
            R.drawable.escuela,
            R.drawable.futbol
    };

    String[] nombres = {
            "avion",
            "cumple",
            "viaje",
            "entrenamiento",
            "delivery",
            "comida",
            "escuela",
            "futbol"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_icono);

        gridIconos = findViewById(R.id.gridIconos);

        // botón atrás
        findViewById(R.id.btBack).setOnClickListener(v -> finish());

        for(int i = 0; i < iconos.length; i++){

            ImageView img = new ImageView(this);

            img.setLayoutParams(new GridLayout.LayoutParams());
            img.getLayoutParams().width = 200;
            img.getLayoutParams().height = 200;

            img.setPadding(20,20,20,20);
            img.setImageResource(iconos[i]);

            int index = i;

            img.setOnClickListener(v -> {

                Intent intent = new Intent();
                intent.putExtra("icono", nombres[index]);
                setResult(RESULT_OK, intent);

                finish();
            });

            gridIconos.addView(img);
        }
    }
}