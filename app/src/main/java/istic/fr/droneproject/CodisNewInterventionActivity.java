package istic.fr.droneproject;

import android.content.Intent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import istic.fr.droneproject.model.CodeSinistre;
import istic.fr.droneproject.model.Intervention;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.codis_new_intervention);
        final EditText libelle = (EditText) findViewById(R.id.libelle);
        final EditText adresse = (EditText) findViewById(R.id.adresse);
        final EditText code = (EditText) findViewById(R.id.code);

        final Button btn_valider = (Button) findViewById(R.id.ButtonSendForm);


        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intervention intervention = new Intervention();
                intervention.libelle = libelle.getText().toString();
                intervention.adresse = adresse.getText().toString();
                intervention.code = CodeSinistre.INC;
                intervention.vehicules= new ArrayList<Vehicule>();

                String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
                intervention.date = currentDateandTime;




                Toast.makeText(getApplicationContext(), currentDateandTime, Toast.LENGTH_SHORT).show();
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






        /*InterventionService service = new InterventionServiceImpl();

        service.addNouvelleIntervention(Intervention intervention, new Callback<void>() {
            @Override
            public void onResponse(Call<List<Intervention>> call, Response<List<Intervention>> response) {
                interventions.clear();
                interventions.addAll(response.body());
                interventionArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Intervention>> call, Throwable t) {
                //DO NOTHING
                Log.e("UserInterventionsActivi", t.toString());
            }
        });*/
    }
}
