package istic.fr.droneproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.adapter.InterventionRecyclerAdapter;
import istic.fr.droneproject.adapter.TableauMoyenRecyclerAdapter;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.InterventionService;
import istic.fr.droneproject.service.impl.InterventionServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTableauMoyenActivity extends AppCompatActivity {

    Intervention currentIntervention;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_tableau_moyen);

        final FloatingActionButton btnAdd = (FloatingActionButton) findViewById(R.id.utm_btn_add);
        final RecyclerView vehiculesRecycler = (RecyclerView) findViewById(R.id.utm_list_vehicules);

        vehiculesRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final List<Intervention> interventions = new ArrayList<>();
        final List<Vehicule> vehicules = new ArrayList<>();
        Log.e("UTM","init vehicules");
//        List<Vehicule> vehicules = interventions.get(0).vehicules;//#TODO:modif pour avoir l'intervention actuel

        final TableauMoyenRecyclerAdapter vehiculeArrayAdapter = new TableauMoyenRecyclerAdapter(vehicules, R.layout.utm_vehicule_item,currentIntervention);
        vehiculesRecycler.setAdapter(vehiculeArrayAdapter);

        InterventionService service = new InterventionServiceImpl();
        service.getListeInterventions(new Callback<List<Intervention>>() {
            @Override
            public void onResponse(Call<List<Intervention>> call, Response<List<Intervention>> response) {
                Log.e("UTM","reloading vehicules");
                //TODO: modification pour sélectionner uniquement l'intervention courrante
                interventions.clear();
                interventions.addAll(response.body());
                vehicules.clear();
                vehicules.addAll(interventions.get(0).vehicules);
                currentIntervention = interventions.get(1);
                for(Intervention inter: interventions){
                    if(inter.vehicules != null)
                        for(Vehicule v : inter.vehicules){
                            vehicules.add(v);
                        }
                }
                vehiculeArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Intervention>> call, Throwable t) {
                Log.e("UTM","failure loading vehicules");
                t.printStackTrace();
                //DO NOTHING
            }
        });

    }
}
