package com.example.migestordegastos.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.migestordegastos.R;
import com.example.migestordegastos.database.DatabaseHelper;
import com.example.migestordegastos.models.Categoria;

import java.util.List;
public class CategoriaAdapter extends BaseAdapter {

    Context context;
    List<Categoria> listaCategorias;
    DatabaseHelper databaseHelper;

    public CategoriaAdapter(Context context, List<Categoria> listaCategorias) {
        this.context = context;
        this.listaCategorias = listaCategorias;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return listaCategorias.size();
    }

    @Override
    public Object getItem(int position) {
        return listaCategorias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaCategorias.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_categoria,parent,false);
        }

        ImageView icono = convertView.findViewById(R.id.imgIconoCategoria);
        TextView nombre = convertView.findViewById(R.id.tvNombreCategoria);
        TextView btnEliminar = convertView.findViewById(R.id.btnEliminarCategoria);

        Categoria categoria = listaCategorias.get(position);

        nombre.setText(categoria.getNombre());

        int iconoId = context.getResources().getIdentifier(
                categoria.getIcono(),
                "drawable",
                context.getPackageName());

        if(iconoId != 0){
            icono.setImageResource(iconoId);
        }

        // =========================
        // BOTÓN ELIMINAR
        // =========================
        btnEliminar.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Eliminar categoría")
                    .setMessage("¿Seguro que quieres eliminarla?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {

                        SQLiteDatabase db = databaseHelper.getWritableDatabase();

                        db.delete("categorias", "id=?",
                                new String[]{String.valueOf(categoria.getId())});

                        listaCategorias.remove(position);
                        notifyDataSetChanged();

                        Toast.makeText(context, "Categoría eliminada", Toast.LENGTH_SHORT).show();

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        return convertView;
    }
}
