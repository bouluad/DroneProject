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

public class AlbumPhotoAdapter extends RecyclerView.Adapter<AlbumPhotoAdapter.PhotoViewHolder> {

    private List<Photo> photos;
    private int layout;
    private Context context;

    public AlbumPhotoAdapter(List<Photo> photos, int layout, Context context) {
        this.photos = photos;
        this.layout = layout;
        this.context = context;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);
        Picasso.with(context).setIndicatorsEnabled(true
        );
        Picasso.with(context).load(photo.path).placeholder(R.drawable.loading).error(R.drawable.image_not_found).into(holder.image);
        holder.nom.setText(photo.nom);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView nom;

        PhotoViewHolder(View itemView) {
            super(itemView);

            this.image = (ImageView) itemView.findViewById(R.id.album_photo_imageview);
            this.nom = (TextView) itemView.findViewById(R.id.album_photo_nomphoto);
        }
    }
}
