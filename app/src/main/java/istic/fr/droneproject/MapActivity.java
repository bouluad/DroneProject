package istic.fr.droneproject;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
import istic.fr.droneproject.service.BaseSPService;
import istic.fr.droneproject.service.TransformImageToStringEtVs;
import istic.fr.droneproject.service.impl.BaseSPServiceImpl;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends Fragment implements OnMapReadyCallback {
    private static final String ARG_ID = "idIntervention";

    SupportMapFragment map;
    GoogleMap mGoogleMap;
    Marker myMarker;    //marker de position de l'intervention
    Marker markerChanged; //marker bleu avec la nouvelle position
    LatLng lng;
    ViewGroup view;
    RecyclerView recyclerViewVehicules;
    MapVehiculesRecyclerAdapter vehiculesAdapter;
    Intervention intervention;
    Vehicule vehicule;
    Boolean clicked = false;
    Vehicule vehiculeselected;
    private List<Vehicule> vehicules;

    //liste de points et vehicules synchroniser a afficher sur la carte
    private List<Vehicule> vehiculesCarte;
    private List<PointInteret> pointsCarte;
    private List<PointInteret> pointsSPCarte;

    //Booleans pour bloquer la synchro des interventions et stocker une notification de MAJ
    boolean synchronisationBloquer = false;
    boolean synchronisationNeedUpdate = false;
    boolean secondClickSurMap = false;
    boolean reloadingIntervention = false;
    //Service de convertion en image et couler
    TransformImageToStringEtVs titsev;

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
    private static final int iconSizeX = 100;
    private static final int iconSizeY = 60;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titsev = new TransformImageToStringEtVs(getContext());
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
        Button deplacer = (Button) view.findViewById(R.id.m_menu_Actionvehicule_deplacer);
        Button confirmer = (Button) view.findViewById(R.id.m_menu_Actionvehicule_confirmer);
        Button liberer = (Button) view.findViewById(R.id.m_menu_Actionvehicule_liberer);
        Button parking = (Button) view.findViewById(R.id.m_menu_Actionvehicule_parking);
        vehicules = new ArrayList<>();
        vehiculesCarte = new ArrayList<>();
        pointsCarte = new ArrayList<>();
        recyclerViewVehicules = (RecyclerView) view.findViewById(R.id.m_list_vehicules);
        recyclerViewVehicules.setLayoutManager(new LinearLayoutManager(getContext()));
        MapVehiculesRecyclerAdapter.VehiculeClickListener interventionClickListener = new MapVehiculesRecyclerAdapter.VehiculeClickListener() {
            @Override
            public void clickVehicule(final Vehicule vehicule) {
                Log.e("Vehicule cliqué", "Vehicule cliqué 1 =========>");
                //vehicule
                int k = intervention.vehicules.indexOf(vehicule);
                intervention.vehicules.get(k).setEtat(EtatVehicule.ENGAGE);
                m_listPositionVehicule = new Double[2];
                m_listPositionVehicule[0] = pointVehicule.latitude;
                m_listPositionVehicule[1] = pointVehicule.longitude;
                intervention.vehicules.get(k).setPosition(m_listPositionVehicule);
                InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("Vehicule cliqué", "=========>Vehicule cliqué ");
                                      /* TransformImageToStringEtVs titsev = new TransformImageToStringEtVs(getContext());
                                        markerChanged.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),titsev.FindImageIdByVehicule(vehicule))));
                                        markerChanged.setTitle(vehicule.nom);
                                        markerChanged.setSnippet(""+intervention.vehicules.indexOf(vehicule));*/

                        Toast.makeText(getContext(),"L'intervention a été Modifié",Toast.LENGTH_SHORT);
                        SynchroniserIntervention();
                        changerMenu(ListeMenu.aucun);

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //DO NOTHING
                        Log.e("MapActivity", t.toString());
                    }
                });
            }
        };
        vehiculesAdapter = new MapVehiculesRecyclerAdapter(vehicules, R.layout.m_vehicules_carte_item, getContext(), interventionClickListener);
        recyclerViewVehicules.setAdapter(vehiculesAdapter);

        MapPointsRecyclerAdapter.PointClickListener pointsClickListener = new MapPointsRecyclerAdapter.PointClickListener() {
            @Override
            public void clickPoint(final Pair<String, String> image) {

                //point
                final PointInteret pointInteret = new PointInteret();
                pointInteret.setCode_image(image.first);
                m_listPositionPoint = new Double[2];
                m_listPositionPoint[0] = pointVehicule.latitude;
                m_listPositionPoint[1] = pointVehicule.longitude;
                pointInteret.setPosition(m_listPositionPoint);
                if (intervention.points == null) {
                    intervention.points = new ArrayList<>();
                }
                intervention.points.add(pointInteret);

                InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        Toast.makeText(getContext(), "L'intervention a été modifiée", Toast.LENGTH_SHORT).show();
                        Log.e("Point cliqué", "=========>Point cliqué ");
                        Log.e("Point cliqué", m_listPositionPoint.toString());
                        Log.e("Point cliqué", "" + pointInteret.getCode_image());

                        String mDrawableName = pointInteret.getCode_image();
                        int resID = getResources().getIdentifier(mDrawableName, "drawable", getContext().getPackageName());


                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(pointInteret.getPosition()[0], pointInteret.getPosition()[1]))

                                .icon(BitmapDescriptorFactory.fromResource(resID))
                                .title("TEST"));
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //DO NOTHING
                        Log.e("MapActivity", t.toString());
                    }
                });
                SynchroniserIntervention();
            }
        };

        SynchroniserIntervention();

        final List<Pair<String, String>> m_images_points = new ArrayList<>();
        recyclerViewPoints = (RecyclerView) view.findViewById(R.id.m_list_points);
        recyclerViewPoints.setLayoutManager(new LinearLayoutManager(getContext()));
        pointsAdapter = new MapPointsRecyclerAdapter(m_images_points, R.layout.m_points_item, pointsClickListener);
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

     deplacer.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             clicked = true;
             Toast.makeText(getContext(), "clicker sur la carte sur la nouvelle position du vehicule", Toast.LENGTH_SHORT).show();
         }
     });

        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String heureArrivee = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
                for (int i = 0; i < intervention.vehicules.size(); i++) {
                    if ((intervention.vehicules.get(i).position != null && vehiculeselected.position != null)
                            && (intervention.vehicules.get(i).position[0].toString().equals(vehiculeselected.position[0].toString()) && intervention.vehicules.get(i).position[1].toString().equals(vehiculeselected.position[1].toString()))
                            && (intervention.vehicules.get(i).nom.equals(vehiculeselected.nom))) {

                        intervention.vehicules.get(i).setEtat(EtatVehicule.ARRIVE);
                        if(intervention.vehicules.get(i).heureArrivee==null)
                            intervention.vehicules.get(i).setHeureArrivee(heureArrivee);
                        InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.e("Vehicule confirmé", "=========>Vehicule confirmé ");

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                //DO NOTHING
                                Log.e("MapActivity", t.toString());
                            }
                        });
                        changerMenu(ListeMenu.aucun);
                        SynchroniserIntervention();
                    }
                }

            }
        });

        liberer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String heureLiberation =new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
                for (int i = 0; i < intervention.vehicules.size(); i++) {
                    if ((intervention.vehicules.get(i).position != null && vehiculeselected.position != null)
                            && (intervention.vehicules.get(i).position[0].toString().equals(vehiculeselected.position[0].toString()) && intervention.vehicules.get(i).position[1].toString().equals(vehiculeselected.position[1].toString()))
                            && (intervention.vehicules.get(i).nom.equals(vehiculeselected.nom))) {

                        intervention.vehicules.get(i).setEtat(EtatVehicule.LIBERE);
                        if(intervention.vehicules.get(i).heureLiberation==null)
                            intervention.vehicules.get(i).setHeureLiberation(heureLiberation);
                        InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.e("Vehicule libéré", "=========>Vehicule liberé ");

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                //DO NOTHING
                                Log.e("MapActivity", t.toString());
                            }
                        });
                        changerMenu(ListeMenu.aucun);
                        SynchroniserIntervention();
                    }
                }

            }
        });
        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (int i = 0; i < intervention.vehicules.size(); i++) {
                    if ((intervention.vehicules.get(i).position != null && vehiculeselected.position != null)
                            && (intervention.vehicules.get(i).position[0].toString().equals(vehiculeselected.position[0].toString()) && intervention.vehicules.get(i).position[1].toString().equals(vehiculeselected.position[1].toString()))
                            && (intervention.vehicules.get(i).nom.equals(vehiculeselected.nom))) {

                        intervention.vehicules.get(i).setEtat(EtatVehicule.PARKING);
                        InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.e("Vehicule Parking", "=========>Vehicule mise dans le Parking ");

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                //DO NOTHING
                                Log.e("MapActivity", t.toString());
                            }
                        });
                        changerMenu(ListeMenu.aucun);
                        SynchroniserIntervention();
                    }
                }

            }
        });

    }

    private void chargerIntervention() {
        InterventionServiceCentral.getInstance().getInterventionById(idIntervention, new Callback<Intervention>() {
            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                intervention = response.body();
                //rechargement des 2 listes de vehicules & points
                vehiculesCarte.clear();
                vehiculesCarte = intervention.vehicules;
                pointsCarte.clear();
                System.out.println("je suis laaaa");
              /*  recupererBaseSP();*/
                pointsCarte = intervention.points;
                //remplissage du tableau des vehicules placeable sur la carte
                Collections.reverse(response.body().vehicules);
                vehicules.clear();
                for (int i = 0; i < response.body().vehicules.size(); i++) {
                    Vehicule vehiculeCourant = response.body().vehicules.get(i);
                    if (
                            (vehiculeCourant.etat == EtatVehicule.PARKING || vehiculeCourant.etat == EtatVehicule.DEMANDE || vehiculeCourant.etat == EtatVehicule.ENGAGE)
                                    && ((vehiculeCourant.position == null) || (vehiculeCourant.position != null && vehiculeCourant.position[0] == null && vehiculeCourant.position[1] == null))) {
                        ajoutImageFromVehicule(vehiculesCarte.get(i), i);
                        vehicules.add(response.body().vehicules.get(i));
                    }
                }
                vehiculesAdapter.notifyDataSetChanged();

                reloadVehiculesPoints();
                changerMenu(ListeMenu.aucun);
                reloadingIntervention = false;
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
                                SynchroniserIntervention();
                                changerMenu(ListeMenu.aucun);
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

        recupererBaseSP();
        this.mGoogleMap = googleMap;
        InterventionServiceCentral.getInstance().getInterventionById(idIntervention, new Callback<Intervention>() {
            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                intervention = response.body();

                if (intervention.position != null && intervention.position[0] != null && intervention.position[1] != null) {
                    lng = new LatLng(intervention.position[0], intervention.position[1]);
                } else {
                    lng = new LatLng(40.76793169992044, -73.98180484771729);
                }
                /*recupererBaseSP();*/
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                myMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(lng)
                        .title("-1"));

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 18));

                SynchroniserIntervention();
                changerMenu(ListeMenu.aucun);
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
                changerMenu(ListeMenu.m_menu_choix);
                if(markerChanged != null)
                    markerChanged.remove();
                //les vehicules on un ii entre 0 et 999
                if (Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) < 1000) {
                    changerMenu(ListeMenu.m_menu_Actionvehicule);

                    vehiculeselected = vehiculesCarte.get(Integer.parseInt(marker.getTitle()));
                    //TODO on clique sur une icone d'un vehicule
                    Log.e("MapMarkerClick", "marker: " + marker);
                    Log.e("MapMarkerClick", "title: " + marker.getTitle());
                    Log.e("MapMarkerClick", "marker: " + marker.getSnippet());
                    try {
                        Log.e("MapMarkerClick", "in liste[" + marker.getTitle() + "]: " + vehiculesCarte.get(Integer.parseInt(marker.getTitle())));
                    } catch (Exception exception) {
                    }
                }
                //les points temporaire pout 1 intervention on un id entre 1000 et 2000
                else if (Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) >= 1000 && Integer.parseInt(marker.getTitle()) < 2000) {
                    //TODO
                    changerMenu(ListeMenu.m_menu_Actionpoint);
                }
                // les points SP
                else if (Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) >= 2000) {
                    //TODO Salma <1000 SP
                    changerMenu(ListeMenu.aucun);
                    if(markerChanged != null)
                        markerChanged.remove();
                }
                else{
                    SynchroniserIntervention();
                    changerMenu(ListeMenu.aucun);

                }
                return false;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Log.e("Map", "Map clicked");
                pointVehicule=point;
                if(clicked){//on a cliquer sur un vehile et sur déplacer juste avant
                    Double[]list =new Double[2];
                    list[0] = pointVehicule.latitude;
                    list[1] = pointVehicule.longitude;
                    Log.e("","vehicule.nom"+vehiculeselected.nom);
                    for(int i=0;i<intervention.vehicules.size();i++) {
                        if(intervention.vehicules.get(i).position !=null){
                            Double k1=intervention.vehicules.get(i).position[0];
                            Double k2=intervention.vehicules.get(i).position[1];}
                        Double s1=vehiculeselected.position[0];
                        Double s2=vehiculeselected.position[1];
                        String nom1=intervention.vehicules.get(i).nom;
                        String nom2=vehiculeselected.nom;

                        if ((intervention.vehicules.get(i).position !=null && vehiculeselected.position !=null)
                                &&(intervention.vehicules.get(i).position[0].toString().equals(vehiculeselected.position[0].toString() ) && intervention.vehicules.get(i).position[1].toString().equals(vehiculeselected.position[1].toString()) )
                                && (intervention.vehicules.get(i).nom.equals(vehiculeselected.nom))) {
                             intervention.vehicules.get(i).setEtat(EtatVehicule.ENGAGE);
                            intervention.vehicules.get(i).setPosition(list);
                            InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Log.e("Position Vehicule","=========>Position Vehicule updated ");

                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    //DO NOTHING
                                    Log.e("MapActivity", t.toString());
                                }
                            });
                            SynchroniserIntervention();
                            changerMenu(ListeMenu.aucun);
                            clicked=false;
                        }
                        // int k= intervention.vehicules.indexOf(vehiculeselected);
                    } // Log.e("",": "+k);


                    // animateMarker(myMarker,pointVehicule,false);

                }
                else if(secondClickSurMap == true){
                    changerMenu(ListeMenu.aucun);
                    secondClickSurMap = false;
                    markerChanged.remove();
                }
                else{



               /* myMarker.remove();*/
                if (markerChanged != null)
                    markerChanged.remove();
                markerChanged = mGoogleMap.addMarker(new MarkerOptions()
                        .position(point)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                        .title("-1"));

                Log.e("Position Marker", point.toString());
                changerMenu(ListeMenu.m_menu_choix);
                secondClickSurMap = true;

                }
            }
        });

    }

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
        Log.e("Map ajout vehicule", "Ahout de " + vehicule.nom + " a la position " + positionDansListeVehicules);
        //TODO afficher un marker custom
        if (markerChanged == null)
            markerChanged = myMarker;

        //LatLng SYDNEY = markerChanged.getPosition();

        //LatLng SYDNEY = markerChanged.getPosition();

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(iconSizeX, iconSizeY, conf);//taille de l'image a coordonée avec la taille de R.drawText
        Canvas canvas1 = new Canvas(bmp);

        // paint defines the text color, stroke width and size
        Paint color = new Paint();

        color.setTextSize(25);
        color.setColor(titsev.FindColorByVehicule(vehicule.categorie));
        //TODO choisir la bonne couleur
        // modify canvas

        //TODO utiliser le service de yousra pour charger la bonne image
        titsev.transformImageToString(titsev.FindImageIdByVehicule(vehicule));
        canvas1.drawBitmap(
                titsev.transformStringToImage(titsev.transformImageToString(titsev.FindImageIdByVehicule(vehicule)))

                , null, new RectF(0, 0, iconSizeX, iconSizeY), new Paint(Color.GREEN)); ///taille de l'image a coordinée avec la taille de bmp

        canvas1.drawText(vehicule.nom, iconSizeX / 20, iconSizeY / 5 * 3, color);

        // add marker to
        LatLng posVehicule;
        if (vehicule.position == null || vehicule.position[0] == null || vehicule.position[1] == null)
            posVehicule = markerChanged.getPosition();
        else
            posVehicule = new LatLng(vehicule.position[0], vehicule.position[1]);
        Marker newMarker = mGoogleMap.addMarker(new MarkerOptions().position(posVehicule)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.5f, 1));
        newMarker.setTitle("" + positionDansListeVehicules);
        newMarker.setSnippet(vehicule.nom);
    }

    /**
     * Methode pour ajouter sur la map un point
     */
    private void ajoutImageFromPoint(PointInteret point, int positionDansListePoints) {
            if (point.getCode_image() != null) {
                System.out.println(point.getCode_image());

                String mDrawableName = point.getCode_image().toLowerCase();
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(getResources().getIdentifier(mDrawableName, "drawable", getContext().getPackageName()));
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, iconSizeX/2, iconSizeX/2, false);



                       /* String mDrawableName = pointInteret.getCode_image().toLowerCase();
                        int resID = getResources().getIdentifier(mDrawableName, "drawable", getContext().getPackageName());*/


                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(point.getPosition()[0], point.getPosition()[1]))

                               /* .icon(BitmapDescriptorFactory.fromResource(resID))*/
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .title(""+positionDansListePoints)
                        .snippet(""+mDrawableName));
            }
    }



    /**
     * Methode qui converti un nom d'image en image
     */
    private Bitmap convertionDrawableToImageString(String drawableName) {
        //TODO faire une vrai convertion
        return BitmapFactory.decodeResource(getResources(),
                R.drawable.ve_hu);
    }

    /**
     * Méthode qui synchronise l'intervention et appel le rechargement des vehicules et points si l'utilisateur n'effectue pas d'intéraction.
     */
    private void SynchroniserIntervention() {
        //TODO avec idIntervention

        if(!synchronisationBloquer && mGoogleMap != null && !reloadingIntervention){
            reloadingIntervention = true;

            chargerIntervention();
        }
    }

    /**
     * Méthode qui parcourt la liste d'intervention et recupère la liste des véhicules et la liste des points, et la liste des points SP,
     * puis ajoute tout les points sur la map.
     */
    private void reloadVehiculesPoints() {
        mGoogleMap.clear();
        myMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(lng)
                .title("-1"));
        //ajouts des vehicules
        for (int i = 0; i < vehiculesCarte.size(); i++) {
            Vehicule vehiculeCourant = vehiculesCarte.get(i);
            if (
                    (vehiculeCourant.etat == EtatVehicule.DEMANDE || vehiculeCourant.etat == EtatVehicule.ENGAGE || vehiculeCourant.etat == EtatVehicule.ARRIVE)
                            && (vehiculeCourant.position != null && vehiculeCourant.position[0] != null && vehiculeCourant.position[1] != null)) {
                ajoutImageFromVehicule(vehiculesCarte.get(i), i);
            }
        }
        //ajouts des points
        for (int i = 0; i < pointsCarte.size(); i++) {
            PointInteret pointCourant = pointsCarte.get(i);
            ajoutImageFromPoint(pointCourant, 1000+i);

        }

        //ajouts des points SP
        for (int i = 0; i < pointsSPCarte.size(); i++) {
            PointInteret pointCourant = pointsSPCarte.get(i);
            ajoutImageFromPoint(pointCourant, 2000+i);

        }
    }

    /**
     * Methode pour afficher un seul menu a la foit
     */
    public void changerMenu(ListeMenu menu) {

        m_menu_vehicules.setVisibility(View.GONE);
        m_menu_points.setVisibility(View.GONE);
        m_menu_choix.setVisibility(View.GONE);
        m_menu_Actionpoint.setVisibility(View.GONE);
        m_menu_Actionvehicule.setVisibility(View.GONE);

        switch (menu) {
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
                secondClickSurMap = false;
                if(markerChanged != null)
                    markerChanged.remove();
                break;
        }
    }


    private void recupererBaseSP() {

        BaseSPService baseSP = new BaseSPServiceImpl();
        baseSP.getBaseSP(new Callback<List<PointInteret>>() {
            @Override
            public void onResponse(Call<List<PointInteret>> call, Response<List<PointInteret>> response) {
                pointsSPCarte = response.body();

            }

            @Override
            public void onFailure(Call<List<PointInteret>> call, Throwable t) {
                //DO NOTHING
                Log.e("UserInterventionsActivi", t.toString());
            }

        });
    }
}

