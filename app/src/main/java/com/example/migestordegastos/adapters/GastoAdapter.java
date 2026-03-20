package com.example.migestordegastos.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.migestordegastos.R;
import com.example.migestordegastos.database.DatabaseHelper;
import com.example.migestordegastos.models.Gasto;

import java.util.List;
public class GastoAdapter extends BaseAdapter {

    DatabaseHelper databaseHelper;
    private Context context;
    private List<Gasto> listaGastos;

    public GastoAdapter(Context context, List<Gasto> listaGastos) {
        this.context = context;
        this.listaGastos = listaGastos;
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return listaGastos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaGastos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaGastos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gasto, parent, false);
        }

        ImageView imgIcono = convertView.findViewById(R.id.imgIconoCategoria);
        TextView tvConcepto = convertView.findViewById(R.id.tvConcepto);
        TextView tvFecha = convertView.findViewById(R.id.tvFecha);
        TextView tvCantidad = convertView.findViewById(R.id.tvCantidad);

        Gasto gasto = listaGastos.get(position);

        tvConcepto.setText(gasto.getConcepto());
        tvFecha.setText(gasto.getFecha());
        tvCantidad.setText(gasto.getCantidad() + " €");

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT icono FROM categorias WHERE id=?",
                new String[]{String.valueOf(gasto.getIdCategoria())});

        if(cursor.moveToFirst()){

            String nombreIcono = cursor.getString(0);

            int iconoId = context.getResources().getIdentifier(
                    nombreIcono,
                    "drawable",
                    context.getPackageName());

            imgIcono.setImageResource(iconoId);
        }

        cursor.close();

        return convertView;
    }
}
