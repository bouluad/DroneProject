package istic.fr.droneproject.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Intervention;

public class InterventionRecyclerAdapter extends RecyclerView.Adapter<InterventionRecyclerAdapter.InterventionViewHolder> {

    private List<Intervention> interventions;
    private int layout;
    private InterventionClickListener listener;

    public InterventionRecyclerAdapter(List<Intervention> interventions, int layout, InterventionClickListener listener) {
        this.interventions = interventions;
        this.layout = layout;
        this.listener = listener;
    }

    @Override
    public InterventionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new InterventionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(InterventionViewHolder holder, int position) {
        final Intervention intervention = interventions.get(position);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickIntervention(intervention);
            }
        });

        if(intervention.cloturer){
            holder.libelle.setPaintFlags(holder.libelle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.libelle.setText(intervention.libelle+" (clôturée)");
        }else{
            holder.libelle.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            holder.libelle.setText(intervention.libelle);
        }
    }

    @Override
    public int getItemCount() {
        return interventions.size();
    }

    public class InterventionViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView libelle;

        public InterventionViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.ui_intervention_item);
            libelle = (TextView) itemView.findViewById(R.id.ui_intervention_item_libelle);
        }
    }

    public interface InterventionClickListener {
        void clickIntervention(Intervention intervention);
    }
}
