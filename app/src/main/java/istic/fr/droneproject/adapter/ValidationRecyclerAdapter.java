package istic.fr.droneproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Validation;

/**
 * Created by nirina on 24/03/17.
 */

public class ValidationRecyclerAdapter extends RecyclerView.Adapter<ValidationRecyclerAdapter.ValidationViewHolder> {

    private List<Validation> validations;
    private int layout;
    private RefusClickListener refusListener;
    private ValidClickListener validListener;

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

        holder.nom.setText(validation.vehicule.nom);
        holder.libelle.setText(validation.intervention.libelle);
        holder.heuredemande.setText(validation.vehicule.heureDemande);
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
        TextView nom;
        TextView heuredemande;
        TextView libelle;
        Button validation;
        Button refus;

        public ValidationViewHolder(View itemView) {
            super(itemView);
            nom = (TextView) itemView.findViewById(R.id.ui_validation_item_nom);
            heuredemande = (TextView) itemView.findViewById(R.id.ui_validation_item_heuredemande);
            libelle = (TextView) itemView.findViewById(R.id.ui_validation_item_libelle);
            validation = (Button) itemView.findViewById(R.id.ui_validation_item_validation);
            refus = (Button) itemView.findViewById(R.id.ui_validation_item_refus);
        }
    }

    public interface RefusClickListener{
        void clickRefus(Validation validation);
    }

    public interface ValidClickListener{
        void clickValidation(Validation validation);
    }
}
