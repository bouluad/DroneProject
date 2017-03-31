package istic.fr.droneproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.TypePoint;

public class MapPointsRecyclerAdapter extends RecyclerView.Adapter<MapPointsRecyclerAdapter.PointViewHolder> {

    private final List<TypePoint> listeImages;
    private final int layout;
    private PointClickListener listener;
    private Context context;

    /**
     *
     * @param images Liste de paires (nom, drawable)
     * @param layout
     * @param context
     * @param listener
     */
    public MapPointsRecyclerAdapter(List<TypePoint> images, int layout, Context context, PointClickListener listener) {
        this.listeImages = images;
        this.layout = layout;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public PointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new PointViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PointViewHolder holder, int position) {
        final TypePoint imageCourante = listeImages.get(position);

        Picasso.with(context).load(imageCourante.getDrawable()).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickPoint(imageCourante.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listeImages.size();
    }

    public interface PointClickListener {
        void clickPoint(String nomImage);
    }

    class PointViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        PointViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.m_points_image);
        }
    }
}