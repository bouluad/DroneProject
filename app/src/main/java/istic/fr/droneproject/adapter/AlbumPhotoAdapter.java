package istic.fr.droneproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.DronePhotos;

public class AlbumPhotoAdapter extends RecyclerView.Adapter<AlbumPhotoAdapter.PhotoViewHolder> {

    private List<DronePhotos> photos;
    private int layout;
    private Context context;

    public AlbumPhotoAdapter(List<DronePhotos> photos, int layout, Context context) {
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
        DronePhotos photo = photos.get(position);
        Picasso.with(context)
                .load(photo.path)
                .into(holder.image);

        final String OLD_FORMAT = "YYYYMMddHHmmss";
        final String NEW_FORMAT = "dd MMMM YYYY - HH:mm:ss";

        String oldDateString = photo.date_heure;
        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.FRANCE);

        try {
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
            holder.date.setText(newDateString);

        }catch (ParseException e) {
            Log.e("AlbumPhotoAdapter", e.toString());

            holder.date.setText(oldDateString);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView date;

        PhotoViewHolder(View itemView) {
            super(itemView);

            this.image = (ImageView) itemView.findViewById(R.id.album_photo_imageview);
            this.date = (TextView) itemView.findViewById(R.id.album_photo_datephoto);
        }
    }
}
