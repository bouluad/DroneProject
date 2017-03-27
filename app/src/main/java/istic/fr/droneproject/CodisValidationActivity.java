package istic.fr.droneproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
 * Created by nirina on 24/03/17.
 */

public class CodisValidationActivity extends AppCompatActivity{

     @Override
    protected void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         setContentView(R.layout.codis_validation);

         final RecyclerView validationsRecycler = (RecyclerView) findViewById(R.id.codis_list_validation);
         validationsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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
                 //TODO modification dans la liste
                 //service.modifyIntervention(validation.intervention)
                 int count = 0;
                 for (Intervention i:interventions){
                     for (Vehicule v:i.vehicules){
                         if (v.etat == EtatVehicule.DEMANDE){
                             count += 1;
                         }
                     }
                 }
                 Log.e("TestCodisValidationActi","Nombre de véhicules à valider : "+count);

             }
         };
         ValidationRecyclerAdapter.RefusClickListener refusListener = new ValidationRecyclerAdapter.RefusClickListener() {
             @Override
             public void clickRefus(Validation validation) {
                 validation.vehicule.etat = EtatVehicule.ANNULE;
                 validations.remove(validation);
                 InterventionService service = InterventionServiceCentral.getInstance();
                 //TODO modification dans la liste
                 //service.modifyIntervention(validation.intervention)
                 int count = 0;
                 for (Intervention i:interventions){
                     for (Vehicule v:i.vehicules){
                         if (v.etat == EtatVehicule.DEMANDE){
                             count += 1;
                         }
                     }
                 }
                 Log.e("TestCodisValidationActi","Nombre de véhicules à valider : "+count);

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
                 //Log.e("CodisValidationActivi","Nombre total d'interventions : "+interventions.size());
                 //interventionArrayAdapter.notifyDataSetChanged();
                 validations.clear();
                 int countV = 0;
                 for (Intervention i:interventions){
                     for (Vehicule v:i.vehicules){
                         if (v.etat == EtatVehicule.DEMANDE){
                             countV += 1;
                             validations.add(new Validation(v,i));
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
     }
}
