package com.example.amlr.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amlr.R;
import com.example.amlr.entidades.Actividades;

import java.util.ArrayList;

public class ListaActividadesAdapter extends RecyclerView.Adapter<ListaActividadesAdapter.ActividadViewHolder> {
    ArrayList<Actividades> listaActividades;

    public ListaActividadesAdapter(ArrayList<Actividades> listaActividades){
        this.listaActividades = listaActividades;
    }
    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_actividad, null, false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        holder.viewActividad.setText(listaActividades.get(position).getAccion());
        holder.viewFechaHora.setText(listaActividades.get(position).getFecha_y_hora());
    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }

    public class ActividadViewHolder extends RecyclerView.ViewHolder {
        TextView viewFechaHora, viewActividad;
        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);

            viewFechaHora = itemView.findViewById(R.id.viewFechaHora);
            viewActividad = itemView.findViewById(R.id.viewActividad);
        }
    }
}
