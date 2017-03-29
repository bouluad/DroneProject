package istic.fr.droneproject;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import istic.fr.droneproject.adapter.MapPointsRecyclerAdapter;
import istic.fr.droneproject.adapter.MapVehiculesRecyclerAdapter;
import istic.fr.droneproject.model.Categorie;
import istic.fr.droneproject.model.EtatVehicule;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.PointInteret;
import istic.fr.droneproject.model.TypeVehicule;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.TransformImageToStringEtVs;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends Fragment implements OnMapReadyCallback {
    private static final String ARG_ID = "idIntervention";
    SupportMapFragment map;
    GoogleMap mGoogleMap;
    Marker myMarker;
    Marker markerChanged;
    LatLng lng;
    ViewGroup view;
    Button boutonMenu;
    RecyclerView recyclerViewVehicules;
    MapVehiculesRecyclerAdapter vehiculesAdapter;
    Intervention intervention;
    Vehicule vehicule;
    private List<Vehicule> vehicules;

    //liste de points et vehicules synchroniser a afficher sur la carte
    private List<Vehicule> vehiculesCarte;
    private List<PointInteret> pointsCarte;

    //Booleans pour bloquer la synchro des interventions et stocker une notification de MAJ
    boolean synchronisationBloquer = false;
    boolean synchronisationNeedUpdate = false;

    //Service de convertion en image et couler
    TransformImageToStringEtVs titsev = new TransformImageToStringEtVs(getContext());

    RecyclerView recyclerViewPoints;
    MapPointsRecyclerAdapter pointsAdapter;
    View m_menu_vehicules;
    View m_menu_points;
    LatLng pointVehicule;
    View m_menu_choix;
    Double[] m_listPositionPoint;
    Double[] m_listPositionVehicule;
    View m_menu_Actionvehicule;
    View m_menu_Actionpoint;

    public enum ListeMenu {
        m_menu_vehicules, m_menu_points, m_menu_choix, m_menu_Actionvehicule, m_menu_Actionpoint, aucun
    }
    private String idIntervention;

    //taille des icones sur la carte en X et en Y
    private static final int iconSizeX = 200;
    private static final int iconSizeY = 117;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idIntervention = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_map, container, false);
    }

    @Override
    public void onPause() {
        Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.m_map));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        final FragmentManager fragManager = this.getFragmentManager();
        final Fragment fragment = fragManager.findFragmentById(R.id.m_map);
        if (fragment != null) {
            fragManager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_menu_choix = (LinearLayout) view.findViewById(R.id.m_menu_choix);
        m_menu_points = (LinearLayout) view.findViewById(R.id.m_menu_points);
        m_menu_vehicules = (LinearLayout) view.findViewById(R.id.m_menu_vehicules);
        m_menu_Actionvehicule = (LinearLayout) view.findViewById(R.id.m_menu_Actionvehicule);
        m_menu_Actionpoint = (LinearLayout) view.findViewById(R.id.m_menu_Actionpoint);

        Button points = (Button) view.findViewById(R.id.m_menu_choix_points);
        Button vehicule = (Button) view.findViewById(R.id.m_menu_choix_vehicules);
        vehicules = new ArrayList<>();
        vehiculesCarte = new ArrayList<>();
        pointsCarte = new ArrayList<>();
        recyclerViewVehicules = (RecyclerView) view.findViewById(R.id.m_list_vehicules);
        recyclerViewVehicules.setLayoutManager(new LinearLayoutManager(getContext()));
       MapVehiculesRecyclerAdapter.VehiculeClickListener interventionClickListener = new MapVehiculesRecyclerAdapter.VehiculeClickListener() {
            @Override
            public void clickVehicule(final Vehicule vehicule) {
                Log.e("Vehicule cliqué","Vehicule cliqué 1 =========>");
                //vehicule
                               int k =intervention.vehicules.indexOf(vehicule);
                                intervention.vehicules.get(k).setEtat(EtatVehicule.ENGAGE);
                                m_listPositionVehicule = new Double[2];
                m_listPositionVehicule[0]=pointVehicule.latitude;
                m_listPositionVehicule[1]=pointVehicule.longitude;
                                intervention.vehicules.get(k).setPosition(m_listPositionVehicule);
                                InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {

                                        Toast.makeText(getContext(),"L'intervention a été Modifié",Toast.LENGTH_SHORT);
                                        Log.e("Vehicule cliqué","=========>Vehicule cliqué ");
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        //DO NOTHING
                                        Log.e("MapActivity", t.toString());
                                    }
                                });
            }
        };
        vehiculesAdapter = new MapVehiculesRecyclerAdapter(vehicules, R.layout.m_vehicules_carte_item, getContext(),interventionClickListener);
        recyclerViewVehicules.setAdapter(vehiculesAdapter);

        MapPointsRecyclerAdapter.PointClickListener pointsClickListener = new MapPointsRecyclerAdapter.PointClickListener() {
            @Override
            public void clickPoint(final Pair<String, String> image) {

                //point
                PointInteret pointInteret=new PointInteret();
                pointInteret.setCode_image(image.first);
                m_listPositionPoint = new Double[2];
                m_listPositionPoint[0]=pointVehicule.latitude;
                m_listPositionPoint[1]=pointVehicule.longitude;
                pointInteret.setPosition(m_listPositionPoint);
                if(intervention.points == null){
                    intervention.points = new ArrayList<>();
                }
                intervention.points.add(pointInteret);

                InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        Toast.makeText(getContext(),"L'intervention a été modifiée",Toast.LENGTH_SHORT).show();
                        Log.e("Point cliqué","=========>Point cliqué ");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //DO NOTHING
                        Log.e("MapActivity", t.toString());
                    }
                });
                chargerIntervention();
            }
        };

       chargerIntervention();

        final List<Pair<String, String>> m_images_points = new ArrayList<>();
        recyclerViewPoints = (RecyclerView) view.findViewById(R.id.m_list_points);
        recyclerViewPoints.setLayoutManager(new LinearLayoutManager(getContext()));
        pointsAdapter = new MapPointsRecyclerAdapter(m_images_points, R.layout.m_points_item,pointsClickListener);
        recyclerViewPoints.setAdapter(pointsAdapter);

        Bitmap largeIconeau = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.eau);
        ByteArrayOutputStream streameau = new ByteArrayOutputStream();
        largeIconeau.compress(Bitmap.CompressFormat.JPEG, 100, streameau);
        byte[] byteFormateau = streameau.toByteArray();
        String encodedImageeau = Base64.encodeToString(byteFormateau, Base64.NO_WRAP);

        Bitmap largeIconpseau = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ps_eau);
        ByteArrayOutputStream streampseau = new ByteArrayOutputStream();
        largeIconpseau.compress(Bitmap.CompressFormat.JPEG, 100, streampseau);
        byte[] byteFormatpseau = streampseau.toByteArray();
        String encodedImagepseau = Base64.encodeToString(byteFormatpseau, Base64.NO_WRAP);

        Bitmap largeIconps_hu = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ps_hu);
        ByteArrayOutputStream streamps_hu = new ByteArrayOutputStream();
        largeIconps_hu.compress(Bitmap.CompressFormat.JPEG, 100, streamps_hu);
        byte[] byteFormatps_hu = streamps_hu.toByteArray();
        String encodedImageps_hu = Base64.encodeToString(byteFormatps_hu, Base64.NO_WRAP);


        Bitmap largeIconps_in = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ps_in);
        ByteArrayOutputStream streamps_in = new ByteArrayOutputStream();
        largeIconps_in.compress(Bitmap.CompressFormat.JPEG, 100, streamps_in);
        byte[] byteFormatps_in = streamps_in.toByteArray();
        String encodedImageps_in = Base64.encodeToString(byteFormatps_in, Base64.NO_WRAP);

        Bitmap largeIconps_rp = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ps_rp);
        ByteArrayOutputStream streamps_rp = new ByteArrayOutputStream();
        largeIconps_rp.compress(Bitmap.CompressFormat.JPEG, 100, streamps_rp);
        byte[] byteFormatps_rp = streamps_rp.toByteArray();
        String encodedImageps_rp = Base64.encodeToString(byteFormatps_rp, Base64.NO_WRAP);
        m_images_points.add(new Pair<String, String>("eau", encodedImageeau));
        m_images_points.add(new Pair<String, String>("ps_eau", encodedImagepseau));
        m_images_points.add(new Pair<String, String>("ps_hu", encodedImageps_hu));
        m_images_points.add(new Pair<String, String>("ps_in", encodedImageps_in));
        m_images_points.add(new Pair<String, String>("ps_rp", encodedImageps_rp));
      /*  m_images_points.add(new Pair<String,String>("eau",m_transform.transform( R.drawable.eau)));
        m_images_points.add(new Pair<String,String>("ps_reau",m_transform.transform( R.drawable.ps_eau)));
      m_images_points.add(new Pair<String,String>("ps_hu",m_transform.transform( R.drawable.ps_hu) ));
      m_images_points.add(new Pair<String,String>("ps_in",m_transform.transform( R.drawable.ps_in)));
      m_images_points.add(new Pair<String,String>("ps_rp",m_transform.transform( R.drawable.ps_rp)));*/

        pointsAdapter.notifyDataSetChanged();


        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerMenu(ListeMenu.m_menu_points);
            }
        });
        vehicule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerMenu(ListeMenu.m_menu_vehicules);
            }
        });

        final FloatingActionButton btn_add_moyen = (FloatingActionButton) m_menu_vehicules.findViewById(R.id.m_list_vehicules_add);
        btn_add_moyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSimplePopUp();
            }
        });

        map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.m_map);
        map.getMapAsync(this);


    }

    private void chargerIntervention() {
        InterventionServiceCentral.getInstance().getInterventionById(idIntervention, new Callback<Intervention>() {
            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                intervention = response.body();
                Collections.reverse(response.body().vehicules);
                vehicules.clear();
                for (int i = 0; i < response.body().vehicules.size(); i++) {

                    if (response.body().vehicules.get(i).etat == EtatVehicule.PARKING || response.body().vehicules.get(i).etat == EtatVehicule.DEMANDE|| response.body().vehicules.get(i).position == null) {
                        vehicules.add(response.body().vehicules.get(i));
                    }
                }
                vehiculesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Intervention> call, Throwable t) {
                //DO NOTHING
                Log.e("MapActivity", t.toString());
            }
        });
    }

    /**
     * Methode pour l'ajout d'un vehicule
     */

    private void showSimplePopUp() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this.getActivity());
        helpBuilder.setTitle("Demander un véhicule");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View popupLayout = inflater.inflate(R.layout.codis_add_moyen_popup, null);


        final EditText nom_vehicule = (EditText) popupLayout.findViewById(R.id.nom_moyen);
        final Spinner popupSpinner = (Spinner) popupLayout.findViewById(R.id.spinnerCategorie);
        Categorie[] categories = Categorie.values();
        ArrayAdapter<Categorie> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        popupSpinner.setAdapter(adapter);


        final RadioGroup radiogroup = (RadioGroup) popupLayout.findViewById(R.id.type_radio);


        helpBuilder.setView(popupLayout);


        helpBuilder.setPositiveButton("Ajouter",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        //add to list vehicule
                        vehicule = new Vehicule();
                        vehicule.nom = nom_vehicule.getText().toString();

                        int selectedId = radiogroup.getCheckedRadioButtonId();
                        switch (selectedId) {
                            case R.id.type_radio_fpt:
                                vehicule.type = TypeVehicule.FPT;
                                break;
                            case R.id.type_radio_vlcg:
                                vehicule.type = TypeVehicule.VLCG;
                                break;
                            case R.id.type_radio_vsav:
                                vehicule.type = TypeVehicule.VSAV;
                                break;
                        }

                        vehicule.categorie = (Categorie) popupSpinner.getSelectedItem();

                        vehicule.etat = EtatVehicule.DEMANDE;
                        vehicule.heureDemande = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());

