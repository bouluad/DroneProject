package istic.fr.droneproject;

import android.content.DialogInterface;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import istic.fr.droneproject.model.Categorie;
import istic.fr.droneproject.model.CodeSinistre;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.TypeVehicule;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.InterventionService;
import istic.fr.droneproject.service.impl.InterventionServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by salma on 21/03/17.
 */

public class CodisNewInterventionActivity extends AppCompatActivity {
    String[] categorie = {"SAUVETAGE", "INCENDIE", "RISQUE PARTICULIER", "EAU", "COMMANDEMENT"};
    Intervention intervention;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.codis_new_intervention);
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


        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intervention = new Intervention();
                intervention.libelle = libelle.getText().toString();
                intervention.adresse = adresse.getText().toString();
                intervention.code = CodeSinistre.INC;
                intervention.vehicules= new ArrayList<Vehicule>();

                String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
                intervention.date = currentDateandTime;


                boolean inc = radioButtonCode.isChecked();

                if (inc) {
                    intervention.code = CodeSinistre.INC;


                } else {
                    intervention.code = CodeSinistre.SAP;


                }





                Toast.makeText(getApplicationContext(), intervention.code.toString(), Toast.LENGTH_SHORT).show();
                InterventionService service = new InterventionServiceImpl();
                service.addNouvelleIntervention(intervention, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {



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

                Vehicule vehicule= new Vehicule();
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




                intervention.vehicules.add(vehicule);



                Toast.makeText(getApplicationContext(), selectedSpinner, Toast.LENGTH_SHORT).show();

            }
        });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

    }
}
