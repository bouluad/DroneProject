package istic.fr.droneproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import istic.fr.droneproject.adapter.TableauMoyenRecyclerAdapter;
import istic.fr.droneproject.model.Intervention;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FloatingActionButton btnAdd = (FloatingActionButton) view.findViewById(R.id.utm_btn_add);
        final RecyclerView vehiculesRecycler = (RecyclerView) view.findViewById(R.id.utm_list_vehicules);

        vehiculesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        final List<Vehicule> vehicules = new ArrayList<>();

        final TableauMoyenRecyclerAdapter vehiculeArrayAdapter = new TableauMoyenRecyclerAdapter(vehicules, R.layout.utm_vehicule_item,currentIntervention);
        vehiculesRecycler.setAdapter(vehiculeArrayAdapter);

        InterventionServiceCentral.getInstance().getInterventionById(idIntervention, new Callback<Intervention>() {
            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                currentIntervention = response.body();
                vehicules.clear();
                if(currentIntervention != null) {
                    vehicules.addAll(currentIntervention.vehicules);
                }
                vehiculeArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Intervention> call, Throwable t) {
                Log.e("UTM",t.toString());
                //DO NOTHING
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Faire une demande de moyen
                Log.e("UTM", "Ajouter Moyen");
            }
        });

    }
}
