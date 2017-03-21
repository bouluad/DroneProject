package istic.fr.droneproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mListView = (ListView) findViewById(R.id.listInterventions);

        final ArrayList<String> interventionList = new ArrayList<>(Arrays.asList("intervention1", "intervention2"));
        String[] listItems = new String[interventionList.size()];

        for(int i = 0; i < interventionList.size(); i++){
            String intervention = interventionList.get(i);
            listItems[i] = intervention;
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);
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


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);*/
    }




}
