package istic.fr.droneproject.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import istic.fr.droneproject.R;
import istic.fr.droneproject.adapter.MapPointsRecyclerAdapter;
import istic.fr.droneproject.adapter.MapVehiculesRecyclerAdapter;
import istic.fr.droneproject.model.Categorie;
import istic.fr.droneproject.model.Drone;
import istic.fr.droneproject.model.DronePhotos;
import istic.fr.droneproject.model.DronePosition;
import istic.fr.droneproject.model.EtatVehicule;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.PointInteret;
import istic.fr.droneproject.model.TypePoint;
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
    Boolean clickedPoint = false;
    Vehicule vehiculeselected;
    Marker droneMarker;
    Drone drone;
    DronePosition dronePosition;
    DronePhotos dronePhotos;

  /*  PointInteret pointSelected;*/
    int pointSelected;
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
    View m_menu_Actiondrone;
    public enum ListeMenu {
        m_menu_vehicules, m_menu_points, m_menu_choix, m_menu_Actionvehicule, m_menu_Actionpoint, m_menu_Actiondrone, aucun
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
        m_menu_Actiondrone = (LinearLayout) view.findViewById(R.id.m_menu_Actiondrone);

        Button points = (Button) view.findViewById(R.id.m_menu_choix_points);
        Button vehicule = (Button) view.findViewById(R.id.m_menu_choix_vehicules);
        Button deplacer = (Button) view.findViewById(R.id.m_menu_Actionvehicule_deplacer);
        Button confirmer = (Button) view.findViewById(R.id.m_menu_Actionvehicule_confirmer);
        Button liberer = (Button) view.findViewById(R.id.m_menu_Actionvehicule_liberer);
        Button parking = (Button) view.findViewById(R.id.m_menu_Actionvehicule_parking);

        //bouton suppression point
        Button m_menu_Actiondrone_segment= (Button) view.findViewById(R.id.m_menu_Actiondrone_segment);
        Button m_menu_Actiondrone_zone= (Button) view.findViewById(R.id.m_menu_Actiondrone_zone);
        Button m_menu_Actiondrone_stop= (Button) view.findViewById(R.id.m_menu_Actiondrone_stop);

        //bouton drone
        Button m_menu_Actionpoint_supprimer = (Button) view.findViewById(R.id.m_menu_Actionpoint_supprimer);
        Button m_menu_Actionpoint_deplacer = (Button) view.findViewById(R.id.m_menu_Actionpoint_deplacer);
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
            public void clickPoint(String nomImage) {
                //point
                final PointInteret pointInteret = new PointInteret();
                pointInteret.setCode_image(nomImage);
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

        recyclerViewPoints = (RecyclerView) view.findViewById(R.id.m_list_points);
        recyclerViewPoints.setLayoutManager(new LinearLayoutManager(getContext()));

        pointsAdapter = new MapPointsRecyclerAdapter(TypePoint.getAll(), R.layout.m_points_item, getContext(),pointsClickListener);
        recyclerViewPoints.setAdapter(pointsAdapter);

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
                    //oN CHERCHE LE VÉHICULE SÉLECTIONNER
                    if ((intervention.vehicules.get(i).position != null && vehiculeselected.position != null)
                            && (intervention.vehicules.get(i).position[0].toString().equals(vehiculeselected.position[0].toString()) && intervention.vehicules.get(i).position[1].toString().equals(vehiculeselected.position[1].toString()))
                            && (intervention.vehicules.get(i).nom.equals(vehiculeselected.nom))) {

                        if ((EtatVehicule.ENGAGE.equals(intervention.vehicules.get(i).etat) && (intervention.vehicules.get(i).heureEngagement != null) ) || (EtatVehicule.PARKING.equals(intervention.vehicules.get(i).etat)) ) {


                            intervention.vehicules.get(i).setEtat(EtatVehicule.ARRIVE);
                            if (intervention.vehicules.get(i).heureArrivee == null)
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
                        else {

                            Toast.makeText(getActivity(),
                                    "Veuillez attendre la validation par le CODIS avant de confirmer", Toast.LENGTH_LONG).show();
                        }
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

        m_menu_Actionpoint_supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        try {


                        intervention.points.remove(pointSelected);

                        InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.e("point suppprimé", "=========>point supprimé ");

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                //DO NOTHING

                            }
                        });
                        changerMenu(ListeMenu.aucun);
                        SynchroniserIntervention();


                        }
                        catch (Exception e){

                        }

            }
        });

        m_menu_Actionpoint_deplacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickedPoint = true;
                Toast.makeText(getContext(), "clicker sur la carte sur la nouvelle position du point", Toast.LENGTH_SHORT).show();

            }
        });


        //Appel périodique de reloadDrone
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after ###ms
                Log.e("MapActivityHandler","Ploop");
                reloadDrone();
                handler.postDelayed(this, 2000);
            }
        }, 2000);

        m_menu_Actiondrone_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: action stop sur le drone (changer drone.tat vers STOP)
            }
        });

        m_menu_Actiondrone_segment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: déclancher le placement de point pour un segments
            }
        });

        m_menu_Actiondrone_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            (vehiculeCourant.etat == EtatVehicule.PARKING)  || (vehiculeCourant.etat == EtatVehicule.DEMANDE || vehiculeCourant.etat == EtatVehicule.ENGAGE)
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
                        vehicule.position = new Double[2];
                        vehicule.position[0] = markerChanged.getPosition().latitude;
                        vehicule.position[1] = markerChanged.getPosition().longitude;
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
                        .title("Intervention")
                        .snippet(intervention.libelle));

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
                if(markerChanged != null) {
                    markerChanged.remove();
                    Log.e("MapActivity","markerChanged.remove(); line 667");
                }
                //les vehicules on un ii entre 0 et 999
                if(marker.getTitle() == null ||marker.getTitle().equals("Intervention")){
                    //do nothing but catch not a number marker
//                    SynchroniserIntervention();
//                    changerMenu(ListeMenu.aucun);
                }
                else if (Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) < 1000) {
                    changerMenu(ListeMenu.m_menu_Actionvehicule);
                    System.out.println("veh" +marker.getTitle());
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
                    System.out.println("heeeeeeeeeereeeeee");
                    String title = marker.getTitle();


                    pointSelected = Integer.parseInt(title.substring(title.length() - 1));
                    System.out.println("point selected "+pointSelected);
                }
                // les drones
                else if (Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) >= 2000 && Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) < 3000) {
                    changerMenu(ListeMenu.m_menu_Actiondrone);
                }
                // les points SP
                else if (Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) >= 3000){
                    changerMenu(ListeMenu.aucun);
                    if(markerChanged != null) {
                        markerChanged.remove();
                        Log.e("MapActivity", "markerChanged.remove(); line 705");
                    }
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
                if(clickedPoint) {
                    try {
                        Double nPos[] = new Double[2];
                        nPos[0] = point.latitude;
                        nPos[1] = point.longitude;
                        intervention.points.get(pointSelected).setPosition(nPos);
                        InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.e("point updated", "=========>point updated ");

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                //DO NOTHING

                            }
                        });
                        changerMenu(ListeMenu.aucun);
                        SynchroniserIntervention();


                    }
                    catch (Exception e){

                    }
                }
                else if(clicked){//on a cliquer sur un vehile et sur déplacer juste avant
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

                } else if (secondClickSurMap) {
                    changerMenu(ListeMenu.aucun);
                    secondClickSurMap = false;
                    markerChanged.remove();
                    Log.e("MapActivity","markerChanged.remove(); line 797");
                }
                else{



               /* myMarker.remove();*/
                if (markerChanged != null) {
                    markerChanged.remove();
                    Log.e("MapActivity","markerChanged.remove(); line 807");
                }
                markerChanged = mGoogleMap.addMarker(new MarkerOptions()
                        .position(point)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                        );

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

        if (markerChanged == null) {
            markerChanged = myMarker;
        }


        int drawableRes = titsev.FindImageIdByVehicule(vehicule);
        BitmapDrawable bmpDraw = (BitmapDrawable) getResources().getDrawable(drawableRes);
//        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(getResources().getIdentifier(drawableRes, "drawable", getContext().getPackageName()));
        Bitmap b = bmpDraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, iconSizeX, iconSizeY, false);
        Canvas canvas1 = new Canvas(smallMarker);

        // texte vehicule

        Paint stkPaint = new Paint();
        stkPaint.setStyle(Paint.Style.STROKE);
        stkPaint.setTextSize(25);
        stkPaint.setStrokeWidth(4);
        stkPaint.setColor(Color.BLACK);
        canvas1.drawText(vehicule.nom, iconSizeX / 20, iconSizeY / 5 * 3, stkPaint);

        Paint fillPaint = new Paint();
        fillPaint.setTextSize(25);
        fillPaint.setColor(TransformImageToStringEtVs.FindColorByVehicule(vehicule.categorie));
        canvas1.drawText(vehicule.nom, iconSizeX / 20, iconSizeY / 5 * 3, fillPaint);

        // add marker
        LatLng posVehicule;
        if (vehicule.position == null || vehicule.position[0] == null || vehicule.position[1] == null) {
            posVehicule = markerChanged.getPosition();
        } else {
            posVehicule = new LatLng(vehicule.position[0], vehicule.position[1]);
        }

        Marker newMarker = mGoogleMap.addMarker(new MarkerOptions().position(posVehicule)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
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
        try {

            mGoogleMap.clear();
            myMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(lng)
                    .title("Intervention")
            .snippet(intervention.libelle));
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
            if (pointsCarte != null) {
                for (int i = 0; i < pointsCarte.size(); i++) {
                    PointInteret pointCourant = pointsCarte.get(i);
                    ajoutImageFromPoint(pointCourant, 1000 + i);
                }
            }

            //ajouts des points SP
            if (pointsSPCarte != null) {
                for (int i = 0; i < pointsSPCarte.size(); i++) {
                    PointInteret pointCourant = pointsSPCarte.get(i);
                    ajoutImageFromPoint(pointCourant, 3000 + i);

                }
            }
        }
            catch(Exception e){

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
        m_menu_Actiondrone.setVisibility(View.GONE);

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
            case m_menu_Actiondrone:
                m_menu_Actiondrone.setVisibility(View.VISIBLE);
                break;
            case aucun:
                secondClickSurMap = false;
                if(markerChanged != null) {
                    markerChanged.remove();
                    Log.e("MapActivity", "markerChanged.remove(); line 996");

                    myMarker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(lng)
                            .title("Intervention")
                            .snippet(intervention.libelle));

                }
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

    private void reloadDrone(){
        //TODO: get la nouvelle position dans @dronePosition
        if(droneMarker == null){
            if(dronePosition != null && dronePosition.getPostion() != null && dronePosition.getPostion()[0] != null && dronePosition.getPostion()[1] != null){
                droneMarker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(lng)
                        .title("SuperDrone le sauveur des Petits chats")
                        .snippet(""+2000));
            }
            else{
                Log.e("MapActivity","Pas de dronePosition");
            }
        }
        else{
            droneMarker.setPosition(new LatLng(dronePosition.getPostion()[0],dronePosition.getPostion()[1]));
        }

    }
}

