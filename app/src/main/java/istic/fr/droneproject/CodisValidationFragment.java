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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    List<Validation> validations;
    ValidationRecyclerAdapter adapter;
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

        validations = new ArrayList<>();
        ValidationRecyclerAdapter.ValidClickListener validListener = new ValidationRecyclerAdapter.ValidClickListener() {
            @Override
            public void clickValidation(Validation validation) {
                if (validation.vehicule.position == null){
                    validation.vehicule.etat = EtatVehicule.PARKING;
                }else{
                    validation.vehicule.etat = EtatVehicule.ENGAGE;
                }

                SimpleDateFormat formatter = new SimpleDateFormat("HH/mm");
                validation.vehicule.heureEngagement = formatter.format(new Date());

                InterventionService service = InterventionServiceCentral.getInstance();
                service.updateIntervention(validation.intervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        chargerValidations();
                        Log.e("TestCodisValidationActi","modification OK");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TestCodisValidationActi","modification KO");
                    }
                });
            }
        };
        ValidationRecyclerAdapter.RefusClickListener refusListener = new ValidationRecyclerAdapter.RefusClickListener() {
            @Override
            public void clickRefus(Validation validation) {
                validation.vehicule.etat = EtatVehicule.ANNULE;
                InterventionService service = InterventionServiceCentral.getInstance();
                service.updateIntervention(validation.intervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        chargerValidations();
                        Log.e("TestCodisValidationActi","modification OK");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TestCodisValidationActi","modification KO");
                    }
                });
            }
        };

        //final ValidationRecyclerAdapter adapter = new ValidationRecyclerAdapter(validations,R.layout.codis_validation_item,refusListener,validListener);
        adapter = new ValidationRecyclerAdapter(validations,R.layout.codis_validation_item,refusListener,validListener);
        validationsRecycler.setAdapter(adapter);

        chargerValidations();
        //adapter.notifyDataSetChanged();
        return validationView;
    }

    public void chargerValidations(){
        final List<Intervention> interventions = new ArrayList<>();
        InterventionService service = InterventionServiceCentral.getInstance();
        service.getListeInterventions(new Callback<List<Intervention>>() {
            @Override
            public void onResponse(Call<List<Intervention>> call, Response<List<Intervention>> response) {
                interventions.clear();
                interventions.addAll(response.body());
                validations.clear();

                //Integer count = 0;

                for (Intervention i:interventions){
                    if (i.vehicules!= null) {
                        for (Vehicule v : i.vehicules) {
                            if (v.etat == EtatVehicule.DEMANDE) {
                                /*count+=1;
                                Log.e("Check objet",count.toString());
                                Log.e("Check objet",v.etat.toString());
                                Log.e("Check objet",v.heureDemande);
                                Log.e("Check objet",v.nom);
                                Log.e("Check objet",i.libelle);*/
                                validations.add(new Validation(v, i));
                            }
                        }
                    }
                }
                Log.e("CodisValidationActivi","Nombre de véhicules à valider : "+validations.size());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Intervention>> call, Throwable t) {
                //DO NOTHING
                Log.e("CodisValidationActivi", t.toString());
            }
        });
    }
}
