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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Vehicule;

public class MapPointsRecyclerAdapter extends RecyclerView.Adapter<MapPointsRecyclerAdapter.VehiculeViewHolder> {


    private final List<String> listeImages;
    private final int layout;

    public MapPointsRecyclerAdapter(List<String> images, int layout) {
        this.listeImages = images;
//        this.listeImages.add("eau.png");
//        this.listeImages.add("eau_np.png");
//        this.listeImages.add("ps_eau.png");
//        this.listeImages.add("ps_hu.png");
//        this.listeImages.add("ps_in.png");
//        this.listeImages.add("ps_rp.png");
//        this.listeImages.add("sd_eau.png");
//        this.listeImages.add("sd_hu.png");
//        this.listeImages.add("sd_in.png");
//        this.listeImages.add("sd_rp.png");
//        this.listeImages.add("ve_eau.png");
//        this.listeImages.add("ve_hu.png");
//        this.listeImages.add("ve_in.png");
//        this.listeImages.add("ve_rp.png");
        this.layout = layout;
    }

    @Override
    public VehiculeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new VehiculeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VehiculeViewHolder holder, int position) {
        String imageCourante = listeImages.get(position);
        byte[] encodeByte = Base64.decode(imageCourante, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        holder.image.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return listeImages.size();
    }

    public class VehiculeViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public VehiculeViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.m_points_image);


        }
    }
}