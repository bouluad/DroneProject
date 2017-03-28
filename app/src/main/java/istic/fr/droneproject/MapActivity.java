package istic.fr.droneproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import istic.fr.droneproject.adapter.MapPointsRecyclerAdapter;
import istic.fr.droneproject.adapter.MapVehiculesRecyclerAdapter;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Photo;
import istic.fr.droneproject.model.PointInteret;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends Fragment implements OnMapReadyCallback {
    SupportMapFragment map;
    GoogleMap mGoogleMap;
    Marker myMarker;
    Marker markerChanged;
    ViewGroup view;
    Button boutonMenu;
    RecyclerView recyclerViewVehicules;
    MapVehiculesRecyclerAdapter vehiculesAdapter;
    RecyclerView recyclerViewPoints;
    MapPointsRecyclerAdapter pointsAdapter;

    View m_menu_vehicules;
    View m_menu_points;
    View m_menu_choix;
    private static final String ARG_ID = "idIntervention";
    private String idIntervention;


    @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_menu_choix = (LinearLayout) view.findViewById(R.id.m_menu_choix);
        m_menu_points = (LinearLayout) view.findViewById(R.id.m_menu_points);
        m_menu_vehicules = (LinearLayout) view.findViewById(R.id.m_menu_vehicules);

        Button points = (Button) view.findViewById(R.id.m_menu_choix_points);
        Button vehicule=(Button)  view.findViewById(R.id.m_menu_choix_vehicules);
        final List<Vehicule> vehicules = new ArrayList<>();
        recyclerViewVehicules= (RecyclerView) view.findViewById(R.id.m_list_vehicules);
        recyclerViewVehicules.setLayoutManager(new LinearLayoutManager(getContext()));
        vehiculesAdapter=new MapVehiculesRecyclerAdapter(vehicules,R.layout.m_vehicules_item,getContext());
        recyclerViewVehicules.setAdapter(vehiculesAdapter);

        InterventionServiceCentral.getInstance().getInterventionById("58d1327e5bce7c234254cf28",new Callback<Intervention>() {
            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                Collections.reverse(response.body().vehicules);
                vehicules.clear();
                vehicules.addAll(response.body().vehicules);
                vehiculesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Intervention> call, Throwable t) {
                //DO NOTHING
                Log.e("MapActivity", t.toString());
            }
        });

        final List<Pair<String,String>> m_images_points = new ArrayList<>();
        recyclerViewPoints= (RecyclerView) view.findViewById(R.id.m_list_points);
        recyclerViewPoints.setLayoutManager(new LinearLayoutManager(getContext()));
        pointsAdapter=new MapPointsRecyclerAdapter(m_images_points,R.layout.m_points_item);
        recyclerViewPoints.setAdapter(pointsAdapter);

       Bitmap largeIconeau= BitmapFactory.decodeResource(this.getResources(),
                R.drawable.eau);
        ByteArrayOutputStream streameau = new ByteArrayOutputStream();
        largeIconeau.compress(Bitmap.CompressFormat.JPEG, 100, streameau);
        byte[] byteFormateau = streameau.toByteArray();
        String encodedImageeau = Base64.encodeToString(byteFormateau, Base64.NO_WRAP);

        Bitmap largeIconpseau= BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ps_eau);
        ByteArrayOutputStream streampseau = new ByteArrayOutputStream();
        largeIconpseau.compress(Bitmap.CompressFormat.JPEG, 100, streampseau);
        byte[] byteFormatpseau = streampseau.toByteArray();
        String encodedImagepseau = Base64.encodeToString(byteFormatpseau, Base64.NO_WRAP);

        Bitmap largeIconps_hu= BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ps_hu);
        ByteArrayOutputStream streamps_hu = new ByteArrayOutputStream();
        largeIconps_hu.compress(Bitmap.CompressFormat.JPEG, 100, streamps_hu);
        byte[] byteFormatps_hu = streamps_hu.toByteArray();
        String encodedImageps_hu = Base64.encodeToString(byteFormatps_hu, Base64.NO_WRAP);


        Bitmap largeIconps_in= BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ps_in);
        ByteArrayOutputStream streamps_in = new ByteArrayOutputStream();
        largeIconps_in.compress(Bitmap.CompressFormat.JPEG, 100, streamps_in);
        byte[] byteFormatps_in = streamps_in.toByteArray();
        String encodedImageps_in = Base64.encodeToString(byteFormatps_in, Base64.NO_WRAP);

        Bitmap largeIconps_rp= BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ps_rp);
        ByteArrayOutputStream streamps_rp = new ByteArrayOutputStream();
        largeIconps_rp.compress(Bitmap.CompressFormat.JPEG, 100, streamps_rp);
        byte[] byteFormatps_rp = streamps_rp.toByteArray();
        String encodedImageps_rp = Base64.encodeToString(byteFormatps_rp, Base64.NO_WRAP);
        m_images_points.add(new Pair<String,String>("eau",encodedImageeau));
        m_images_points.add(new Pair<String,String>("ps_eau",encodedImagepseau));
        m_images_points.add(new Pair<String,String>("ps_hu",encodedImageps_hu));
        m_images_points.add(new Pair<String,String>("ps_in",encodedImageps_in));
      /*  m_images_points.add(new Pair<String,String>("eau",m_transform.transform( R.drawable.eau)));
        m_images_points.add(new Pair<String,String>("ps_reau",m_transform.transform( R.drawable.ps_eau)));
      m_images_points.add(new Pair<String,String>("ps_hu",m_transform.transform( R.drawable.ps_hu) ));
      m_images_points.add(new Pair<String,String>("ps_in",m_transform.transform( R.drawable.ps_in)));
      m_images_points.add(new Pair<String,String>("ps_rp",m_transform.transform( R.drawable.ps_rp)));*/

        pointsAdapter.notifyDataSetChanged();



        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_menu_points.setVisibility(View.VISIBLE);
                m_menu_choix.setVisibility(View.GONE);
                //findViewById(R.id.m_list_vehicules).setVisibility(View.VISIBLE);
            }
        });
        vehicule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_menu_vehicules.setVisibility(View.VISIBLE);
                m_menu_choix.setVisibility(View.GONE);
                //findViewById(R.id.m_list_vehicules).setVisibility(View.VISIBLE);
            }
        });

        map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.m_map);
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
               m_menu_choix.setVisibility(View.VISIBLE);

                Vehicule vTest = new Vehicule();
                vTest.nom = "Batcopter";
                ajoutImageFromVehicule(vTest);
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
    public static MapActivity newInstance(String idIntervention) {
        MapActivity fragment = new MapActivity();
        Bundle args = new Bundle();
        args.putString(ARG_ID, idIntervention);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * Methode pour ajouter sur la map un vehicule
     *
     */
    private void ajoutImageFromVehicule(Vehicule vehicule) {

        //TODO afficher un marker custom
        LatLng SYDNEY = markerChanged.getPosition();

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(200, 200, conf);//taille de l'image a coordonée avec la taille de R.drawText
        Canvas canvas1 = new Canvas(bmp);

// paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(40);
        color.setColor(Color.BLACK);

// modify canvas
        canvas1.drawBitmap(convertionDrawableToImageString("eiage_eau"), null, new RectF(0, 0, 200, 200), color); ///taille de l'image a coordinée avec la taille de bmp
        canvas1.drawText(vehicule.nom, 10, 150, color);

// add marker to Map
        mGoogleMap.addMarker(new MarkerOptions().position(SYDNEY)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.5f, 1));


    }

    /**
     *
     * Methode pour ajouter sur la map un point
     *
     */
    private void ajoutImageFromPoint(PointInteret point){

    }

    /**
     *
     * Methode qui converti un nom d'image en image
     */
    private Bitmap convertionDrawableToImageString(String drawableName){
        //TODO faire une vrai convertion
        return BitmapFactory.decodeResource(getResources(),
                R.drawable.ve_hu);
    }
}
