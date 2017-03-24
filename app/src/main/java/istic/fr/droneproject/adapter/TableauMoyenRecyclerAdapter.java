package istic.fr.droneproject.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Vehicule;

public class TableauMoyenRecyclerAdapter extends RecyclerView.Adapter<TableauMoyenRecyclerAdapter.VehiculeViewHolder> {


    private final List<Vehicule> vehicules;
    private final int layout;
    private final Intervention intervention;

    public TableauMoyenRecyclerAdapter(List<Vehicule> vehicules, int layout, Intervention intervention) {
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
        }

    }

    @Override
    public int getItemCount() {
        return vehicules.size();
    }

    public class VehiculeViewHolder extends RecyclerView.ViewHolder {
        boolean confirmationLiberer = false;
        TextView nom;
        TextView heure1;
        TextView heure2;
        TextView heure3;
        TextView heure4;
        Button btnLiberer;
        Button btnConfirmer;

        public VehiculeViewHolder(final View itemView) {
            super(itemView);
            nom = (TextView) itemView.findViewById(R.id.utm_vi_Nom);
            heure1 = (TextView) itemView.findViewById(R.id.utm_vi_heure1);
            heure2 = (TextView) itemView.findViewById(R.id.utm_vi_heure2);
            heure3 = (TextView) itemView.findViewById(R.id.utm_vi_heure3);
            heure4 = (TextView) itemView.findViewById(R.id.utm_vi_heure4);
            btnConfirmer = (Button) itemView.findViewById((R.id.utm_vi_btn_Confirmer));

            btnConfirmer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TMR","heure3: "+heure3.getText());
                    if(heure3.getText().toString().equals("----")){
                        heure3.setText(Calendar.getInstance().getTime().getHours()+":"+Calendar.getInstance().getTime().getMinutes());
                        heure3.setBackgroundColor(Color.WHITE);
                    }
                    //TODO:faire un appel a modification de confirmation ( changer l'état du véhucile, changer l'heure3(heure d'arrivée), modifier l'intervention et la re pusher)
                    Toast.makeText(itemView.getContext().getApplicationContext(), "click sur button"+itemView.getId()+"\n avec heure: "+heure1+"\n intervention: "+ intervention, Toast.LENGTH_SHORT).show();
                    if(intervention != null && intervention.libelle != null)
                        Toast.makeText(itemView.getContext().getApplicationContext(), "intervention libelle: "+ intervention.libelle, Toast.LENGTH_SHORT).show();

                }
            });
            btnLiberer = (Button) itemView.findViewById((R.id.utm_vi_btn_liberation));
            btnLiberer.setVisibility(View.GONE);
            btnLiberer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("TMR","click lberation");
                    if(confirmationLiberer == false){
                        confirmationLiberer = true;
                        btnLiberer.setBackgroundColor(Color.RED);
                        btnLiberer.setText("Confirmer Liberer !!!");
                    }
                    else{
                        if(heure4.getText().toString().equals("----")){
                            heure4.setText(Calendar.getInstance().getTime().getHours()+":"+Calendar.getInstance().getTime().getMinutes());
                            heure4.setBackgroundColor(Color.WHITE);
                            heure4.setVisibility(View.VISIBLE);
                            btnLiberer.setVisibility(View.GONE);
                            btnConfirmer.setVisibility(View.INVISIBLE);
                        }
                        //TODO:faire un appel a modification de liberation ( changer l'état du véhucile, changer l'heure3(heure de libération), modifier l'intervention et la re pusher)
                        Toast.makeText(itemView.getContext().getApplicationContext(), "click sur button"+itemView.getId()+"\n avec heure: "+heure1+"\n intervention: "+ intervention, Toast.LENGTH_SHORT).show();
                        if(intervention != null && intervention.libelle != null)
                            Toast.makeText(itemView.getContext().getApplicationContext(), "intervention libelle: "+ intervention.libelle, Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }
}