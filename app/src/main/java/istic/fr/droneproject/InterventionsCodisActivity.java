package istic.fr.droneproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import istic.fr.droneproject.model.Intervention;

/**
 * Created by salma on 21/03/17.
 */

public class InterventionsCodisActivity extends AppCompatActivity {


    private ListView mListView;

    private FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interventions_codis);

        add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterventionsCodisActivity.this, NewInterventionActivity.class);
                startActivity(intent);
            }
        });
        mListView = (ListView) findViewById(R.id.listInterventions);

        final ArrayList<String> interventionList = new ArrayList<>(Arrays.asList("intervention1", "intervention2"));
        String[] listItems = new String[interventionList.size()];

        for(int i = 0; i < interventionList.size(); i++){
            String intervention = interventionList.get(i);
            listItems[i] = intervention;
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);







       /*

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interventions_codis);



       /* final ArrayList<Intervention> interventionList =  *//*get interventions*//*;


        String[] listItems = new String[interventionList.size()];

        for(int i = 0; i < interventionList.size(); i++){
            Intervention intervention = interventionList.get(i);
            listItems[i] = intervention.libelle;
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);

        */
    }




}
