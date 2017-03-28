package istic.fr.droneproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import istic.fr.droneproject.adapter.ValidationRecyclerAdapter;
import istic.fr.droneproject.model.EtatVehicule;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Validation;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.InterventionService;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nirina on 27/03/17.
 */

public class CodisValidationFragment extends Fragment {

    public CodisValidationFragment(){

    }

    public static CodisValidationFragment newInstance(){
        CodisValidationFragment fragment = new CodisValidationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedStateInstance){
        View validationView = inflater.inflate(R.layout.codis_validation, container, false);

        final RecyclerView validationsRecycler = (RecyclerView) validationView.findViewById(R.id.codis_list_validation);
        validationsRecycler.setLayoutManager(new LinearLayoutManager(validationView.getContext()));

        final List<Intervention> interventions = new ArrayList<>();
        final List<Validation> validations = new ArrayList<>();
        ValidationRecyclerAdapter.ValidClickListener validListener = new ValidationRecyclerAdapter.ValidClickListener() {
            @Override
            public void clickValidation(Validation validation) {
                if (validation.vehicule.position == null){
                    validation.vehicule.etat = EtatVehicule.PARKING;
                }else{
                    validation.vehicule.etat = EtatVehicule.ENGAGE;
                }
                validations.remove(validation);
                InterventionService service = InterventionServiceCentral.getInstance();
                service.updateIntervention(validation.intervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("TestCodisValidationActi","modification OK");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TestCodisValidationActi","modification KO");
                    }
                });

                /*int count = 0;
                for (Intervention i:interventions){
                    for (Vehicule v:i.vehicules){
                        if (v.etat == EtatVehicule.DEMANDE){
                            count += 1;
                        }
                    }
                }
                Log.e("TestCodisValidationActi","Nombre de véhicules à valider : "+count);*/

            }
        };
        ValidationRecyclerAdapter.RefusClickListener refusListener = new ValidationRecyclerAdapter.RefusClickListener() {
            @Override
            public void clickRefus(Validation validation) {
                validation.vehicule.etat = EtatVehicule.ANNULE;
                validations.remove(validation);
                InterventionService service = InterventionServiceCentral.getInstance();
                service.updateIntervention(validation.intervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.e("TestCodisValidationActi","modification OK");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TestCodisValidationActi","modification KO");
                    }
                });
                //TODO modification dans la liste
                //service.modifyIntervention(validation.intervention)
                /*int count = 0;
                for (Intervention i:interventions){
                    for (Vehicule v:i.vehicules){
                        if (v.etat == EtatVehicule.DEMANDE){
                            count += 1;
                        }
                    }
                }
                Log.e("TestCodisValidationActi","Nombre de véhicules à valider : "+count);*/

            }
        };

        final ValidationRecyclerAdapter validationArrayAdapter = new ValidationRecyclerAdapter(validations,R.layout.codis_validation_item,refusListener,validListener);
        validationsRecycler.setAdapter(validationArrayAdapter);

        InterventionService service = InterventionServiceCentral.getInstance();
        service.getListeInterventions(new Callback<List<Intervention>>() {
            @Override
            public void onResponse(Call<List<Intervention>> call, Response<List<Intervention>> response) {
                interventions.clear();
                interventions.addAll(response.body());
                validations.clear();
                for (Intervention i:interventions){
                    if (i.vehicules!= null) {
                        for (Vehicule v : i.vehicules) {
                            if (v.etat == EtatVehicule.DEMANDE) {
                                validations.add(new Validation(v, i));
                            }
                        }
                    }
                }
                Log.e("CodisValidationActivi","Nombre de véhicules à valider : "+validations.size());
                validationArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Intervention>> call, Throwable t) {
                //DO NOTHING
                Log.e("CodisValidationActivi", t.toString());
            }
        });

        return validationView;
    }
}
