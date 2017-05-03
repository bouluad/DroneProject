package istic.fr.droneproject.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.adapter.MapPointsRecyclerAdapter;
import istic.fr.droneproject.adapter.MapVehiculesRecyclerAdapter;
import istic.fr.droneproject.model.Categorie;
import istic.fr.droneproject.model.Drone;
import istic.fr.droneproject.model.DronePhotos;
import istic.fr.droneproject.model.DronePosition;
import istic.fr.droneproject.model.EtatDrone;
import istic.fr.droneproject.model.EtatVehicule;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.PointInteret;
import istic.fr.droneproject.model.Segment;
import istic.fr.droneproject.model.TypePoint;
import istic.fr.droneproject.model.TypeVehicule;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.model.Zone;
import istic.fr.droneproject.service.BaseSPService;
import istic.fr.droneproject.service.TransformImageToStringEtVs;
import istic.fr.droneproject.service.impl.BaseSPServiceImpl;
import istic.fr.droneproject.service.impl.DronePositionServiceImpl;
import istic.fr.droneproject.service.impl.DroneServiceImpl;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActivity extends Fragment implements OnMapReadyCallback {
    private static final String ARG_ID = "idIntervention";

    SupportMapFragment map;
    GoogleMap mGoogleMap;
    boolean notcheckedMoyens=false;
    boolean notcheckedPoints=false;
    boolean notcheckedSegment=false;
    boolean notcheckedZone=false;
    boolean filtreMoyens=false;
    boolean  filtrePoints=false;
    boolean filtrePointsSP=false;
    boolean filtreSegment=false;
    boolean filtreZone=false;
    Marker myMarker;    //marker de position de l'intervention
    Marker markerChanged; //marker bleu avec la nouvelle position
    LatLng lng; //la position de l'intervention ?
    LatLng LastLatLng;
    RecyclerView recyclerViewVehicules;
    MapVehiculesRecyclerAdapter vehiculesAdapter;
    Intervention intervention;
    Vehicule vehicule;
    DronePosition droneposition;
    DronePhotos dronephotos;
    Drone drone;
    Boolean clicked = false; //pour les moyens
    Boolean clickedPoint = false; //pour les points
    Boolean clickedSegment = false; //pour les zegments du drone
    Boolean clickedZone = false; //pour les zone du drones
    Boolean clickedZoneExclusion = false; //pour les zones d'exclusions du drone
    Vehicule vehiculeselected;
    Marker droneMarker;
    Segment segment;
    List<Marker> markersMoyens;

    // points pr dessiner une zone
    Marker markerStart; // marker start zone
    List<Polyline> polylinesZone;  //liste de segments pr une zone
    List<Double[]> contours;
    ArrayList<LatLng> markerPoints; //liste de points pour une zone
    Polygon polygonZone = null; //zone sur la carte

    // pr les zones d'exclusion
    /*List<Polyline> polylinesZoneExclusion;  //liste de segments pr une zone

    ArrayList<LatLng> markerPointsExclusion;
*/


    /*  PointInteret pointSelected;*/
    int pointSelected;
    private List<Vehicule> vehicules;

    // points pour dessiner les segments

    List<Polyline> polylineList;
    List<Marker> markers;
    List<Double[]> pointsSegment;
    private boolean boucleSegment = false; //indique si le drone boucle
    private Polyline boucleSegmentPoly = null; //le polyline de boucle

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
    View m_menu_Actiondrone_segment;
    View m_menu_Actiondrone_zone;
    //boutons pour le drone
    Button m_menu_Actiondrone_zone_b;
    Button m_menu_Actiondrone_segment_b;
    Button m_menu_Actiondrone_stop;
    Button m_menu_Actiondrone_parking;
    Button m_menu_Actiondrone_exclusion;

    public enum ListeMenu {
        m_menu_vehicules, m_menu_points, m_menu_choix, m_menu_Actionvehicule, m_menu_Actionpoint, m_menu_Actiondrone, m_menu_Actiondrone_segment, m_menu_Actiondrone_zone, aucun
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
        Log.e("MAPCycledevie", "onCreateView");
        return inflater.inflate(R.layout.activity_map, container, false);
    }

    @Override
    public void onPause() {
        Log.e("MAPCycledevie", "onPause");
        DroneServiceImpl.getInstance().deleteCallbacks();
        DronePositionServiceImpl.getInstance().deleteCallbacks();
        Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.m_map));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.e("MAPCycledevie", "onDestroy");
        super.onDestroy();
        final FragmentManager fragManager = this.getFragmentManager();
        final Fragment fragment = fragManager.findFragmentById(R.id.m_map);
        if (fragment != null) {
            fragManager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.e("MAPCycledevie", "onResume");
        //mCollectionPagerAdapter.notifyDataSetChanged();
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
        m_menu_Actiondrone_segment = (LinearLayout) view.findViewById(R.id.m_menu_Actiondrone_segment);
        m_menu_Actiondrone_zone = (LinearLayout) view.findViewById(R.id.m_menu_Actiondrone_zone);

        Button points = (Button) view.findViewById(R.id.m_menu_choix_points);
        Button vehicule = (Button) view.findViewById(R.id.m_menu_choix_vehicules);
        Button deplacer = (Button) view.findViewById(R.id.m_menu_Actionvehicule_deplacer);
        Button confirmer = (Button) view.findViewById(R.id.m_menu_Actionvehicule_confirmer);
        Button liberer = (Button) view.findViewById(R.id.m_menu_Actionvehicule_liberer);
        Button parking = (Button) view.findViewById(R.id.m_menu_Actionvehicule_parking);

        //bouton suppression point
        Button m_menu_Actionpoint_supprimer = (Button) view.findViewById(R.id.m_menu_Actionpoint_supprimer);
        Button m_menu_Actionpoint_deplacer = (Button) view.findViewById(R.id.m_menu_Actionpoint_deplacer);

        //bouton drone

        m_menu_Actiondrone_segment_b = (Button) view.findViewById(R.id.m_menu_Actiondrone_segment_b);
        m_menu_Actiondrone_zone_b = (Button) view.findViewById(R.id.m_menu_Actiondrone_zone_b);

        m_menu_Actiondrone_stop = (Button) view.findViewById(R.id.m_menu_Actiondrone_stop);

        m_menu_Actiondrone_exclusion = (Button) view.findViewById(R.id.m_menu_Actiondrone_exclusion);
        m_menu_Actiondrone_parking = (Button) view.findViewById(R.id.m_menu_Actiondrone_parking);

        Button m_menu_Actiondrone_segment_annuler = (Button) view.findViewById(R.id.m_menu_Actiondrone_segment_annule);
        Button m_menu_Actiondrone_segment_fin = (Button) view.findViewById(R.id.m_menu_Actiondrone_segment_fin);
        Button m_menu_Actiondrone_segment_supplast = (Button) view.findViewById(R.id.m_menu_Actiondrone_segment_supplast);
        Button m_menu_Actiondrone_segment_boucle = (Button) view.findViewById(R.id.m_menu_Actiondrone_segment_boucle);

        Button m_menu_Actiondrone_zone_annule = (Button) view.findViewById(R.id.m_menu_Actiondrone_zone_annule);
        Button m_menu_Actiondrone_zone_fin = (Button) view.findViewById(R.id.m_menu_Actiondrone_zone_fin);
        Button m_menu_Actiondrone_zone_supplast = (Button) view.findViewById(R.id.m_menu_Actiondrone_zone_supplast);

        //bouton de la carte
        FloatingActionButton m_map_reloaddrone = (FloatingActionButton) view.findViewById(R.id.m_map_reloaddrone);
        FloatingActionButton m_map_filtre = (FloatingActionButton) view.findViewById(R.id.m_map_filtre);

        vehicules = new ArrayList<>();
        vehiculesCarte = new ArrayList<>();
        pointsCarte = new ArrayList<>();
        recyclerViewVehicules = (RecyclerView) view.findViewById(R.id.m_list_vehicules);
        recyclerViewVehicules.setLayoutManager(new LinearLayoutManager(getContext()));
        MapVehiculesRecyclerAdapter.VehiculeClickListener interventionClickListener = new MapVehiculesRecyclerAdapter.VehiculeClickListener() {
            @Override
            public void clickVehicule(final Vehicule vehicule) {

                m_listPositionVehicule = new Double[2];
                m_listPositionVehicule[0] = pointVehicule.latitude;
                m_listPositionVehicule[1] = pointVehicule.longitude;

                boolean modifEffectuee = vehicule.engager(m_listPositionVehicule);

                if (modifEffectuee) {

                    InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.d("MapActivity", "Véhicule du parking passé à engagé");
                            SynchroniserIntervention();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            //DO NOTHING
                            Log.e("MapActivity", t.toString());
                        }
                    });
                    changerMenu(ListeMenu.aucun);

                } else {
                    Toast.makeText(getActivity(),
                            "Impossible de placer ce véhicule sur la carte", Toast.LENGTH_LONG).show();
                }
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

        pointsAdapter = new MapPointsRecyclerAdapter(TypePoint.getAll(), R.layout.m_points_item, getContext(), pointsClickListener);
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
                Toast.makeText(getContext(), "cliquez sur la carte sur la nouvelle position du vehicule", Toast.LENGTH_SHORT).show();
            }
        });

        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < intervention.vehicules.size(); i++) {
                    Vehicule vehiculeCourant = intervention.vehicules.get(i);

                    //On cherche le vehicule selectionne
                    if (vehiculeselected.equals(vehiculeCourant)) {

                        boolean modifEffectuee = vehiculeCourant.arriver();

                        if (modifEffectuee) {

                            InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Log.d("MapActivity", "Vehicule confirmé");
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    //DO NOTHING
                                    Log.e("MapActivity", t.toString());
                                }
                            });
                            changerMenu(ListeMenu.aucun);
                            SynchroniserIntervention();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Veuillez attendre l'engagement par le CODIS avant de confirmer", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });

        liberer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < intervention.vehicules.size(); i++) {
                    Vehicule vehiculeCourant = intervention.vehicules.get(i);

                    //On cherche le vehicule selectionne
                    if (vehiculeselected.equals(vehiculeCourant)) {

                        boolean modifEffectuee = vehiculeCourant.liberer();

                        if (modifEffectuee) {

                            InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Log.d("MapActivity", "Vehicule libéré");
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    //DO NOTHING
                                    Log.e("MapActivity", t.toString());
                                }
                            });
                            changerMenu(ListeMenu.aucun);
                            SynchroniserIntervention();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Impossible de libérer ce véhicule", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });
        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < intervention.vehicules.size(); i++) {
                    Vehicule vehiculeCourant = intervention.vehicules.get(i);

                    if (vehiculeselected.equals(vehiculeCourant)) {

                        boolean modifEffectuee = vehiculeCourant.parking();

                        if (modifEffectuee) {

                            InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Log.d("MapActivity", "Véhicule au parking");
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    //DO NOTHING
                                    Log.e("MapActivity", t.toString());
                                }
                            });
                            changerMenu(ListeMenu.aucun);
                            SynchroniserIntervention();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Impossible de mettre ce véhicule au parking.", Toast.LENGTH_LONG).show();
                        }
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


                } catch (Exception e) {

                }

            }
        });

        m_menu_Actionpoint_deplacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickedPoint = true;
                Toast.makeText(getContext(), "cliquez sur la carte sur la nouvelle position du point", Toast.LENGTH_SHORT).show();

            }
        });

        //#####################  DRONE  MENU ###############################################################
        m_menu_Actiondrone_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerMenu(ListeMenu.aucun);
                drone.etat = EtatDrone.STOP;
                DroneServiceImpl.getInstance().updateDrone(drone, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("Drone updated", String.valueOf(response.body()));
                        reloadDroneEtat();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Drone not updated", "");
                    }
                });

            }
        });

        m_menu_Actiondrone_segment_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerMenu(ListeMenu.m_menu_Actiondrone_segment);
                clickedSegment = true;
                Log.e("segment", "Segment crée");
            }
        });

        m_menu_Actiondrone_zone_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerMenu(ListeMenu.m_menu_Actiondrone_zone);
                markerPoints = new ArrayList<>();
                polylinesZone = new ArrayList<Polyline>();


                clickedZone = true;
                //TODO: déclancher le placement de point pour une zone
            }
        });

        m_menu_Actiondrone_exclusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pareil que zone mais crée une zone d'exclusion
                changerMenu(ListeMenu.m_menu_Actiondrone_zone);
               /* dessinerZone();
*/
                markerPoints = new ArrayList<>();
                polylinesZone = new ArrayList<Polyline>();
                /*clickedZone = true;*/
                clickedZoneExclusion = true;
                //TODO: déclancher le placement de point pour une zone d'exclusion
            }
        });

        m_menu_Actiondrone_parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerMenu(ListeMenu.aucun);
                drone.etat = EtatDrone.PARKING;
                DroneServiceImpl.getInstance().updateDrone(drone, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("Drone updated", String.valueOf(response.body()));
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Drone not updated", "");
                    }
                });
            }
        });

        //#####################  DRONE SEGMENT MENU ###############################################################

        m_menu_Actiondrone_segment_annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changerMenu(ListeMenu.aucun);

                if (pointsSegment != null)
                    pointsSegment.clear();
                else pointsSegment = new ArrayList<Double[]>();
                if (markers != null)
                    markers.clear();
                else markers = new ArrayList<Marker>();
                if (polylineList != null)
                    polylineList.clear();
                else polylineList = new ArrayList<Polyline>();
                boucleSegment = false;
                changerMenu(ListeMenu.m_menu_Actiondrone);
                clickedSegment = false;
                SynchroniserIntervention();
            }
        });

        m_menu_Actiondrone_segment_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pointsSegment != null && !pointsSegment.isEmpty()) {
                    drone.etat = EtatDrone.SEGMENT;
                    if(drone.segment == null)
                        drone.segment = new Segment();
                    drone.segment.setPoints(pointsSegment);
                    drone.segment.setBoucleFermee(boucleSegment);

                    DroneServiceImpl.getInstance().updateDrone(drone, new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.e("Drone updated", String.valueOf(response.body()));
                            Toast.makeText(getActivity().getApplicationContext(), "Segment ajouté avec succès", Toast.LENGTH_SHORT);
                            if (pointsSegment != null)
                                pointsSegment.clear();
                            if (markers != null)
                                markers.clear();
                            if (polylineList != null)
                                polylineList.clear();
                            boucleSegment = false;

                            clickedSegment = false;
                            changerMenu(ListeMenu.aucun);
                            reloadDroneEtat();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Drone not updated", "");
                            Toast.makeText(getActivity().getApplicationContext(), "Echec, Segment non ajouté", Toast.LENGTH_SHORT);
                        }
                    });


                }
            }
        });

        m_menu_Actiondrone_segment_supplast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!polylineList.isEmpty()) {
                    polylineList.get(polylineList.size() - 1).remove(); //remove de la carte
                    polylineList.remove(polylineList.size() - 1); // remove de la liste
                }

                if (!markers.isEmpty()) {
                    markers.get(markers.size() - 1).remove();
                    markers.remove(markers.size() - 1);
                }

                if (boucleSegment) {
                    if (boucleSegmentPoly != null)
                        boucleSegmentPoly.remove();
                    if (markers.size() > 0)
                        boucleSegmentPoly = mGoogleMap.addPolyline((new PolylineOptions())
                                .add(markers.get(markers.size() - 1).getPosition(), markers.get(0).getPosition()).width(6).color(Color.RED)
                                .visible(true));
                }

            }
        });

        m_menu_Actiondrone_segment_boucle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Yousra
                boucleSegment = !boucleSegment;
                if (boucleSegment) {
                    if (markers.size() > 0)
                        boucleSegmentPoly = mGoogleMap.addPolyline((new PolylineOptions())
                                .add(markers.get(markers.size() - 1).getPosition(), markers.get(0).getPosition()).width(6).color(Color.RED)
                                .visible(true));
                } else {
                    if (boucleSegmentPoly != null)
                        boucleSegmentPoly.remove();
                }

            }
        });

        //#####################  DRONE ZONE MENU ###############################################################

        m_menu_Actiondrone_zone_annule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //changerMenu(ListeMenu.aucun);
                /*polylinesZone.get(polylinesZone.size()-1).remove();
                markerPoints.remove(markerPoints.size()-1);*/
                for (Polyline line : polylinesZone) {
                    line.remove();
                }

                polylinesZone.clear();
                markerPoints.clear();
                if(markerStart != null) {
                    markerStart.remove();
                }

                clickedZone = false;

                SynchroniserIntervention();
                //reset la sélection du drone
                //TODO: annuler l'ajout de point au segment, leurs suppression de la carte


            }
        });


        m_menu_Actiondrone_zone_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changerMenu(ListeMenu.aucun);
                int color1; //couleur segment
                int color2; //couleur surface
                if(clickedZoneExclusion){
                   color1 =Color.RED;
                    color2= R.color.TransparentRed;

                }
                else{
                    color1 =Color.BLUE;
                    color2= R.color.TransparentBlue;

                }

                /****************fermer le polygone, redessiner tt le polygone*********************/
                mGoogleMap.addPolyline((new PolylineOptions())
                        .add(markerPoints.get(markerPoints.size() - 1), markerPoints.get(0)).width(6).color(color1)
                        .visible(true));

                PolygonOptions zoneOptions = new PolygonOptions();
                for (int i = 0; i < markerPoints.size(); i++) {
                    zoneOptions.add(markerPoints.get(i));
                }
                zoneOptions.add(markerPoints.get(0));
                zoneOptions.strokeColor(color2);


                zoneOptions.fillColor(color2);

                // Get back the mutable Polygon
                Polygon polygon = mGoogleMap.addPolygon(zoneOptions);
                markerStart.remove();
                /**********************************************************************************/

               /* clickedZoneExclusion=false;
                clickedZone = false;*/
                //valider la zone et l'envoyer au service REST

                /*************************** update data drone **************************************/
                if(clickedZoneExclusion){
                    List<Double[]> contours = new ArrayList<Double[]>();
                    List<List<Double[]>> exclusion = new ArrayList<List<Double[]>>();


                    for (LatLng point : markerPoints) {
                        Double[] tab = new Double[2];
                        tab[0] = point.latitude;
                        tab[1] = point.longitude;
                        contours.add(tab);
                    }
                    exclusion.add(contours);

                    if(drone.zone == null)
                        drone.zone = new Zone();
                    if(drone.zone.getExclusion() ==null){
                    drone.zone.setExclusion(exclusion);}
                    else{
                        drone.zone.getExclusion().add(contours);
                    }
                    drone.etat = EtatDrone.ZONE;


                }

                else { //zone normale

                    List<Double[]> contours = new ArrayList<Double[]>();


                    for (LatLng point : markerPoints) {
                        Double[] tab = new Double[2];
                        tab[0] = point.latitude;
                        tab[1] = point.longitude;
                        contours.add(tab);
                    }
                    if (drone.zone == null)
                        drone.zone = new Zone();
                    drone.zone.setContours(contours);
                    drone.etat = EtatDrone.ZONE;

                }

                /***********************************************************************************/

                clickedZoneExclusion=false;
                clickedZone = false;

                DroneServiceImpl.getInstance().updateDrone(drone, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("MapZoneFin","updateDrone sucess");

                        reloadDroneEtat();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("MapZoneFin","updateDrone failed");
                    }

                });
                changerMenu(ListeMenu.aucun);

            }
        });

        m_menu_Actiondrone_zone_supplast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: supprimer le dernier point de la zone ajouter, le supprimer de la carte, redessiner le polygone
                System.out.println("supprimer dernier");
                polylinesZone.get(polylinesZone.size() - 1).remove();
                markerPoints.remove(markerPoints.size() - 1);
            }
        });

        //######################################################################################################

        m_map_reloaddrone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: supprimer le dernier point de la zone ajouter, le supprimer de la carte, redessiner le polygone
                if (droneMarker != null) {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(droneposition.position[0], droneposition.position[1]), 18));
                    reloadDrone();
                    changerMenu(ListeMenu.m_menu_Actiondrone);
                } else
                    Toast.makeText(getActivity().getApplicationContext(), "Pas de drone disponible", Toast.LENGTH_SHORT);

            }
        });

        m_map_filtre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: faire la tache taiga #141, cf description
                Toast.makeText(getActivity().getApplicationContext(), "ici prochainement, un menu de filtre", Toast.LENGTH_SHORT);
                popUpFiltreCarte();
            }
        });

        //######################################################################################################
    }

    private void chargerIntervention() {
        InterventionServiceCentral.getInstance().getInterventionById(idIntervention, new Callback<Intervention>() {
            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                mGoogleMap.clear();//clean carte
                reloadDrone(); // remettre drone
                intervention = response.body();
                //rechargement des 2 listes de vehicules & points
                if (vehiculesCarte != null)
                    vehiculesCarte.clear();
                vehiculesCarte = intervention.vehicules;
                if (pointsCarte != null)
                    pointsCarte.clear();
              /*  recupererBaseSP();*/
                pointsCarte = intervention.points;
                //remplissage du tableau des vehicules placeable sur la carte
                Collections.reverse(response.body().vehicules);
                vehicules.clear();
                for (int i = 0; i < response.body().vehicules.size(); i++) {
                    Vehicule vehiculeCourant = response.body().vehicules.get(i);
                    if (
                            vehiculeCourant.etat == EtatVehicule.PARKING ||
                                    (vehiculeCourant.etat == EtatVehicule.DEMANDE && !Vehicule.positionValide(vehiculeCourant.position))) {
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
                reloadingIntervention = false;
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

                        vehicule.demander();

                        vehicule.position = new Double[2];
                        vehicule.position[0] = markerChanged.getPosition().latitude;
                        vehicule.position[1] = markerChanged.getPosition().longitude;

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

    /**
     * Methode pour le filtre des Moyens/Points et drone sur la carte
     */

    private void popUpFiltreCarte() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View popupLayout = inflater.inflate(R.layout.user_filtre_objets_popup, null);
        helpBuilder.setView(popupLayout);
        final Switch mySwitchMoyens=(Switch)popupLayout.findViewById(R.id.mySwitch1);
        final Switch mySwitchPoints=(Switch)popupLayout.findViewById(R.id.mySwitch2);
        final Switch mySwitchSegment=(Switch)popupLayout.findViewById(R.id.mySwitch3);
        final Switch mySwitchZone=(Switch)popupLayout.findViewById(R.id.mySwitch4);
        final RadioGroup radiogroup = (RadioGroup) popupLayout.findViewById(R.id.type_carte);

      if(!notcheckedMoyens)
        //set the switch1 to ON
        mySwitchMoyens.setChecked(true);

    if(!notcheckedPoints)
        //set the switch2 to ON
        mySwitchPoints.setChecked(true);

        if(!notcheckedSegment)
        //set the switch3 to ON
        mySwitchSegment.setChecked(true);

        if(!notcheckedZone)
        //set the switch4 to ON
        mySwitchZone.setChecked(true);

        //set the switch5 to ON
      //  mySwitchcarte.setChecked(true);

        //attach a listener to check for changes in state
        mySwitchMoyens.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {

                    filtreMoyens = false;
                    notcheckedMoyens=false;
                    SynchroniserIntervention();
                } else {
                    filtreMoyens = true;
                    notcheckedMoyens=true;
                    SynchroniserIntervention();

                }

            }
        });

        mySwitchPoints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    filtrePoints = false;
                    filtrePointsSP = false;
                    notcheckedPoints=false;

                    SynchroniserIntervention();
                } else {
                    filtrePoints = true;
                    filtrePointsSP = true;
                    notcheckedPoints=true;
                    SynchroniserIntervention();

                }

            }
        });

        mySwitchSegment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    filtreSegment=false;
                    notcheckedSegment=false;
                    SynchroniserIntervention();

                }else{
                    filtreSegment=true;
                    notcheckedSegment=true;
                    SynchroniserIntervention();


                }

            }
        });
        mySwitchZone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if(isChecked){
                    filtreZone=false;
                    notcheckedZone=false;
                    SynchroniserIntervention();

                }else{
                    filtreZone=true;
                    notcheckedZone=true;
                    SynchroniserIntervention();


                }

            }
        });

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
        switch (checkedId) {

            case R.id.type_carte_satel:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                break;
            case R.id.type_carte_terrain:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                break;
        }

        }
        });


        helpBuilder.create().show();


    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        polylineList = new ArrayList<>();
        contours = new ArrayList<Double[]>();
        markers = new ArrayList<>();
        markersMoyens = new ArrayList<>();
        pointsSegment = new ArrayList<>();


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

        Log.e("OnMapReady", " calling async REST get dronePosition");
        DronePositionServiceImpl.getInstance().getDronePositionByIdIntervention(idIntervention, new Callback<DronePosition>() {
            @Override
            public void onResponse(Call<DronePosition> call, Response<DronePosition> response) {
//               Marker MarkerDrone = mGoogleMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(response.body().getPostion()[0],response.body().getPostion()[1]))
//                    .title("Drone"));


                droneposition = response.body();
                //Yousra
                Log.e("OnMapReady", "Drone Position is" + String.valueOf(response.body().position[0])+" "+String.valueOf(response.body().position[0]));
                reloadDrone();

            }

            @Override
            public void onFailure(Call<DronePosition> call, Throwable t) {
                Log.e("OnMapReady", "Drone not created");
            }
        });

        //charger l'état du drone la 1er fois
        reloadDroneEtat();

        //#########################    QUAND ON CLICK SUR UN MARQUEUR SUR LA CARTE GOOGLE MAP
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


               if (clickedZone || clickedZoneExclusion) {
                    System.out.println("dessiner une zone2");

                    markerPoints.add(marker.getPosition());

                    if (markerPoints.size() == 1) {

                        markerStart = mGoogleMap.addMarker(new MarkerOptions()
                                .position(marker.getPosition())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerstart))
                                .title("start"));


                    }
                    if (markerPoints.size() > 1) {

                        int i = markerPoints.size() - 2;

                        if(clickedZoneExclusion) {
                            Polyline polyline = mGoogleMap.addPolyline((new PolylineOptions())
                                    .add(markerPoints.get(i), markerPoints.get(i + 1)).width(6).color(Color.RED)
                                    .visible(true));

                            polylinesZone.add(polyline);

                        }
                        else  {
                            Polyline polyline = mGoogleMap.addPolyline((new PolylineOptions())
                                    .add(markerPoints.get(i), markerPoints.get(i + 1)).width(6).color(Color.BLUE)                                    .visible(true));

                            polylinesZone.add(polyline);

                        }


                    }
                } else if (clickedSegment ) {
                    //Yousra

                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting latitude and longitude of the marker position
                    markerOptions.position(marker.getPosition());

                    // Setting titile of the infowindow of the marker
                    markerOptions.title("Position");

                    // Setting the content of the infowindow of the marker
                    markerOptions.snippet("Latitude:" + marker.getPosition().latitude + "," + "Longitude:" + marker.getPosition().longitude);
                    // Adding the marker to the map
                    Marker markerS = mGoogleMap.addMarker(markerOptions);
                    Polyline poly;
                    if (markers.isEmpty()) {// si c'est le premeir point on trace une ligne depuis le drone
                        LastLatLng = new LatLng(droneposition.position[0], droneposition.position[1]);
                        poly = mGoogleMap.addPolyline((new PolylineOptions())
                                .add(LastLatLng, LastLatLng).width(6).color(Color.RED)
                                .visible(true));
                    }
                    else {
                        LastLatLng = markers.get(markers.size() - 1).getPosition();
                        poly = mGoogleMap.addPolyline((new PolylineOptions())
                                .add(LastLatLng, marker.getPosition()).width(6).color(Color.RED)
                                .visible(true));
                    }


                    polylineList.add(poly);

                    markers.add(markerS);

                    Double[] tab = new Double[2];
                    tab[0] = marker.getPosition().latitude;
                    tab[1] = marker.getPosition().longitude;
                    pointsSegment.add(tab);

                    if (boucleSegment) {//si on demande a boucler
                        if (boucleSegmentPoly != null)
                            boucleSegmentPoly.remove();
                        boucleSegmentPoly = mGoogleMap.addPolyline((new PolylineOptions())
                                .add(markers.get(markers.size() - 1).getPosition(), markers.get(0).getPosition()).width(6).color(Color.RED)
                                .visible(true));
                    }


                } else {
                    //on click sur la carte dans le vide
                    changerMenu(ListeMenu.m_menu_choix);
                    if (markerChanged != null) {
                        markerChanged.remove();
                        Log.e("MapActivity", "markerChanged.remove(); line 667");
                    }
                    //on click sur le marqueur d'intervention
                    if (marker.getTitle() == null || marker.getTitle().equals("Intervention")) {
                        //do nothing but catch not a number marker
//                    SynchroniserIntervention();
//                    changerMenu(ListeMenu.aucun);
                    }
                    //les vehicules on un ii entre 0 et 999
                    else if (Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) < 1000) {
                        changerMenu(ListeMenu.m_menu_Actionvehicule);
                        System.out.println("veh" + marker.getTitle());
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
                        System.out.println("point selected " + pointSelected);
                    }
                    // les drones
                    else if (Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) >= 2000 && Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) < 3000) {
                        changerMenu(ListeMenu.m_menu_Actiondrone);
                    }
                    // les points SP
                    else if (Integer.parseInt(marker.getTitle()) != -1 && Integer.parseInt(marker.getTitle()) >= 3000) {
                        changerMenu(ListeMenu.aucun);
                        if (markerChanged != null) {
                            markerChanged.remove();
                            Log.e("MapActivity", "markerChanged.remove(); line 705");
                        }
                    } else {
                        SynchroniserIntervention();
                        changerMenu(ListeMenu.aucun);

                    }
                }
                return false;

            }
        });

        //#########################    QUAND ON CLICK SUR LA CARTE GOOGLE MAP
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {


                Log.e("Map", "Map clicked");

if (clickedZone || clickedZoneExclusion)
{
                    markerPoints.add(point);

                    if (markerPoints.size() == 1) {

                        markerStart = mGoogleMap.addMarker(new MarkerOptions()
                                .position(point)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerstart))
                                .title("start shape"));

                    }
                    if (markerPoints.size() > 1) {
                        System.out.println("dessiner une zone");
                        int i = markerPoints.size() - 2;


                        if(clickedZoneExclusion) {
                            Polyline polyline = mGoogleMap.addPolyline((new PolylineOptions())
                                    .add(markerPoints.get(i), markerPoints.get(i + 1)).width(6).color(Color.RED)
                                    .visible(true));

                            polylinesZone.add(polyline);

                        }
                        else  {
                            Polyline polyline = mGoogleMap.addPolyline((new PolylineOptions())
                                    .add(markerPoints.get(i), markerPoints.get(i + 1)).width(6).color(Color.BLUE)                                    .visible(true));

                            polylinesZone.add(polyline);

                        }


                    }
                }
            /*if(clickedSegment || clickedZone || clickedZoneExclusion){*/
                else if (clickedSegment ) {
                    //Yousra

                    System.out.println("dessiner une ligne");
                    if (markers.isEmpty())// si c'est le premeir point on trace une ligne depuis le drone
                        LastLatLng = new LatLng(droneposition.position[0], droneposition.position[1]);
                    else
                        LastLatLng = markers.get(markers.size() - 1).getPosition();

                    Polyline poly;
                    if (markers.isEmpty()) {// si c'est le premeir point on trace une ligne depuis le drone
                        LastLatLng = new LatLng(droneposition.position[0], droneposition.position[1]);
                        poly = mGoogleMap.addPolyline((new PolylineOptions())
                                .add(LastLatLng, LastLatLng).width(6).color(Color.RED)
                                .visible(true));
                    }
                    else {
                        LastLatLng = markers.get(markers.size() - 1).getPosition();
                        poly = mGoogleMap.addPolyline((new PolylineOptions())
                                .add(LastLatLng, point).width(6).color(Color.RED)
                                .visible(true));
                    }
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Setting latitude and longitude of the marker position
                    markerOptions.position(point);

                    // Setting titile of the infowindow of the marker
                    markerOptions.title("Position");

                    // Setting the content of the infowindow of the marker
                    markerOptions.snippet("Latitude:" + point.latitude + "," + "Longitude:" + point.longitude);
                    // Adding the marker to the map
                    Marker marker = mGoogleMap.addMarker(markerOptions);

                    markers.add(marker);
                    polylineList.add(poly);

                    Double[] tab = new Double[2];
                    tab[0] = point.latitude;
                    tab[1] = point.longitude;
                    pointsSegment.add(tab);
                    // drone.getSegment().getPoints().add(tab);

                    if (boucleSegment) {//si on demande a boucler
                        if (boucleSegmentPoly != null)
                            boucleSegmentPoly.remove();
                        boucleSegmentPoly = mGoogleMap.addPolyline((new PolylineOptions())
                                .add(markers.get(markers.size() - 1).getPosition(), markers.get(0).getPosition()).width(6).color(Color.RED)
                                .visible(true));
                    }

                } else {
                    pointVehicule = point;
                    if (clickedPoint) {
                        Double nPos[] = new Double[2];
                        nPos[0] = point.latitude;
                        nPos[1] = point.longitude;
                        intervention.points.get(pointSelected).setPosition(nPos);

                        InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.d("MapActivity", "Point déplacé");
                                SynchroniserIntervention();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                //DO NOTHING
                                Log.e("MapActivity", t.toString());
                            }
                        });
                        changerMenu(ListeMenu.aucun);

                    } else if (clicked) {//on a clique sur un vehicule et sur déplacer juste avant
                        Double[] position = new Double[2];
                        position[0] = pointVehicule.latitude;
                        position[1] = pointVehicule.longitude;

                        for (int i = 0; i < intervention.vehicules.size(); i++) {
                            Vehicule vehiculeCourant = intervention.vehicules.get(i);

                            if (vehiculeCourant.equals(vehiculeselected)) {

                                boolean modifEffectuee = vehiculeCourant.engager(position);

                                if (modifEffectuee) {
                                    InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            Log.d("MapActivity", "Véhicule déplacé");
                                            SynchroniserIntervention();
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            //DO NOTHING
                                            Log.e("MapActivity", t.toString());
                                        }
                                    });
                                    changerMenu(ListeMenu.aucun);
                                } else {
                                    Toast.makeText(getActivity(),
                                            "Impossible de déplacer ce véhicule", Toast.LENGTH_LONG).show();
                                }
                                clicked = false;
                            }
                        }

                    } else if (secondClickSurMap) {
                        changerMenu(ListeMenu.aucun);
                        secondClickSurMap = false;
                        markerChanged.remove();
                        Log.e("MapActivity", "markerChanged.remove(); line 797");
                    } else {



               /* myMarker.remove();*/
                        if (markerChanged != null) {
                            markerChanged.remove();
                            Log.e("MapActivity", "markerChanged.remove(); line 807");
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
            }
        });


    }

    private void reloadDroneEtat() {
        DroneServiceImpl.getInstance().getDroneByIdIntervention(idIntervention, new Callback<Drone>() {
            @Override
            public void onResponse(Call<Drone> call, Response<Drone> response) {
                drone = response.body();
                if(drone == null)
                    drone = new Drone(idIntervention,EtatDrone.STOP);
                if(drone.etat == null)
                    drone.etat = EtatDrone.STOP;

                Log.e("Drone retreived", String.valueOf(response.body()));

                //Salma
                SynchroniserIntervention();
                Log.e("MapreloadDroneEtat","etat: "+drone.etat);
                m_menu_Actiondrone_segment_b.setEnabled(false);
                m_menu_Actiondrone_zone_b.setEnabled(false);
                m_menu_Actiondrone_exclusion.setEnabled(true);
                m_menu_Actiondrone_parking.setEnabled(false);
                m_menu_Actiondrone_stop.setEnabled(true);

                switch(drone.etat){
                    case STOP:
                        m_menu_Actiondrone_segment_b.setEnabled(true);
                        m_menu_Actiondrone_zone_b.setEnabled(true);
                        m_menu_Actiondrone_parking.setEnabled(true);
                        m_menu_Actiondrone_exclusion.setEnabled(true);
                        break;
                    case SEGMENT:
                        m_menu_Actiondrone_stop.setEnabled(true);
                        break;
                    case ZONE:
                        m_menu_Actiondrone_stop.setEnabled(true);
                        break;
                }
            }

            @Override
            public void onFailure(Call<Drone> call, Throwable t) {
                Log.e("Drone not created", "");
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
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, iconSizeX / 2, iconSizeX / 2, false);



                       /* String mDrawableName = pointInteret.getCode_image().toLowerCase();
                        int resID = getResources().getIdentifier(mDrawableName, "drawable", getContext().getPackageName());*/


            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(point.getPosition()[0], point.getPosition()[1]))

                               /* .icon(BitmapDescriptorFactory.fromResource(resID))*/
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .title("" + positionDansListePoints)
                    .snippet("" + mDrawableName));
        }
    }

    /**
     * Méthode qui synchronise l'intervention et appel le rechargement des vehicules et points si l'utilisateur n'effectue pas d'intéraction.
     */
    public void SynchroniserIntervention() {
        //TODO avec idIntervention


        if (mGoogleMap != null && !reloadingIntervention) {
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

            myMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(lng)
                    .title("Intervention")
                    .snippet(intervention.libelle));
            //ajouts des vehicules
            if (filtreMoyens == false) {
                for (int i = 0; i < vehiculesCarte.size(); i++) {
                    Vehicule vehiculeCourant = vehiculesCarte.get(i);
                    if (
                            (vehiculeCourant.etat == EtatVehicule.DEMANDE || vehiculeCourant.etat == EtatVehicule.ENGAGE || vehiculeCourant.etat == EtatVehicule.ARRIVE)
                                    && (vehiculeCourant.position != null && vehiculeCourant.position[0] != null && vehiculeCourant.position[1] != null)) {
                        ajoutImageFromVehicule(vehiculesCarte.get(i), i);
                    }
                }
            }
            if (filtrePoints == false) {
                //ajouts des points
                if (pointsCarte != null) {
                    for (int i = 0; i < pointsCarte.size(); i++) {
                        PointInteret pointCourant = pointsCarte.get(i);
                        ajoutImageFromPoint(pointCourant, 1000 + i);
                    }
                }
            }
            if (filtrePointsSP == false) {
                //ajouts des points SP
                if (pointsSPCarte != null) {
                    for (int i = 0; i < pointsSPCarte.size(); i++) {
                        PointInteret pointCourant = pointsSPCarte.get(i);
                        ajoutImageFromPoint(pointCourant, 3000 + i);

                    }
                }
            }
            if (filtreSegment == false) {
                if(drone != null && drone.etat == EtatDrone.SEGMENT)
                        dessinerSegment();
            }
            if (filtreZone == false) {
                if(drone != null &&drone.etat == EtatDrone.ZONE)
                    dessinerZone();
        }
        }catch (Exception e) {

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
        m_menu_Actiondrone_zone.setVisibility(View.GONE);
        m_menu_Actiondrone_segment.setVisibility(View.GONE);

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
            case m_menu_Actiondrone: {
                m_menu_Actiondrone.setVisibility(View.VISIBLE);

            }

            break;
            case m_menu_Actiondrone_segment:
                m_menu_Actiondrone_segment.setVisibility(View.VISIBLE);
                break;
            case m_menu_Actiondrone_zone:
                m_menu_Actiondrone_zone.setVisibility(View.VISIBLE);
                break;
            case aucun:
                secondClickSurMap = false;
                if (markerChanged != null) {
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

    private void reloadDrone() {
        //TODO: get la nouvelle position dans @dronePosition
//        if(droneMarker == null){


        if (droneposition != null && droneposition.position != null && droneposition.position[0] != null && droneposition.position[1] != null) {
            Log.e("MapActivity", "création du drone" + droneposition);
            if (droneMarker != null)
                droneMarker.remove();
            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(getResources().getIdentifier("drone", "drawable", getContext().getPackageName()));
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, iconSizeX, iconSizeX, false);


            mGoogleMap.stopAnimation();
            droneMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(droneposition.position[0], droneposition.position[1]))
                    .anchor(0.5f,0.5f)
                    .title("" + 2000)
                    .snippet("SuperDrone le sauveur des Petits chats")
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            );
//            droneMarker.setVisible(true);
//            droneMarker.showInfoWindow();
            //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(droneposition.position[0],droneposition.position[1]), 18));
        } else {
            Log.e("MapActivity", "Pas de dronePosition");
        }

