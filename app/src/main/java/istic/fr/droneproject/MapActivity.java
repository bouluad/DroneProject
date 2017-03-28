package istic.fr.droneproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import istic.fr.droneproject.adapter.AlbumPhotoAdapter;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Photo;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends android.support.v4.app.Fragment implements OnMapReadyCallback {
    SupportMapFragment map;
    GoogleMap mGoogleMap;
    Marker myMarker;
    Marker markerChanged;
    ViewGroup view;
    Button boutonMenu;

    View m_menu_vehicules;
    View m_menu_points;
    View m_menu_choix;

    Button points;
    Button vehicules;

    private FragmentActivity myContext;//pour le fragment manager

    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }
    private Marker markerTest;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



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
               m_menu_choix.setVisibility(View.VISIBLE);

                //TODO afficher un marker custom
                Toast.makeText(getContext(), "Clock on :"+markerChanged.getPosition(), Toast.LENGTH_SHORT).show();
                LatLng SYDNEY = new LatLng(markerChanged.getPosition().latitude,markerChanged.getPosition().longitude);

//                markerTest = mGoogleMap.addMarker(new MarkerOptions()
//
//                        .position(SYDNEY)
//                        .title("Au feu, help, help, I'm burning, help me, please, Too hot")
//                        .snippet("Naaaaah, everything is fine !!!")
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ps_in)));

                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                Bitmap bmp = Bitmap.createBitmap(100, 100, conf);
                Canvas canvas1 = new Canvas(bmp);

// paint defines the text color, stroke width and size
                Paint color = new Paint();
                color.setTextSize(35);
                color.setColor(Color.BLACK);

// modify canvas
                canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.vh_eau), null, new RectF(0, 0, 100, 100), color);
                canvas1.drawText("User Name!", 30, 40, color);

// add marker to Map
                mGoogleMap.addMarker(new MarkerOptions().position(SYDNEY)
                        .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                        // Specifies the anchor to be at a particular point in the marker image.
                        .anchor(0.5f, 1));

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
                m_menu_choix.setVisibility(View.VISIBLE);
                m_menu_vehicules.setVisibility(View.GONE);
                m_menu_points.setVisibility(View.GONE);
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



    private static final String ARG_ID = "idIntervention";
    private String idIntervention;

    public MapActivity() {
        //Required empty constructor
    }

    public static MapActivity newInstance(String idIntervention) {
        MapActivity fragment = new MapActivity();
        Bundle args = new Bundle();
        args.putString(ARG_ID, idIntervention);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewPhotos = (RecyclerView) view.findViewById(R.id.album_liste);
        //TODO init les composants


        m_menu_choix = (LinearLayout) view.findViewById(R.id.m_menu_choix);
        m_menu_points = (LinearLayout) view.findViewById(R.id.m_menu_points);
        m_menu_vehicules = (LinearLayout) view.findViewById(R.id.m_menu_vehicules);

        points = (Button) view.findViewById(R.id.m_menu_choix_points);
        vehicules=(Button)  view.findViewById(R.id.m_menu_choix_vehicules);

        //listener pour le menu points
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_menu_points.setVisibility(View.VISIBLE);
                m_menu_choix.setVisibility(View.GONE);
                //findViewById(R.id.m_list_vehicules).setVisibility(View.VISIBLE);
            }
        });
        //Listener pour le menu vehicules
        vehicules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_menu_vehicules.setVisibility(View.VISIBLE);
                m_menu_choix.setVisibility(View.GONE);
                //findViewById(R.id.m_list_vehicules).setVisibility(View.VISIBLE);
            }
        });

        FragmentManager fragManager = myContext.getSupportFragmentManager(); //If using fragments from support v4
//        map = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.m_map);
        map = (SupportMapFragment) fragManager.findFragmentById(R.id.m_map);
        map.getMapAsync(this);

        InterventionServiceCentral.getInstance().getInterventionById(idIntervention, new Callback<Intervention>() {
            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {

            }

            @Override
            public void onFailure(Call<Intervention> call, Throwable t) {

            }
        });
    }
}
