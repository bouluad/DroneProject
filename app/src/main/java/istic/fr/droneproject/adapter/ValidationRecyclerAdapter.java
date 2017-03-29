package istic.fr.droneproject.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.EtatVehicule;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.TypeVehicule;
import istic.fr.droneproject.model.Validation;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.InterventionService;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;

/**
 * Created by nirina on 24/03/17.
 */

public class ValidationRecyclerAdapter extends RecyclerView.Adapter<ValidationRecyclerAdapter.ValidationViewHolder> {

    private List<Validation> validations;
    private int layout;
    private RefusClickListener refusListener;
    private ValidClickListener validListener;

    //public ValidationRecyclerAdapter(List<Validation> validations, int layout){
    public ValidationRecyclerAdapter(List<Validation> validations, int layout, RefusClickListener refusListener, ValidClickListener validListener){
        this.validations = validations;
        this.layout = layout;
        this.refusListener = refusListener;
        this.validListener = validListener;
    }

    @Override
    public ValidationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ValidationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ValidationViewHolder holder, int position) {
        final Validation validation = validations.get(position);
        String categorie = validation.vehicule.categorie.toString().toLowerCase();
        switch (categorie){
            //SAUVETAGE - VERT
            case "sauvetage":
                holder.tablerow.setBackgroundColor(Color.parseColor("#00f386"));
                break;
            //INCENDIE - ROUGE
            case "incendie":
                holder.tablerow.setBackgroundColor(Color.parseColor("#ee6a6a"));
                break;
            //EAU - BLEU
            case "eau":
                holder.tablerow.setBackgroundColor(Color.parseColor("#a7cae8"));
                break;
            //RISQUE_PARTICULIER - JAUNE
            case "risque_particulier":
                holder.tablerow.setBackgroundColor(Color.parseColor("#fff402"));
                break;
            //COMMANDEMENT - VIOLET
            case "commandement":
                holder.tablerow.setBackgroundColor(Color.parseColor("#927ae8"));
                break;
            //PAR DEFAUT  - VERT CLAIR
            default:
                holder.tablerow.setBackgroundColor(Color.parseColor("#d0fff0"));
                break;
        }

        holder.nom.setText(validation.vehicule.nom);
        holder.libelle.setText(validation.intervention.libelle);
        holder.heuredemande.setText(validation.vehicule.heureDemande);
        holder.type.setText(validation.vehicule.type.toString());
        holder.validation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                validListener.clickValidation(validation);
            }
        });
        holder.refus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                refusListener.clickRefus(validation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return validations.size();
    }

    public class ValidationViewHolder extends RecyclerView.ViewHolder{

        TableRow tablerow;
        TextView nom;
        TextView heuredemande;
        TextView libelle;
        TextView type;
        Button validation;
        Button refus;


        public ValidationViewHolder(View itemView) {
            super(itemView);
            tablerow = (TableRow) itemView.findViewById(R.id.tableRowValidMoyen);
            nom = (TextView) itemView.findViewById(R.id.ui_validation_item_nom);
            heuredemande = (TextView) itemView.findViewById(R.id.ui_validation_item_heuredemande);
            libelle = (TextView) itemView.findViewById(R.id.ui_validation_item_libelle);
            validation = (Button) itemView.findViewById(R.id.ui_validation_item_validation);
            refus = (Button) itemView.findViewById(R.id.ui_validation_item_refus);
            type = (TextView) itemView.findViewById(R.id.ui_validation_item_type);

        }

    }

    public interface RefusClickListener{
        void clickRefus(Validation validation);
    }

    public interface ValidClickListener{
        void clickValidation(Validation validation);
    }
}
