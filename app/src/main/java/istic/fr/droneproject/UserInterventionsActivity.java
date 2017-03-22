package istic.fr.droneproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import istic.fr.droneproject.adapter.InterventionRecyclerAdapter;
import istic.fr.droneproject.adapter.VehiculeRecyclerAdapter;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.service.InterventionService;
import istic.fr.droneproject.service.impl.InterventionServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInterventionsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_interventions);

        final RecyclerView interventionsRecycler = (RecyclerView) findViewById(R.id.ui_list_interventions);
        final View layoutDetails = findViewById(R.id.ui_layout_details);
        final Button btnSelect = (Button) findViewById(R.id.ui_btn_select);
        final TextView textAdresse = (TextView) findViewById(R.id.ui_text_adresse);
        final TextView textLibelle = (TextView) findViewById(R.id.ui_text_libelle);
        final TextView textPosition = (TextView) findViewById(R.id.ui_text_position);
        final TextView textDate = (TextView) findViewById(R.id.ui_text_date);
        final TextView textCode = (TextView) findViewById(R.id.ui_text_code);
        final RecyclerView vehiculesRecycler = (RecyclerView) findViewById(R.id.ui_list_vehicules);

        interventionsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        vehiculesRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        layoutDetails.setVisibility(View.GONE);

        final List<Intervention> interventions = new ArrayList<>();
        InterventionRecyclerAdapter.InterventionClickListener interventionClickListener = new InterventionRecyclerAdapter.InterventionClickListener() {
            @Override
            public void clickIntervention(Intervention intervention) {
                layoutDetails.setVisibility(View.VISIBLE);
                textLibelle.setText(intervention.libelle);
                textAdresse.setText(intervention.adresse);
                textPosition.setText(String.valueOf(intervention.position));
                textDate.setText(intervention.date);
                textCode.setText(String.valueOf(intervention.code));
                btnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO Implementer la redirection vers l'ihm principale
                    }
                });
                VehiculeRecyclerAdapter vehiculeArrayAdapter = new VehiculeRecyclerAdapter(intervention.vehicules, R.layout.ui_vehicule_item);
                vehiculesRecycler.setAdapter(vehiculeArrayAdapter);
            }
        };
        final InterventionRecyclerAdapter interventionArrayAdapter = new InterventionRecyclerAdapter(interventions, R.layout.ui_intervention_item, interventionClickListener);
        interventionsRecycler.setAdapter(interventionArrayAdapter);

        InterventionService service = new InterventionServiceImpl();
        service.getListeInterventions(new Callback<List<Intervention>>() {
            @Override
            public void onResponse(Call<List<Intervention>> call, Response<List<Intervention>> response) {
                interventions.clear();
                interventions.addAll(response.body());
                interventionArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Intervention>> call, Throwable t) {
                //DO NOTHING
            }
        });

    }
}
