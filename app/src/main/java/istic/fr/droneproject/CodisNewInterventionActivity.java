package istic.fr.droneproject;


import android.content.DialogInterface;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


import istic.fr.droneproject.model.Categorie;
import istic.fr.droneproject.model.CodeSinistre;
import istic.fr.droneproject.model.EtatVehicule;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.TypeVehicule;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.InterventionService;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by salma on 21/03/17.
 */

public class CodisNewInterventionActivity extends AppCompatActivity {
    String[] categorie = {"SAUVETAGE", "INCENDIE", "RISQUE PARTICULIER", "EAU", "COMMANDEMENT"};
    Intervention intervention;
    Vehicule vehicule;
    ListView listView ;
    ArrayAdapter<String> listAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.codis_new_intervention);


        listView = (ListView) findViewById( R.id.listMoyens);
        String[] names = new String[] { };
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(names) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.codis_nom_vehicule_row, planetList);

        listView.setAdapter( listAdapter );



        final EditText libelle = (EditText) findViewById(R.id.libelle);
        final EditText adresse = (EditText) findViewById(R.id.adresse);

        final RadioButton radioButtonCode = (RadioButton) findViewById(R.id.code_radio_inc);

        final FloatingActionButton btn_add_moyen = (FloatingActionButton) findViewById(R.id.btn_addMoyen);
        btn_add_moyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSimplePopUp();
            }
        });

        final Button btn_valider = (Button) findViewById(R.id.ButtonSendForm);
        intervention = new Intervention();
        intervention.vehicules= new ArrayList<Vehicule>();

        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intervention.libelle = libelle.getText().toString();
                intervention.adresse = adresse.getText().toString();
                intervention.code = CodeSinistre.INC;

                intervention.position = new Double[2];

                String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
                intervention.date = currentDateandTime;
                System.out.println(intervention.date);


                boolean inc = radioButtonCode.isChecked();

                if (inc) {
                    intervention.code = CodeSinistre.INC;


                } else {
                    intervention.code = CodeSinistre.SAP;


                }











                InterventionServiceCentral.getInstance().addNouvelleIntervention(intervention, new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        Toast.makeText(getApplicationContext(), "Intervention ajoutée", Toast.LENGTH_SHORT).show();




                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });







    }
    private void showSimplePopUp() {


        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("Moyens 1er depart");
        /*helpBuilder.setMessage("This is a Simple Pop Up");*/

        LayoutInflater inflater = getLayoutInflater();
        final View popupLayout = inflater.inflate(R.layout.codis_add_moyen_popup, null);



        final EditText nom_vehicule = (EditText)popupLayout.findViewById(R.id.nom_moyen);
        final Spinner popupSpinner = (Spinner)popupLayout.findViewById(R.id.spinnerCategorie);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(CodisNewInterventionActivity.this,
                        android.R.layout.simple_spinner_item, categorie);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        popupSpinner.setAdapter(adapter);




        final RadioGroup radiogroup =  (RadioGroup) popupLayout.findViewById(R.id.type_radio);














        helpBuilder.setView(popupLayout);



        helpBuilder.setPositiveButton("Ajouter",
        new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                //add to list vehicule

                vehicule= new Vehicule();


                vehicule.nom=  nom_vehicule.getText().toString();
                int selectedId = radiogroup.getCheckedRadioButtonId();

                final RadioButton radioButton = (RadioButton) popupLayout.findViewById(selectedId);

                String radio_value = radioButton.getText().toString();
                switch(radio_value) {
                    case "FPT":

                        vehicule.type=TypeVehicule.FPT;
                        break;
                    case "VLCG":

                        vehicule.type=TypeVehicule.VLCG;
                        break;
                    case "VSAV":

                        vehicule.type=TypeVehicule.VSAV;
                        break;
                }

                String selectedSpinner = popupSpinner.getSelectedItem().toString();
                switch(selectedSpinner) {

                    case "SAUVETAGE":

                        vehicule.categorie=Categorie.SAUVETAGE;
                        break;
                    case "INCENDIE":

                        vehicule.categorie=Categorie.INCENDIE;
                        break;
                    case "RISQUE_PARTICULIER":

                        vehicule.categorie=Categorie.RISQUE_PARTICULIER;
                        break;
                    case "EAU":

                        vehicule.categorie=Categorie.EAU;
                        break;
                    case "COMMANDEMENT":

                        vehicule.categorie=Categorie.COMMANDEMENT;
                        break;
                }

                String currentTime = new SimpleDateFormat("HH:mm").format(new Date());
                vehicule.heureDemande=currentTime;
                vehicule.heureEngagement=currentTime;
                vehicule.etat= EtatVehicule.ENGAGE;





                intervention.vehicules.add(vehicule);
                listAdapter.add( vehicule.nom );


                // Set the ArrayAdapter as the ListView's adapter.


                Toast.makeText(getApplicationContext(), "Véhicule enregistré", Toast.LENGTH_SHORT).show();


            }
        });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();


    }
}
