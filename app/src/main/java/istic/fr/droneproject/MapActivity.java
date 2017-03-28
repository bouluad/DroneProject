package istic.fr.droneproject;

import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import istic.fr.droneproject.adapter.AlbumPhotoAdapter;
import istic.fr.droneproject.adapter.MapVehiculesRecyclerAdapter;
import istic.fr.droneproject.model.Categorie;
import istic.fr.droneproject.model.EtatVehicule;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Photo;
import istic.fr.droneproject.model.TypeVehicule;
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
    Vehicule vehicule;
    List<Vehicule> vehicules;

    View m_menu_vehicules;
    View m_menu_points;
    View m_menu_choix;
    private static final String ARG_ID = "idIntervention";
    private String idIntervention;
    String[] categorie = {"SAUVETAGE", "INCENDIE", "RISQUE PARTICULIER", "EAU", "COMMANDEMENT"};


    @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_map, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_menu_choix = (LinearLayout) view.findViewById(R.id.m_menu_choix);
        m_menu_points = (LinearLayout) view.findViewById(R.id.m_menu_points);
        m_menu_vehicules = (LinearLayout) view.findViewById(R.id.m_menu_vehicules);

        Button points = (Button) view.findViewById(R.id.m_menu_choix_points);
        Button vehicule=(Button)  view.findViewById(R.id.m_menu_choix_vehicules);
       /* final List<Vehicule> vehicules = new ArrayList<>();*/
        vehicules = new ArrayList<>();
        recyclerViewVehicules= (RecyclerView) view.findViewById(R.id.m_list_vehicules);
        recyclerViewVehicules.setLayoutManager(new LinearLayoutManager(getContext()));
        vehiculesAdapter=new MapVehiculesRecyclerAdapter(vehicules,R.layout.m_vehicules_item);
        recyclerViewVehicules.setAdapter(vehiculesAdapter);

        InterventionServiceCentral.getInstance().getInterventionById(idIntervention,new Callback<Intervention>() {
            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                //Log.e("Cateeeegoriiiie======",response.body().vehicules.get(0).categorie.toString());
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
                System.out.println("ici");
                final FloatingActionButton btn_add_moyen = (FloatingActionButton) m_menu_vehicules.findViewById(R.id.m_list_vehicules_add);
                btn_add_moyen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showSimplePopUp();
                    }
                });







                //findViewById(R.id.m_list_vehicules).setVisibility(View.VISIBLE);
            }
        });



        map = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.m_map);
        map.getMapAsync(this);


    }
    private void showSimplePopUp() {


        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this.getActivity());
        helpBuilder.setTitle("Moyens 1er depart");
        /*helpBuilder.setMessage("This is a Simple Pop Up");*/

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View popupLayout = inflater.inflate(R.layout.codis_add_moyen_popup, null);




        final EditText nom_vehicule = (EditText)popupLayout.findViewById(R.id.nom_moyen);
        final Spinner popupSpinner = (Spinner)popupLayout.findViewById(R.id.spinnerCategorie);
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, categorie);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        popupSpinner.setAdapter(adapter);




        final RadioGroup radiogroup =  (RadioGroup) popupLayout.findViewById(R.id.type_radio);














        helpBuilder.setView(popupLayout);



        helpBuilder.setPositiveButton("Ajouter",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        //add to list vehicule

                        vehicule= new Vehicule();


                        vehicule.nom=  nom_vehicule.getText().toString();
                        int selectedId = radiogroup.getCheckedRadioButtonId();

                        final RadioButton radioButton = (RadioButton) popupLayout.findViewById(selectedId);

                        String radio_value = radioButton.getText().toString();
                        switch(radio_value) {
                            case "FPT":

                                vehicule.type=TypeVehicule.FPT;
                                break;
                            case "VLCG":

                                vehicule.type=TypeVehicule.VLCG;
                                break;
                            case "VSAV":

                                vehicule.type=TypeVehicule.VSAV;
                                break;
                        }

                        String selectedSpinner = popupSpinner.getSelectedItem().toString();
                        switch(selectedSpinner) {

                            case "SAUVETAGE":

                                vehicule.categorie=Categorie.SAUVETAGE;
                                break;
                            case "INCENDIE":

                                vehicule.categorie=Categorie.INCENDIE;
                                break;
                            case "RISQUE_PARTICULIER":

                                vehicule.categorie=Categorie.RISQUE_PARTICULIER;
                                break;
                            case "EAU":

                                vehicule.categorie=Categorie.EAU;
                                break;
                            case "COMMANDEMENT":

                                vehicule.categorie=Categorie.COMMANDEMENT;
                                break;
                        }

                        String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
                        vehicule.heureDemande=currentTime;

                        vehicule.etat= EtatVehicule.DEMANDE;


                        InterventionServiceCentral.getInstance().getInterventionById(idIntervention,new Callback<Intervention>() {
                            @Override
                            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                                //Log.e("Cateeeegoriiiie======",response.body().vehicules.get(0).categorie.toString());
                                Collections.reverse(response.body().vehicules);
                                vehicules.clear();
                                vehicules.addAll(response.body().vehicules);
                                vehicules.add(vehicule);
                                vehiculesAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<Intervention> call, Throwable t) {
                                //DO NOTHING
                                Log.e("MapActivity", t.toString());
                            }
                        });









                        // Set the ArrayAdapter as the ListView's adapter.


                        /*Toast.makeText(getApplicationContext(), "Véhicule enregistré", Toast.LENGTH_SHORT).show();*/


                    }
                });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();


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






}
