package istic.fr.droneproject.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.EtatVehicule;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Vehicule;

public class TableauMoyenRecyclerAdapter extends RecyclerView.Adapter<TableauMoyenRecyclerAdapter.VehiculeViewHolder> {


    private final List<Vehicule> vehicules;
    private final int layout;
    private final Intervention intervention;
    EventsVehiculeClickListener eventsVehiculeClickListener;

    public TableauMoyenRecyclerAdapter(List<Vehicule> vehicules, int layout, Intervention intervention, EventsVehiculeClickListener listener) {
        this.vehicules = vehicules;
        this.layout = layout;
        this.intervention = intervention;
        this.eventsVehiculeClickListener = listener;
    }

    @Override
    public VehiculeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new VehiculeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VehiculeViewHolder holder, final int position) {
        Vehicule vehicule = vehicules.get(position);
        switch (vehicule.categorie.toString()) {
            //SAUVETAGE - VERT
            case "SAUVETAGE":
                holder.nom.setTextColor(Color.parseColor("#009D4F"));
                break;
            //INCENDIE - ROUGE
            case "INCENDIE":
                holder.nom.setTextColor(Color.parseColor("#e12b2b"));
                break;
            //EAU - BLEU
            case "EAU":
                holder.nom.setTextColor(Color.parseColor("#1343f0"));
                break;
            //RISQUE_PARTICULIER - JAUNE
            case "RISQUE_PARTICULIER":
                holder.nom.setTextColor(Color.parseColor("#cee514"));
                break;
            //COMMANDEMENT - VIOLET
            case "COMMANDEMENT":
                holder.nom.setTextColor(Color.parseColor("#ae07b8"));
                break;
            //PAR DEFAUT  - NOIR
            default:
                holder.nom.setTextColor(Color.parseColor("#060606"));
                break;
        }

        holder.nom.setText(vehicule.nom);

        if (vehicule.heureDemande == null || vehicule.heureDemande.isEmpty()) {
            holder.heure1.setBackgroundColor(Color.GRAY);
            holder.heure1.setText("----");
        }else {
            holder.heure1.setText(vehicule.heureDemande);
            holder.heure4.setBackgroundColor(Color.TRANSPARENT);
        }
        if (!EtatVehicule.ANNULE.equals(vehicule.etat)) {
            if (vehicule.heureEngagement == null || vehicule.heureEngagement.isEmpty()) {
                holder.heure2.setBackgroundColor(Color.GRAY);
                holder.heure2.setText("----");
            }else {
                holder.heure2.setText(vehicule.heureDemande);
                holder.heure2.setBackgroundColor(Color.TRANSPARENT);
            }
            if (vehicule.heureArrivee == null || vehicule.heureArrivee.isEmpty()) {
                holder.heure3.setBackgroundColor(Color.GRAY);
                holder.heure3.setText("----");
                holder.btnConfirmer.setVisibility(View.VISIBLE);
                holder.btnConfirmer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eventsVehiculeClickListener.clickConfirmer(vehicules.get(position));
                    }
                });

            } else {
                holder.heure3.setText(vehicule.heureArrivee);
                holder.heure3.setBackgroundColor(Color.TRANSPARENT);
                holder.btnConfirmer.setVisibility(View.INVISIBLE);
            }
            if (vehicule.heureLiberation == null || vehicule.heureLiberation.isEmpty()) {
                holder.heure4.setBackgroundColor(Color.GRAY);
                holder.heure4.setText("----");
                holder.btnLiberer.setVisibility(View.VISIBLE);
                holder.btnLiberer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eventsVehiculeClickListener.clickLiberer(vehicules.get(position));
                    }
                });
            } else {
                holder.heure4.setText(vehicule.heureLiberation);
                holder.heure4.setBackgroundColor(Color.TRANSPARENT);
                holder.btnLiberer.setVisibility(View.INVISIBLE);
                holder.btnConfirmer.setVisibility(View.INVISIBLE);
            }
            //bloquer le bouton confirmer si il n'est pas engager avec une position valide
//            if(EtatVehicule.ENGAGE.equals(vehicule.etat) && vehicule.position != null && vehicule.position[0] != null && vehicule.position[1] != null){
//                holder.btnConfirmer.setVisibility(View.VISIBLE);
//            }
//            else{
//                holder.btnConfirmer.setVisibility(View.INVISIBLE);
//            }

        } else {
            holder.heure2.setPaintFlags(holder.heure2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.heure2.setText("annulé");
            holder.heure3.setPaintFlags(holder.heure3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.heure4.setPaintFlags(holder.heure4.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.heure1.setPaintFlags(holder.heure1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.nom.setPaintFlags(holder.nom.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.btnLiberer.setVisibility(View.INVISIBLE);
            holder.btnConfirmer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return vehicules.size();
    }

    /**
     * interface pour les evenement du tableau des moyens
     */
    public interface EventsVehiculeClickListener {
        /**
         * Action à effectuer quand le button confirmer-
         * est appuyé dans la liste des moyens
         *
         * @param vehicule Le vehicule à confirmer
         *                 c
         */
        void clickConfirmer(Vehicule vehicule);

        /**
         * Action à effectuer quand le button Liberer-
         * est appuyé dans la liste des moyens
         *
         * @param vehicule Le vehicule à liberer
         *                 c
         */
        void clickLiberer(Vehicule vehicule);
    }

    public class VehiculeViewHolder extends RecyclerView.ViewHolder {
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
            btnLiberer = (Button) itemView.findViewById((R.id.utm_vi_btn_liberation));
        }
    }
}