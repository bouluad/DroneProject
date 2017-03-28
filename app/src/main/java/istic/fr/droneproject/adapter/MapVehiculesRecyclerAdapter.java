package istic.fr.droneproject.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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

import static istic.fr.droneproject.model.Categorie.COMMANDEMENT;
import static istic.fr.droneproject.model.Categorie.INCENDIE;
import static istic.fr.droneproject.model.Categorie.RISQUE_PARTICULIER;
import static istic.fr.droneproject.model.Categorie.SAUVETAGE;

public class MapVehiculesRecyclerAdapter extends RecyclerView.Adapter<MapVehiculesRecyclerAdapter.VehiculeViewHolder> {


    private final List<Vehicule> vehicules;
    private final int layout;
    private VehiculeClickListener listener;

    public MapVehiculesRecyclerAdapter(List<Vehicule> vehicules, int layout) {
        this.vehicules = vehicules;
        this.layout = layout;

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
        Log.e("==========>",vehicule.categorie.toString());
       switch(vehicule.categorie){
           case COMMANDEMENT:
               byte [] encodeByte= Base64.decode(String.valueOf(R.drawable.vh_hu),Base64.DEFAULT);
               Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
              // Bitmap bMap = BitmapFactory.decodeFile(String.valueOf(R.drawable.vh_hu));
              holder.image.setImageBitmap(bitmap);

           case SAUVETAGE:
               encodeByte= Base64.decode(String.valueOf(R.drawable.vh_hu),Base64.DEFAULT);
               bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
               holder.image.setImageBitmap(bitmap);
           case INCENDIE:
               encodeByte= Base64.decode(String.valueOf(R.drawable.vh_hu),Base64.DEFAULT);
                bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
               holder.image.setImageBitmap(bitmap);

           case RISQUE_PARTICULIER:
               encodeByte= Base64.decode(String.valueOf(R.drawable.vh_hu),Base64.DEFAULT);
               bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
               holder.image.setImageBitmap(bitmap);

           default:
               encodeByte= Base64.decode(String.valueOf(R.drawable.vh_hu),Base64.DEFAULT);
               bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
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