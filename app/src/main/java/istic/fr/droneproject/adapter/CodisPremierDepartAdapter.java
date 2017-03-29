package istic.fr.droneproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.TransformImageToStringEtVs;

public class CodisPremierDepartAdapter extends RecyclerView.Adapter<CodisPremierDepartAdapter.PremierDepartViewHolder> {

    private final int layout;
    private final List<Vehicule> vehicules;

    public CodisPremierDepartAdapter(int layout, List<Vehicule> vehiculesList) {
        this.layout = layout;
        this.vehicules = vehiculesList;
    }

    @Override
    public PremierDepartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new PremierDepartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PremierDepartViewHolder holder, int position) {
        Vehicule vehicule = vehicules.get(position);

        holder.nom.setText(vehicule.nom);
        holder.type.setText(vehicule.type.toString());
        holder.view.setBackgroundColor(TransformImageToStringEtVs.FindColorByVehicule(vehicule.categorie));
    }

    @Override
    public int getItemCount() {
        return vehicules.size();
    }

    class PremierDepartViewHolder extends RecyclerView.ViewHolder{

        TextView nom;
        TextView type;
        View view;

        PremierDepartViewHolder(View itemView) {
            super(itemView);

            nom = (TextView) itemView.findViewById(R.id.codis_premier_depart_nom);
            type = (TextView) itemView.findViewById(R.id.codis_premier_depart_type);
            view = itemView.findViewById(R.id.codis_premier_depart_view);
        }
    }
}