//        }
//        else{
//            Log.e("MapActivity","move drone to position "+droneposition.position[0]+"  "+droneposition.position[1]);
//            droneMarker.setPosition(new LatLng(droneposition.position[0],droneposition.position[1]));
//        }
    }

    private void dessinerZone() {
        if (drone != null && drone.zone != null) {

            if (drone.zone.getContours().size() > 0) {
                System.out.println("===> zone " + drone.zone.getContours());


                ArrayList<LatLng> contourZone = new ArrayList<>();


                for (Double[] tab : drone.zone.getContours()) {
                    LatLng latLng = new LatLng(tab[0], tab[1]);

                    contourZone.add(latLng);


                }


                //Salma
                System.out.println("dessiner polygon");
                PolygonOptions zoneOptions = new PolygonOptions();
                for (int i = 0; i < contourZone.size(); i++) {
                    zoneOptions.add(contourZone.get(i));
                }
                zoneOptions.add(contourZone.get(0));
                zoneOptions.strokeColor(Color.BLUE);


                zoneOptions.fillColor(R.color.TransparentBlue);

// Get back the mutable Polygon
                polygonZone = mGoogleMap.addPolygon(zoneOptions);

            }

         if(drone.zone.getExclusion() !=null) {
             if (drone.zone.getExclusion().size() > 0) {




                 for (List<Double[]> list : drone.zone.getExclusion()) {
                     ArrayList<LatLng> contourZoneExculsion = new ArrayList<>();
                     for (Double[] tab : list) {

                         LatLng latLng = new LatLng(tab[0], tab[1]);

                         contourZoneExculsion.add(latLng);
                     }

                         System.out.println("dessiner polygon");
                         PolygonOptions zoneOptions = new PolygonOptions();
                         for (int i = 0; i < contourZoneExculsion.size(); i++) {
                             zoneOptions.add(contourZoneExculsion.get(i));
                         }
                         zoneOptions.add(contourZoneExculsion.get(0));
                         zoneOptions.strokeColor(Color.RED);


                         zoneOptions.fillColor(R.color.TransparentRed);

// Get back the mutable Polygon
                         polygonZone = mGoogleMap.addPolygon(zoneOptions);


                 }
             }
         }


        }
    }

    private void dessinerSegment() {
        Log.e("Map","dessin des segments existants");
        if (drone.segment != null && drone.segment.getPoints() != null) {
            for (int indexPoint = 0; indexPoint < drone.segment.getPoints().size() && drone.segment.getPoints().size() > 1; indexPoint++) {
                if (indexPoint == 0)
                    LastLatLng = new LatLng(drone.segment.getPoints().get(indexPoint)[0], drone.segment.getPoints().get(indexPoint)[1]);
                else {
                    LatLng newLatLng = new LatLng(drone.segment.getPoints().get(indexPoint)[0], drone.segment.getPoints().get(indexPoint)[1]);
                    mGoogleMap.addPolyline((new PolylineOptions())
                            .add(LastLatLng, newLatLng)
                            .width(6)
                            .color(Color.RED)
                            .visible(true));
                    LastLatLng = newLatLng;
                }
            }
            if (drone.segment.getBoucleFermee()) {
                if (drone.segment.getPoints().size() > 0)
                    mGoogleMap.addPolyline((new PolylineOptions())
                            .add(
                                    new LatLng(drone.segment.getPoints().get(0)[0], drone.segment.getPoints().get(0)[1]),
                                    new LatLng(drone.segment.getPoints().get(drone.segment.getPoints().size() - 1)[0], drone.segment.getPoints().get(drone.segment.getPoints().size() - 1)[1])
                            )
                            .width(6)
                            .color(Color.RED)
                            .visible(true));

            }
        }
    }

//    private void effacerSegment&zone(ArrayList<LatLng> points) // ne pas faire, juste faire un synchroniser

}

