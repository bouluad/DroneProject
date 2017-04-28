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
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.TransformImageToStringEtVs;

public class TableauMoyenRecyclerAdapter extends RecyclerView.Adapter<TableauMoyenRecyclerAdapter.VehiculeViewHolder> {


    private final List<Vehicule> vehicules;
    private final int layout;
    private EventsVehiculeClickListener eventsVehiculeClickListener;

    public TableauMoyenRecyclerAdapter(List<Vehicule> vehicules, int layout, EventsVehiculeClickListener listener) {
        this.vehicules = vehicules;
        this.layout = layout;
        this.eventsVehiculeClickListener = listener;
    }

    @Override
    public VehiculeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new VehiculeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final VehiculeViewHolder holder, final int position) {
        Vehicule vehicule = vehicules.get(position);

        holder.nom.setTextColor(TransformImageToStringEtVs.FindColorByVehicule(vehicule.categorie));
        holder.nom.setText(vehicule.nom);

        holder.btnConfirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsVehiculeClickListener.clickConfirmer(vehicules.get(holder.getAdapterPosition()));
            }
        });

        holder.btnLiberer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsVehiculeClickListener.clickLiberer(vehicules.get(holder.getAdapterPosition()));
            }
        });

        if (vehicule.peutEtreArrive()) {
            holder.btnConfirmer.setEnabled(true);
            holder.btnConfirmer.setVisibility(View.VISIBLE);
        } else {
            holder.btnConfirmer.setEnabled(false);
            holder.btnConfirmer.setVisibility(View.INVISIBLE);
        }

        if (vehicule.peutEtreLibere()) {
            holder.btnLiberer.setEnabled(true);
            holder.btnLiberer.setVisibility(View.VISIBLE);
        } else {
            holder.btnLiberer.setEnabled(false);
            holder.btnLiberer.setVisibility(View.INVISIBLE);
        }

        afficherHeure(holder.heure1, vehicule.heureDemande);
        afficherHeure(holder.heure2, vehicule.heureEngagement);
        afficherHeure(holder.heure3, vehicule.heureArrivee);
        afficherHeure(holder.heure4, vehicule.heureLiberation);

        if (vehicule.etat == EtatVehicule.ANNULE) {

            holder.nom.setText(vehicule.nom + "\n" + "(annulé)");

            barrerTexte(true, holder.nom);
            barrerTexte(true, holder.heure1);
            barrerTexte(true, holder.heure2);
            barrerTexte(true, holder.heure3);
            barrerTexte(true, holder.heure4);
        } else {

            barrerTexte(false, holder.nom);
            barrerTexte(false, holder.heure1);
            barrerTexte(false, holder.heure2);
            barrerTexte(false, holder.heure3);
            barrerTexte(false, holder.heure4);
        }
    }

    private void barrerTexte(boolean barrer, TextView view) {
        if (barrer) {
            view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            view.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
        }
    }

    private void afficherHeure(TextView view, String heure) {
        if (heure == null || heure.isEmpty()) {
            view.setBackgroundColor(Color.GRAY);
            view.setText("----");
        } else {
            view.setText(heure);
            view.setBackgroundColor(Color.TRANSPARENT);
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