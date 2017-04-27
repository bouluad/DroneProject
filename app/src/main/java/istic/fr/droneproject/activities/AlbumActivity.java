package istic.fr.droneproject.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.DronePhotos;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.service.impl.DronePhotosServiceImpl;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by bouluad on 22/03/17.
 */
public class AlbumActivity extends Fragment
{

    private static final String ARG_ID = "idIntervention";
    private View mRootView;
    private MapView mMapView;
    private String idIntervention;
    private Intervention intervention;
    private GoogleMap map;
    private List<DronePhotos> dronePhotos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.album_fragment, container, false);
        initMap();
        mMapView = (MapView) mRootView.findViewById(R.id.photo_map);
        mMapView.onCreate(savedInstanceState);
        return mRootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            idIntervention = getArguments().getString(ARG_ID);

        }
        setupMap();
        bindPictures();

    }


    @Override
    public void onResume()
    {
        super.onResume();

        // map
        if(mMapView != null) mMapView.onResume();
    }


    @Override
    public void onPause()
    {
        super.onPause();

        // map
        if(mMapView != null) mMapView.onPause();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        // map
        if(mMapView != null) mMapView.onDestroy();
    }


    @Override
    public void onLowMemory()
    {
        super.onLowMemory();

        // map
        if(mMapView != null) mMapView.onLowMemory();
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        // save current instance state
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);

        // map
        if(mMapView != null) mMapView.onSaveInstanceState(outState);
    }


    private void bindData()
    {
        map = ((MapView) mRootView.findViewById(R.id.photo_map)).getMap();

        // content
        if(map != null)
        {
            BitmapDescriptor marker1 = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            BitmapDescriptor marker2 = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            BitmapDescriptor marker3 = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            BitmapDescriptor marker4 = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
            BitmapDescriptor[] markers = {marker1, marker2, marker3, marker4};

            for(int i = 0; i < 16; i++)
            {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(31.791702 + 0.001 * Math.sin(i * Math.PI / 8), -7.09262 + 0.001 * Math.cos(i * Math.PI / 8)))
                        .title("Example " + i)
                        .icon(markers[i % 4])
                );
            }
        }


        DronePhotosServiceImpl service = new DronePhotosServiceImpl();
        service.getDronePhotosbyIdIntervention(idIntervention, new Callback<List<DronePhotos>>() {
            @Override
            public void onResponse(Call<List<DronePhotos>> call, Response<List<DronePhotos>> response) {
                dronePhotos = response.body();


                System.out.println("DRONE PHOTO NAME "+dronePhotos.get(0).path);
            }

            @Override
            public void onFailure(Call<List<DronePhotos>> call, Throwable t) {
                //DO NOTHING
                Log.e("AlbumActivity", t.toString());
            }
        });



    }


    private void bindPictures(){

        System.out.println("BIND PICTURES");
        map = ((MapView) mRootView.findViewById(R.id.photo_map)).getMap();

        // content
        if(map != null)
        {





            DronePhotosServiceImpl service = new DronePhotosServiceImpl();
            service.getDronePhotosbyIdIntervention(idIntervention, new Callback<List<DronePhotos>>() {
                @Override
                public void onResponse(Call<List<DronePhotos>> call, Response<List<DronePhotos>> response) {
                    dronePhotos = response.body();

                    System.out.println("BIND PICTURES size : "+dronePhotos.size());

                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    map.setMyLocationEnabled(true);

                    UiSettings settings = map.getUiSettings();
                    settings.setAllGesturesEnabled(true);
                    settings.setMyLocationButtonEnabled(true);
                    settings.setZoomControlsEnabled(true);

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(intervention.position[0], intervention.position[1]))
                            .zoom(18)
                            .bearing(0)
                            .tilt(30)
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    for (DronePhotos p : dronePhotos) {

                        MarkerOptions markerOne = new MarkerOptions().position(new LatLng(p.position[0], p.position[1])).title(p.nom);

                        PicassoMarker  target = new PicassoMarker(map.addMarker(markerOne));
                        Picasso.with(getContext())
                                .load(p.path)
                                .resize(300,300)
                                .into(target);
                    }

                }

                @Override
                public void onFailure(Call<List<DronePhotos>> call, Throwable t) {
                    //DO NOTHING
                    Log.e("AlbumActivity", t.toString());
                }
            });

        }


    }

    private void initMap()
    {

        try
        {
            MapsInitializer.initialize(getActivity());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private void setupMap()
    {
        // reference
        map = ((MapView) mRootView.findViewById(R.id.photo_map)).getMap();

        // settings
        if(map != null)
        {


            InterventionServiceCentral.getInstance().getInterventionById(idIntervention, new Callback<Intervention>() {
                @Override
                public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                    intervention = response.body();

                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    map.setMyLocationEnabled(true);

                    UiSettings settings = map.getUiSettings();
                    settings.setAllGesturesEnabled(true);
                    settings.setMyLocationButtonEnabled(true);
                    settings.setZoomControlsEnabled(true);

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(intervention.position[0], intervention.position[1]))
                            .zoom(18)
                            .bearing(0)
                            .tilt(30)
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    BitmapDescriptor marker4 = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
                    Bitmap marker5 = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.drone);
                    Bitmap scaled = Bitmap.createScaledBitmap(marker5, 300,300,false);

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(intervention.position[0] , intervention.position[1]))
                            .title("Example ")
                            .icon(BitmapDescriptorFactory.fromBitmap((scaled)))
                    );
                    System.out.println("ID INTERVENTION "+intervention._id);
                }

                @Override
                public void onFailure(Call<Intervention> call, Throwable t) {
                    //DO NOTHING
                    Log.e("AlbumActivity", t.toString());
                }
            });

        }
    }

    public static AlbumActivity newInstance(String idIntervention) {
        AlbumActivity fragment = new AlbumActivity();
        Bundle args = new Bundle();
        args.putString(ARG_ID, idIntervention);
        fragment.setArguments(args);
        return fragment;
    }

}