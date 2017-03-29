package istic.fr.droneproject.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;
import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Vehicule;

public class MapPointsRecyclerAdapter extends RecyclerView.Adapter<MapPointsRecyclerAdapter.PointViewHolder> {


    private final List<Pair<String,String>> listeImages;
    private final int layout;
    private PointClickListener listener;

    public MapPointsRecyclerAdapter(List<Pair<String,String>> images, int layout,PointClickListener listener) {
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
        this.listener=listener;
    }

    @Override
    public PointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new PointViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PointViewHolder holder, int position) {
      final Pair<String, String> imageCourante = listeImages.get(position);
        byte[] encodeByte = Base64.decode(imageCourante.second, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        holder.image.setImageBitmap(bitmap);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickPoint(imageCourante);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listeImages.size();
    }

    public class PointViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public PointViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.m_points_image);


        }
    }
    public interface PointClickListener {
        void clickPoint( Pair<String, String> image);
    }
}