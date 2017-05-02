package istic.fr.droneproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String pos1 ="";
        String pos2 ="";
        String idInter ="";

        if(bundle != null){
             pos1 =bundle.getString("Value1");
             pos2 =bundle.getString("Value2");
             idInter =bundle.getString("Value3");
            System.out.println(idInter);

        }

        RecyclerView recyclerViewPhotos = (RecyclerView) findViewById(R.id.album_liste);

                       recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                final List<DronePhotos> photos = new ArrayList<>();
                final AlbumPhotoAdapter adapter = new AlbumPhotoAdapter(photos, R.layout.album_photo_item, this.getApplicationContext());
                recyclerViewPhotos.setAdapter(adapter);

        DronePhotosServiceImpl service = new DronePhotosServiceImpl();

        service.getDronePhotosbyPositionPTS(pos1,pos2, idInter,new Callback<List<DronePhotos>>() {
            @Override
            public void onResponse(Call<List<DronePhotos>> call, Response<List<DronePhotos>> response) {
                photos.clear();
                if (response.body()!=null) {
                    System.out.println(response.body());
                    photos.addAll(response.body());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<DronePhotos>> call, Throwable t) {

                                           }
        });


    }

}