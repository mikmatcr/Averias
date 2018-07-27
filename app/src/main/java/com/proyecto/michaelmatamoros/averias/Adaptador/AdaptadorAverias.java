package com.proyecto.michaelmatamoros.averias.Adaptador;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proyecto.michaelmatamoros.averias.R;
import com.proyecto.michaelmatamoros.averias.modelo.Averia;

import java.util.ArrayList;

public class AdaptadorAverias extends RecyclerView.Adapter<AdaptadorAverias.ViewHolder> implements View.OnClickListener{
    private ArrayList<Averia> datos;
    private View.OnClickListener listener;



    public AdaptadorAverias(ArrayList<Averia> datos) {
        this.datos = datos;
    }

    @Override
    public AdaptadorAverias.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.averias, parent, false);
        view.setOnClickListener(this);

        AdaptadorAverias.ViewHolder tvh = new AdaptadorAverias.ViewHolder(view);
        return tvh;
    }

    @Override
    public void onBindViewHolder(AdaptadorAverias.ViewHolder holder, int position) {
        Averia item = datos.get(position);
        holder.name.setText(item.getNombre());
        holder.tipo.setText(item.getTipo());
        holder.descripcion.setText(item.getDescripcion());

    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,tipo, descripcion;


        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name_p);
            tipo =(TextView)itemView.findViewById(R.id.tipo);
            descripcion = (TextView)itemView.findViewById(R.id.descripcion);

        }

    }
}