//                        vehicules.add(vehicule);
                        intervention.vehicules.add(vehicule);

                        InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                chargerIntervention();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }

                        });
                    }
                });
        helpBuilder.create().show();


    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        this.mGoogleMap = googleMap;
        InterventionServiceCentral.getInstance().getInterventionById(idIntervention, new Callback<Intervention>() {
            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
        Log.e("position","==========>Position Intervention"+intervention.position[0]+" "+intervention.position[1]);
         if(intervention.position!=null && intervention.position[0] != null && intervention.position[1] != null) {
             lng = new LatLng(intervention.position[0], intervention.position[1]);
         }
        else{
              lng = new LatLng(40.76793169992044, -73.98180484771729);}
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                Log.e("position","==========>Position Intervention"+intervention.position[0]+" "+intervention.position[1]);
                if(intervention.position!=null && intervention.position[0] != null && intervention.position[1] != null) {
                    lng = new LatLng(intervention.position[0], intervention.position[1]);
                }
                else{
                    lng = new LatLng(40.76793169992044, -73.98180484771729);}
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                myMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(lng)
                        .title("-1"));
                //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(lng, 10));




                /*CameraUpdate center =
                        CameraUpdateFactory.newLatLng(lng);

                CameraUpdate zoom = CameraUpdateFactory.zoomTo();

                mGoogleMap.moveCamera(center);
                mGoogleMap.animateCamera(zoom);*/

              // Move the camera instantly to hamburg with a zoom of 15.
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 15));

                // Zoom in, animating the camera.
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

            }

            @Override
            public void onFailure(Call<Intervention> call, Throwable t) {
                //DO NOTHING
                Log.e("MapActivity", t.toString());
            }
        });



        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // marker.showInfoWindow();
                SynchroniserIntervention();
                changerMenu(ListeMenu.m_menu_choix);
                if(Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) < 1000 ){
                    //TODO on clique sur une icone d'un vehicule
                    Log.e("MapMarkerClick", "marker: " + marker);
                    Log.e("MapMarkerClick", "title: " + marker.getTitle());
                    Log.e("MapMarkerClick", "marker: " + marker.getSnippet());
                    Log.e("MapMarkerClick", "in liste[" + marker.getTitle() + "]: " + vehicules.get(Integer.parseInt(marker.getTitle())));

                    changerMenu(ListeMenu.m_menu_Actionvehicule);
                }
                else if(Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) >= 1000 ) {
                    //TODO
                    changerMenu(ListeMenu.m_menu_Actionpoint);
                }
                else{
                    //TODO faire l'ajout depuis le menu vers la base
                    //TODO parcourir la liste des vehicules pour afficher les vehicules
                    m_menu_Actionvehicule.setVisibility(View.GONE);
                    Vehicule vTest = new Vehicule();
                    vTest.nom = "Batmobile"+vehicules.size();
                    vehicules.add(vTest);
                    ajoutImageFromVehicule(vTest, vehicules.size()-1);


                }
                return false;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Log.e("Map", "Map clicked");

                m_menu_Actionvehicule.setVisibility(View.GONE);
               /* myMarker.remove();*/
                pointVehicule=point;
                if (markerChanged != null)
                    markerChanged.remove();
                markerChanged = mGoogleMap.addMarker(new MarkerOptions()
                        .position(point)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                        .title("-1"));

                Log.e("Position Marker", point.toString());
                changerMenu(ListeMenu.m_menu_choix);
            }
        });


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
     * Methode pour ajouter sur la map un vehicule
     */
    private void ajoutImageFromVehicule(Vehicule vehicule, int positionDansListeVehicules) {
        Log.e("Map ajout vehicule","Ahout de "+vehicule.nom+" a la position "+positionDansListeVehicules);
        //TODO afficher un marker custom
            
        LatLng SYDNEY = markerChanged.getPosition();

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(iconSizeX, iconSizeY, conf);//taille de l'image a coordonée avec la taille de R.drawText
        Canvas canvas1 = new Canvas(bmp);

// paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(40);
        color.setColor(titsev.FindColorByVehicule(vehicule.categorie));
        TransformImageToStringEtVs titsev = new TransformImageToStringEtVs(getContext());
        //TODO choisir la bonne couleur
// modify canvas
        //TODO utiliser le service de yousra pour charger la bonne image
        titsev.transformImageToString(titsev.FindImageIdByVehicule(vehicule));
        canvas1.drawBitmap(
                titsev.transformStringToImage(titsev.transformImageToString(titsev.FindImageIdByVehicule(vehicule)))
                , null, new RectF(0, 0, iconSizeX, iconSizeY), color); ///taille de l'image a coordinée avec la taille de bmp
        canvas1.drawText(vehicule.nom, iconSizeX/20, iconSizeY/5*3, color);

// add marker to Map
        Marker newMarker = mGoogleMap.addMarker(new MarkerOptions().position(SYDNEY)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.5f, 1));
        newMarker.setTitle(""+positionDansListeVehicules);
        newMarker.setSnippet(vehicule.nom);
    }

    /**
     * Methode pour ajouter sur la map un point
     */
    private void ajoutImageFromPoint(PointInteret point, int positionDansListePoints) {

        Log.e("Map ajout point","Ajout de point a la position "+positionDansListePoints);
        //TODO afficher un marker custom
        LatLng SYDNEY = markerChanged.getPosition();

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(iconSizeX, iconSizeY, conf);//taille de l'image a coordonée avec la taille de R.drawText
        Canvas canvas1 = new Canvas(bmp);

// paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(40);
        color.setColor(Color.BLACK);
        //TODO choisir la bonne couleur
// modify canvas
        //TODO utiliser le service de yousra pour charger la bonne image
        canvas1.drawBitmap(convertionDrawableToImageString("eiage_eau"), null, new RectF(0, 0, iconSizeX, iconSizeY), color); ///taille de l'image a coordinée avec la taille de bmp
        canvas1.drawText(point.code_image, iconSizeX/20, iconSizeY/5*3, color);

// add marker to Map
        Marker newMarker = mGoogleMap.addMarker(new MarkerOptions().position(SYDNEY)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.5f, 1));
        newMarker.setTitle(""+positionDansListePoints);
        newMarker.setSnippet(point.code_image
        );
    }

    /**
     * Methode qui converti un nom d'image en image
     */
    private Bitmap convertionDrawableToImageString(String drawableName){
        //TODO faire une vrai convertion
        return BitmapFactory.decodeResource(getResources(),
                R.drawable.ve_hu);
    }

    /**
     *
     * Méthode qui synchronise l'intervention et appel le rechargement des vehicules et points si l'utilisateur n'effectue pas d'intéraction.
     */
    private void SynchroniserIntervention(){
        //TODO avec idIntervention
        if(!synchronisationBloquer){
            chargerIntervention();
            vehiculesCarte.clear();
            vehiculesCarte = intervention.vehicules;
            pointsCarte.clear();
            pointsCarte = intervention.points;
            reloadVehiculesPoints();
        }
    }

    /**
     * Méthode qui parcourt la liste d'intervention et recupère la liste des véhicules et la liste des points, et la liste des points SP,
     * puis ajoute tout les points sur la map.
     */
    private void reloadVehiculesPoints(){
    mGoogleMap.clear();
        //ajoits des vehicules
        for (int i = 0; i < vehicules.size(); i++) {
            ajoutImageFromVehicule(vehicules.get(i),i);
        }
    }

    /**
     *
     * Methode pour afficher un seul menu a la foit
     */
    public void changerMenu(ListeMenu menu){

        m_menu_vehicules.setVisibility(View.GONE);
        m_menu_points.setVisibility(View.GONE);
        m_menu_choix.setVisibility(View.GONE);
        m_menu_Actionpoint.setVisibility(View.GONE);
        m_menu_Actionvehicule.setVisibility(View.GONE);

        switch (menu){
            case m_menu_vehicules:
                m_menu_vehicules.setVisibility(View.VISIBLE);
                break;
            case m_menu_points:
                m_menu_points.setVisibility(View.VISIBLE);
                break;
            case m_menu_choix:
                m_menu_choix.setVisibility(View.VISIBLE);
                break;
            case m_menu_Actionvehicule:
                m_menu_Actionvehicule.setVisibility(View.VISIBLE);
                break;
            case m_menu_Actionpoint:
                m_menu_Actionpoint.setVisibility(View.VISIBLE);
                break;
            case aucun:
                break;
        }
    }
}
