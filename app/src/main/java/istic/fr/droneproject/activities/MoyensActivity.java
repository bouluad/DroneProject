package istic.fr.droneproject.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import istic.fr.droneproject.R;
import istic.fr.droneproject.adapter.TableauMoyenRecyclerAdapter;
import istic.fr.droneproject.model.Categorie;
import istic.fr.droneproject.model.EtatVehicule;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.TypeVehicule;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bouluad on 22/03/17.
 */
public class MoyensActivity extends android.support.v4.app.Fragment {

    private Intervention currentIntervention;
    private static final String ARG_ID = "idIntervention";
    private String idIntervention;
    private TableauMoyenRecyclerAdapter vehiculeArrayAdapter;
    private List<Vehicule> vehicules;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_tableau_moyen, container, false);
    }

    public MoyensActivity() {
        //Required empty constructor
    }

    public static MoyensActivity newInstance(String idIntervention) {
        MoyensActivity fragment = new MoyensActivity();
        Bundle args = new Bundle();
        args.putString(ARG_ID, idIntervention);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idIntervention = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FloatingActionButton btnAdd = (FloatingActionButton) view.findViewById(R.id.utm_btn_add);
        final RecyclerView vehiculesRecycler = (RecyclerView) view.findViewById(R.id.utm_list_vehicules);

        vehiculesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        vehicules = new ArrayList<>();
        //Un listener sur les evenement dans le tableau des moyens
        //Est appeler à chaque click sur confirmer ou liberer
        TableauMoyenRecyclerAdapter.EventsVehiculeClickListener eventsVehiculeClickListener = new TableauMoyenRecyclerAdapter.EventsVehiculeClickListener(){
            @Override
            public void clickConfirmer(Vehicule vehicule) {
                if ((EtatVehicule.ENGAGE.equals(vehicule.etat) && (vehicule.heureEngagement != null) ) || (EtatVehicule.PARKING.equals(vehicule.etat)) ) {
                    if (vehicule.position == null || vehicule.position[0] == null || vehicule.position[1] == null) {
                        vehicule.etat = EtatVehicule.PARKING;
                    } else {
                        vehicule.etat = EtatVehicule.ARRIVE;
                    }
                    vehicule.heureArrivee = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
                    InterventionServiceCentral.getInstance().updateIntervention(currentIntervention, new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.e("MoyensActivity","UPDATE INTERVENTION");
                            chargerIntervention();
                            Toast.makeText(getActivity(),
                                    "Vehicule confirmé", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                        }
                    });
                }else {

                    Toast.makeText(getActivity(),
                       "Veuillez attendre l'arrivée avant de confirmer", Toast.LENGTH_LONG).show();
            }
            }
            @Override
            public void clickLiberer(Vehicule vehicule) {

                    vehicule.etat = EtatVehicule.LIBERE;
                    vehicule.heureLiberation = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
                    InterventionServiceCentral.getInstance().updateIntervention(currentIntervention, new Callback<Void>() {

                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.e("MoyensActivity", "UPDATE INTERVENTION");
                            chargerIntervention();
                            Toast.makeText(getActivity(),
                                    "Vehicule liberé", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                        }
                    });


            }
        };
        vehiculeArrayAdapter = new TableauMoyenRecyclerAdapter(vehicules, R.layout.utm_vehicule_item, currentIntervention,eventsVehiculeClickListener);
        vehiculesRecycler.setAdapter(vehiculeArrayAdapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimplePopUp(savedInstanceState);
            }
        });
        chargerIntervention();
    }
    public void chargerIntervention(){
        InterventionServiceCentral.getInstance().getInterventionById(idIntervention, new Callback<Intervention>() {
            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                currentIntervention = response.body();
                vehicules.clear();
                if (currentIntervention != null) {
                    vehicules.addAll(currentIntervention.vehicules);
                }
                vehiculeArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Intervention> call, Throwable t) {
                Log.e("UTM", t.toString());
                //DO NOTHING
            }
        });
    }

    /**
     *
     * @param savedInstanceState
     * @return Vehicule avec nom, categorie, type
     */
    private void showSimplePopUp(Bundle savedInstanceState) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getContext());
        helpBuilder.setTitle("Demander un véhicule");

        LayoutInflater inflater = getLayoutInflater(savedInstanceState);
        final View popupLayout = inflater.inflate(R.layout.codis_add_moyen_popup, null);

        final EditText nom_vehicule = (EditText) popupLayout.findViewById(R.id.nom_moyen);

        final Spinner categorieSpinner = (Spinner) popupLayout.findViewById(R.id.spinnerCategorie);
        Categorie[] categories = Categorie.values();
        ArrayAdapter<Categorie> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorieSpinner.setAdapter(adapter);

        final RadioGroup radiogroup = (RadioGroup) popupLayout.findViewById(R.id.type_radio);

        helpBuilder.setView(popupLayout);
        helpBuilder.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Vehicule vehicule = new Vehicule();
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
                vehicule.categorie = (Categorie) categorieSpinner.getSelectedItem();

                vehicule.etat = EtatVehicule.DEMANDE;
                vehicule.heureDemande = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
                currentIntervention.vehicules.add(vehicule);
                InterventionServiceCentral.getInstance().updateIntervention(currentIntervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("MoyensActivity", "UPDATE INTERVENTION");
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


}
