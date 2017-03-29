package istic.fr.droneproject;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import istic.fr.droneproject.adapter.CodisPremierDepartAdapter;
import istic.fr.droneproject.model.Categorie;
import istic.fr.droneproject.model.CodeSinistre;
import istic.fr.droneproject.model.EtatVehicule;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.TypeVehicule;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by salma on 21/03/17.
 */

public class CodisNewInterventionActivity extends AppCompatActivity {
    Intervention intervention;
    RecyclerView moyensRecyclerView;
    CodisPremierDepartAdapter moyensAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codis_new_intervention);

        intervention = new Intervention();
        intervention.vehicules = new ArrayList<>();

        moyensRecyclerView = (RecyclerView) findViewById(R.id.listMoyens);
        moyensAdapter = new CodisPremierDepartAdapter(R.layout.codis_premier_depart_item, intervention.vehicules);
        moyensRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        moyensRecyclerView.setAdapter(moyensAdapter);

        final EditText libelle = (EditText) findViewById(R.id.libelle);
        final EditText adresse = (EditText) findViewById(R.id.adresse);

        final RadioButton radioBtnINC = (RadioButton) findViewById(R.id.code_radio_inc);

        final FloatingActionButton btn_add_moyen = (FloatingActionButton) findViewById(R.id.btn_addMoyen);
        btn_add_moyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAjoutVehicule();
            }
        });

        final Button btn_valider = (Button) findViewById(R.id.ButtonSendForm);
        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intervention.libelle = libelle.getText().toString();
                intervention.adresse = adresse.getText().toString();
                intervention.position = new Double[2];
                intervention.date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRANCE).format(new Date());

                if (radioBtnINC.isChecked()) {
                    intervention.code = CodeSinistre.INC;
                } else {
                    intervention.code = CodeSinistre.SAP;
                }

                InterventionServiceCentral.getInstance().addNouvelleIntervention(intervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getApplicationContext(), "Intervention ajoutée : "+intervention.libelle, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void popupAjoutVehicule() {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Ajouter un moyen de premier départ");

        LayoutInflater inflater = getLayoutInflater();
        final View popupLayout = inflater.inflate(R.layout.codis_add_moyen_popup, null);

        final EditText nom_vehicule = (EditText) popupLayout.findViewById(R.id.nom_moyen);

        final Spinner categorieSpinner = (Spinner) popupLayout.findViewById(R.id.spinnerCategorie);
        Categorie[] categories = Categorie.values();
        ArrayAdapter<Categorie> adapter = new ArrayAdapter<>(CodisNewInterventionActivity.this, android.R.layout.simple_spinner_item, categories);
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

                String currentTime = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
                vehicule.heureDemande = currentTime;
                vehicule.heureEngagement = currentTime;

                vehicule.etat = EtatVehicule.ENGAGE;

                intervention.vehicules.add(vehicule);
                moyensAdapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Véhicule enregistré", Toast.LENGTH_SHORT).show();
            }
        });

        helpBuilder.create().show();
    }
}
