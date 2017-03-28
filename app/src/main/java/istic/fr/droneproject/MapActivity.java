package istic.fr.droneproject;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import istic.fr.droneproject.adapter.MapVehiculesRecyclerAdapter;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    SupportMapFragment map;
    GoogleMap mGoogleMap;
    Marker myMarker;
    Marker markerChanged;
    ViewGroup view;
    Button boutonMenu;
    RecyclerView recyclerViewVehicules;
    MapVehiculesRecyclerAdapter vehiculesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
         Button points = (Button) findViewById(R.id.m_menu_choix_points);
         Button vehicule=(Button)  findViewById(R.id.m_menu_choix_vehicules);
        final List<Vehicule> vehicules = new ArrayList<>();
        recyclerViewVehicules= (RecyclerView) findViewById(R.id.m_list_vehicules);
        recyclerViewVehicules.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        vehiculesAdapter=new MapVehiculesRecyclerAdapter(vehicules,R.layout.m_vehicules_item);
        recyclerViewVehicules.setAdapter(vehiculesAdapter);
      InterventionServiceCentral.getInstance().getListeVehicules("",new Callback<List<Vehicule>>() {
            @Override
            public void onResponse(Call<List<Vehicule>> call, Response<List<Vehicule>> response) {
                Collections.reverse(response.body());

               vehicules.clear();
               vehicules.addAll(response.body());
                vehiculesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Vehicule>> call, Throwable t) {
                //DO NOTHING
                Log.e("MapActivity", t.toString());
            }
        });



        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.m_menu_points).setVisibility(View.VISIBLE);
                //findViewById(R.id.m_list_vehicules).setVisibility(View.VISIBLE);
            }
        });
        vehicule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.m_menu_vehicules).setVisibility(View.VISIBLE);
                //findViewById(R.id.m_list_vehicules).setVisibility(View.VISIBLE);
            }
        });

        map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.m_map);
        map.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera

        this.mGoogleMap = googleMap;

        LatLng lng = new LatLng(40.76793169992044, -73.98180484771729);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        myMarker = this.mGoogleMap.addMarker(new MarkerOptions()
                .position(lng)
                .title("I'm here"));
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
               // marker.showInfoWindow();
               findViewById(R.id.m_menu_choix).setVisibility(View.VISIBLE);
                return false;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Log.e("Map", "Map clicked");
                myMarker.remove();
                if (markerChanged != null)
                    markerChanged.remove();
                markerChanged = mGoogleMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title("I'm here now"));

                Log.e("Position Marker", point.toString());

            }
        });
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(lng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mGoogleMap.moveCamera(center);
        mGoogleMap.animateCamera(zoom);

        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker args) {
                                              /* view=(ViewGroup) findViewById(R.id.activity_main);
                                                ArrayList<View> items=new ArrayList<View>();
                                                Button boutonPoint =new Button(getApplicationContext());
                                                Button boutonVehicule =new Button(getApplicationContext());
                                                boutonPoint.setText("Point");
                                                //boutonPoint.setOnClickListener();
                                                boutonVehicule.setText("Moyen");

                                                items.add(boutonPoint);
                                                items.add(boutonVehicule);
                                                view.addView(boutonPoint);
                                                view.addView(boutonVehicule);

                                                return view;*/
             //   boutonMenu = new Button(getApplicationContext());

               // boutonMenu.setText("Menu");
               /* boutonMenu.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        System.out.println("okeeeeeeeey====++>");
                        Log.d("bouton Menu cliqué", "");
                        view = (ViewGroup) findViewById(R.id.activity_main);
                        Button points = new Button(getApplicationContext());
                        points.setText("Points");
                        view.addView(points, 6);


                    }
                });*/

                return boutonMenu;

            }

            @Override
            public View getInfoContents(Marker marker) {

                return view;
            }



                                        }
        );}



}
