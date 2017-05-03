package istic.fr.droneproject.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.activities.CodisNewInterventionActivity;
import istic.fr.droneproject.adapter.InterventionRecyclerAdapter;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nirina on 24/03/17.
 */

public class CodisInterventionsFragment extends android.support.v4.app.Fragment{
    private FloatingActionButton add;
    private List<Intervention> interventions;
    private InterventionRecyclerAdapter interventionArrayAdapter;


    public CodisInterventionsFragment(){

    }

    public static CodisInterventionsFragment newInstance(){
        CodisInterventionsFragment fragment = new CodisInterventionsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //setContentView(R.layout.codis_interventions);
        View listInterventionsView = inflater.inflate(R.layout.codis_interventions, container, false);
        add = (FloatingActionButton) listInterventionsView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CodisNewInterventionActivity.class);
                startActivity(intent);
            }
        });

        final RecyclerView interventionsRecycler = (RecyclerView) listInterventionsView.findViewById(R.id.codis_list_interventions);


        interventionsRecycler.setLayoutManager(new LinearLayoutManager(listInterventionsView.getContext()));


        interventions = new ArrayList<>();
        InterventionRecyclerAdapter.InterventionClickListener interventionClickListener = new InterventionRecyclerAdapter.InterventionClickListener() {
            @Override
            public void clickIntervention(Intervention intervention) {
                showSimplePopUp(intervention);

            }
        };
        interventionArrayAdapter = new InterventionRecyclerAdapter(interventions, R.layout.ui_intervention_item, interventionClickListener);
        interventionsRecycler.setAdapter(interventionArrayAdapter);

        recupererListeInterventions();
        return listInterventionsView;
    }

    @Override
    public void onResume() {
        super.onResume();
        recupererListeInterventions();
    }

    private void recupererListeInterventions() {
        InterventionServiceCentral.getInstance().getListeInterventions(new Callback<List<Intervention>>() {
            @Override
            public void onResponse(Call<List<Intervention>> call, Response<List<Intervention>> response) {
                Collections.reverse(response.body());

                List<Intervention> triee = new ArrayList<>();
                List<Intervention> cloturees = new ArrayList<>();

                for (Intervention intervention : response.body()) {
                    if(intervention.cloturer){
                        cloturees.add(intervention);
                    }else{
                        triee.add(intervention);
                    }
                }

                triee.addAll(cloturees);

                interventions.clear();
                interventions.addAll(triee);
                interventionArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Intervention>> call, Throwable t) {
                //DO NOTHING
                Log.e("UserInterventionsActivi", t.toString());
                Toast.makeText(getContext(), "Erreur de récupération des interventions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSimplePopUp(final Intervention intervention) {
        if(!intervention.cloturer) {
            AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());
            helpBuilder.setTitle("Clôturer");
            helpBuilder.setMessage("Voulez-vous clôturer cette intervention ?\n Date: " + intervention.date + "\n Adresse: " + intervention.adresse);

            helpBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            intervention.cloturer = true;
                            InterventionServiceCentral.getInstance().updateIntervention(intervention, new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(getContext(), "Intervention clôturée", Toast.LENGTH_SHORT).show();
                                    recupererListeInterventions();
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(getContext(), "Erreur d'envoi de la clôture", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
            helpBuilder.setNegativeButton("Annuler",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

            // Remember, create doesn't show the dialog
            AlertDialog helpDialog = helpBuilder.create();
            helpDialog.show();
        }else{
            Toast.makeText(getContext(),"L'intervention est déjà clôturée.",Toast.LENGTH_SHORT).show();
        }
    }
}
