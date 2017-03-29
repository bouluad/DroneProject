package istic.fr.droneproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Vehicule;
import static istic.fr.droneproject.model.Categorie.COMMANDEMENT;
import static istic.fr.droneproject.model.Categorie.INCENDIE;
import static istic.fr.droneproject.model.Categorie.RISQUE_PARTICULIER;
import static istic.fr.droneproject.model.Categorie.SAUVETAGE;

public class MapVehiculesRecyclerAdapter extends RecyclerView.Adapter<MapVehiculesRecyclerAdapter.VehiculeViewHolder> {


    private  List<Vehicule> vehicules;
    private  int layout;
    private VehiculeClickListener listener;
    private Context context;


    public MapVehiculesRecyclerAdapter(List<Vehicule> vehicules, int layout,Context context,VehiculeClickListener listener) {

        this.vehicules = vehicules;
        this.layout = layout;
        this.context=context;
        this.listener=listener;

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
        Log.e("==========>",vehicule.categorie.toString());
       switch(vehicule.categorie){
           case COMMANDEMENT:
               Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.vh_com);
               holder.image.setImageBitmap(bitmap);

           case SAUVETAGE:

               bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.vh_hu);
               holder.image.setImageBitmap(bitmap);
           case INCENDIE:

               bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.vh_in);
               holder.image.setImageBitmap(bitmap);

           case RISQUE_PARTICULIER:

               bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.vh_rp);
               holder.image.setImageBitmap(bitmap);

           default:

               bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.vh_eau);
               holder.image.setImageBitmap(bitmap);



       }

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