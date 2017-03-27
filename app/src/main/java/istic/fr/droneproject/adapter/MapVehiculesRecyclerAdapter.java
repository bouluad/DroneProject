package istic.fr.droneproject.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Categorie;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Vehicule;

public class MapVehiculesRecyclerAdapter extends RecyclerView.Adapter<MapVehiculesRecyclerAdapter.VehiculeViewHolder> {


    private final List<Vehicule> vehicules;
    private final int layout;
    private final Intervention intervention;

    public MapVehiculesRecyclerAdapter(List<Vehicule> vehicules, int layout, Intervention intervention) {
        this.vehicules = vehicules;
        this.layout = layout;
        this.intervention = intervention;
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
       switch(vehicule.categorie){
           case COMMANDEMENT:
              holder.image.setImageBitmap();
           case SAUVETAGE:

           case INCENDIE:

           case RISQUE_PARTICULIER:


           default:




       }

       /* holder.nom.setText(vehicule.nom);
       Log.e("TMR","vehicule"+vehicule.heureEngagement);
        holder.heure1.setText(vehicule.heureDemande);
        if(vehicule.heureDemande == null || vehicule.heureDemande.isEmpty()){
            holder.heure1.setBackgroundColor(Color.GRAY);
            holder.heure1.setText("----");
        }
        holder.heure2.setText(vehicule.heureEngagement);
        if(vehicule.heureEngagement == null || vehicule.heureEngagement.isEmpty()){
            holder.heure2.setBackgroundColor(Color.GRAY);
            holder.heure2.setText("----");
        }
        holder.heure3.setText(vehicule.heureArrivee);
        if(vehicule.heureArrivee == null || vehicule.heureArrivee.isEmpty()){
            holder.heure3.setBackgroundColor(Color.GRAY);
            holder.heure3.setText("----");
        }else{
            holder.btnConfirmer.setVisibility(View.INVISIBLE);
        }
        holder.heure4.setText(vehicule.heureLiberation);
        if(vehicule.heureLiberation == null || vehicule.heureLiberation.isEmpty()){
            holder.heure4.setBackgroundColor(Color.GRAY);
            holder.heure4.setText("----");
            holder.heure4.setVisibility(View.GONE);
            holder.btnLiberer.setVisibility(View.VISIBLE);
        }*/

    }

    @Override
    public int getItemCount() {
        return vehicules.size();
    }

    public class VehiculeViewHolder extends RecyclerView.ViewHolder {

        TextView nom;
        TextView type;
        ImageView image;


        public VehiculeViewHolder(final View itemView) {
            super(itemView);
            nom = (TextView) itemView.findViewById(R.id.m_vehicules_info_nom);
            type = (TextView) itemView.findViewById(R.id.m_vehicules_info_type);
            image = (ImageView) itemView.findViewById(R.id.m_vehicules_image);


        }
    }
}