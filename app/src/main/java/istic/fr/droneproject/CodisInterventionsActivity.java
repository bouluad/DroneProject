package istic.fr.droneproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import istic.fr.droneproject.adapter.InterventionRecyclerAdapter;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by salma on 21/03/17.
 */

public class CodisInterventionsActivity extends AppCompatActivity {
    private FloatingActionButton add;
    private List<Intervention> interventions;
    private InterventionRecyclerAdapter interventionArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codis_interventions);

        add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CodisInterventionsActivity.this, CodisNewInterventionActivity.class);
                startActivity(intent);
            }
        });

        final RecyclerView interventionsRecycler = (RecyclerView) findViewById(R.id.codis_list_interventions);


        interventionsRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        interventions = new ArrayList<>();
        InterventionRecyclerAdapter.InterventionClickListener interventionClickListener = new InterventionRecyclerAdapter.InterventionClickListener() {
            @Override
            public void clickIntervention(Intervention intervention) {

            }
        };
        interventionArrayAdapter = new InterventionRecyclerAdapter(interventions, R.layout.ui_intervention_item, interventionClickListener);
        interventionsRecycler.setAdapter(interventionArrayAdapter);

        recupererListeInterventions();

    }


    @Override
    protected void onResume() {
        super.onResume();
        recupererListeInterventions();
    }

    private void recupererListeInterventions() {
        InterventionServiceCentral.getInstance().getListeInterventions(new Callback<List<Intervention>>() {
            @Override
            public void onResponse(Call<List<Intervention>> call, Response<List<Intervention>> response) {
                Collections.reverse(response.body());

                interventions.clear();
                interventions.addAll(response.body());
                interventionArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Intervention>> call, Throwable t) {
                //DO NOTHING
                Log.e("UserInterventionsActivi", t.toString());
            }
        });
    }




}
