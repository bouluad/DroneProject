package istic.fr.droneproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Vehicule;

public class VehiculeRecyclerAdapter extends RecyclerView.Adapter<VehiculeRecyclerAdapter.VehiculeViewHolder> {


    private final List<Vehicule> vehicules;
    private final int layout;

    public VehiculeRecyclerAdapter(List<Vehicule> vehicules, int layout) {
        this.vehicules = vehicules;
        this.layout = layout;
    }

    @Override
    public VehiculeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new VehiculeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VehiculeViewHolder holder, int position) {
        Vehicule vehicule = vehicules.get(position);

        holder.nom.setText(vehicule.nom);
        holder.type.setText(vehicule.type.toString());
    }

    @Override
    public int getItemCount() {
        return vehicules.size();
    }

    public class VehiculeViewHolder extends RecyclerView.ViewHolder {
        TextView nom;
        TextView type;

        public VehiculeViewHolder(View itemView) {
            super(itemView);
            nom = (TextView) itemView.findViewById(R.id.ui_vehicule_item_nom);
            type = (TextView) itemView.findViewById(R.id.ui_vehicule_item_type);
        }
    }
}
