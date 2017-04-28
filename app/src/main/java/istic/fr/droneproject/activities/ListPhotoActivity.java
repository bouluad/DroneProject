package istic.fr.droneproject.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.adapter.AlbumPhotoAdapter;
import istic.fr.droneproject.model.DronePhotos;
import istic.fr.droneproject.service.impl.DronePhotosServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bouluad on 27/04/17.
 */

public class ListPhotoActivity  extends Activity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity);

        RecyclerView recyclerViewPhotos = (RecyclerView) findViewById(R.id.album_liste);

                       recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                final List<DronePhotos> photos = new ArrayList<>();
                final AlbumPhotoAdapter adapter = new AlbumPhotoAdapter(photos, R.layout.album_photo_item, this.getApplicationContext());
                recyclerViewPhotos.setAdapter(adapter);

        String pos1 ="31.791702";
        String pos2 ="-7.09262";

        DronePhotosServiceImpl service = new DronePhotosServiceImpl();

        service.getDronePhotosbyPositionPTS(pos1,pos2, new Callback<List<DronePhotos>>() {
            @Override
            public void onResponse(Call<List<DronePhotos>> call, Response<List<DronePhotos>> response) {
                photos.clear();
                photos.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<DronePhotos>> call, Throwable t) {

                                           }
        });


    }

}