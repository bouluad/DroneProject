package istic.fr.droneproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.DronePhotos;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.PicassoMarker;
import istic.fr.droneproject.service.impl.DronePhotosServiceImpl;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by bouluad on 22/03/17.
 */
public class AlbumActivity extends Fragment implements GoogleMap.OnMarkerClickListener {

    private static final String ARG_ID = "idIntervention";
    private View mRootView;
    private MapView mMapView;
    private String idIntervention;
    private Intervention intervention;
    private GoogleMap map;
    private List<DronePhotos> dronePhotos;
    List<PicassoMarker> picassoMarkers = new ArrayList<PicassoMarker>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.album_fragment, container, false);
        initMap();
        mMapView = (MapView) mRootView.findViewById(R.id.photo_map);

        mMapView.onCreate(savedInstanceState);
        return mRootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            idIntervention = getArguments().getString(ARG_ID);
        }
        bindPictures();

    }


    @Override
    public void onResume() {
        super.onResume();

        // map
        if (mMapView != null) mMapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();

        // map
        if (mMapView != null) mMapView.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        // map
        if (mMapView != null) mMapView.onDestroy();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();

        // map
        if (mMapView != null) mMapView.onLowMemory();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save current instance state
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);

        // map
        if (mMapView != null) mMapView.onSaveInstanceState(outState);
    }

    private void bindPictures() {

        map = ((MapView) mRootView.findViewById(R.id.photo_map)).getMap();

        // content
        if (map != null) {

            //options
            map.setOnMarkerClickListener(this);

            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            UiSettings settings = map.getUiSettings();
            settings.setAllGesturesEnabled(true);
            settings.setZoomControlsEnabled(true);


            final Callback<List<DronePhotos>> callback = new Callback<List<DronePhotos>>() {
                @Override
                public void onResponse(Call<List<DronePhotos>> call, Response<List<DronePhotos>> response) {

                    dronePhotos = response.body();

                    Collections.sort(dronePhotos, new Comparator<DronePhotos>() {
                        @Override
                        public int compare(DronePhotos o1, DronePhotos o2) {
                            return o1.date_heure.compareTo(o2.date_heure);
                        }
                    });

                    List<String> positions = new ArrayList<>();

                    for (DronePhotos p : dronePhotos) {

                        String title = p.positionPTS[0] + ";" + p.positionPTS[1];

                        if(!positions.contains(title)) {
                            positions.add(title);

                            MarkerOptions markerOne = new MarkerOptions()
                                    .position(new LatLng(p.positionPTS[0], p.positionPTS[1]))
                                    .title(p.positionPTS[0] + ";" + p.positionPTS[1]);

                            PicassoMarker target = new PicassoMarker(map.addMarker(markerOne));
                            picassoMarkers.add(target);
                            Picasso.with(getContext())
                                    .load(p.path)
                                    .resize(100, 100)
                                    .into(target);
                        }
                    }

                }

                @Override
                public void onFailure(Call<List<DronePhotos>> call, Throwable t) {
                    //DO NOTHING
                    Log.e("AlbumActivity", t.toString());
                }
            };

            InterventionServiceCentral.getInstance().getInterventionById(idIntervention, new Callback<Intervention>() {
                @Override
                public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                    intervention = response.body();

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(intervention.position[0], intervention.position[1]))
                            .zoom(18)
                            .bearing(0)
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    DronePhotosServiceImpl.getInstance().getDronePhotosbyIdIntervention(idIntervention, callback);
                }

                @Override
                public void onFailure(Call<Intervention> call, Throwable t) {
                    //DO NOTHING
                    Log.e("AlbumActivity", t.toString());
                }
            });


        }


    }

    private void initMap() {

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AlbumActivity newInstance(String idIntervention) {
        AlbumActivity fragment = new AlbumActivity();
        Bundle args = new Bundle();
        args.putString(ARG_ID, idIntervention);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        String[] parts = marker.getTitle().split(";");
        String part1 = parts[0];
        String part2 = parts[1];

        Intent i = new Intent(this.getContext(), ListPhotoActivity.class);
        i.putExtra("Value1", part1);
        i.putExtra("Value2", part2);
        i.putExtra("Value3", idIntervention);
        startActivity(i);
        return false;
    }
}