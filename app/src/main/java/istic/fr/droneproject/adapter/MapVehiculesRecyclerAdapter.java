package istic.fr.droneproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Vehicule;

public class MapVehiculesRecyclerAdapter extends RecyclerView.Adapter<MapVehiculesRecyclerAdapter.VehiculeViewHolder> {

    private List<Vehicule> vehicules;
    private int layout;
    private VehiculeClickListener listener;
    private Context context;

    public MapVehiculesRecyclerAdapter(List<Vehicule> vehicules, int layout, Context context, VehiculeClickListener listener) {

        this.vehicules = vehicules;
        this.layout = layout;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public VehiculeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new VehiculeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VehiculeViewHolder holder, int position) {
        final Vehicule vehicule = vehicules.get(position);

        holder.nom.setText(vehicule.nom);
        holder.nom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickVehicule(vehicule);
            }
        });
        holder.type.setText(vehicule.type.toString());

        int drawable;
        switch (vehicule.categorie) {
            case COMMANDEMENT:
                drawable = R.drawable.vh_com;
                break;

            case SAUVETAGE:
                drawable = R.drawable.vh_hu;
                break;

            case INCENDIE:
                drawable = R.drawable.vh_in;
                break;

            case RISQUE_PARTICULIER:
                drawable = R.drawable.vh_rp;
                break;

            case EAU:
                drawable = R.drawable.vh_eau;
                break;

            default:
                drawable = R.drawable.vh_eau;
        }
        Picasso.with(context).load(drawable).resize(200,150).into(holder.image);

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

    public interface VehiculeClickListener {
        void clickVehicule(Vehicule vehicule);
    }
}