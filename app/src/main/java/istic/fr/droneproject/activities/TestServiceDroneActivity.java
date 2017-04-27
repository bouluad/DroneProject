package istic.fr.droneproject.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import istic.fr.droneproject.R;
import istic.fr.droneproject.model.Drone;
import istic.fr.droneproject.model.EtatDrone;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Segment;
import istic.fr.droneproject.service.impl.DronePositionServiceImpl;
import istic.fr.droneproject.service.impl.DroneServiceImpl;
import istic.fr.droneproject.service.impl.InterventionServiceCentral;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestServiceDroneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_service_drone);
        testUpdateDrone();
        /*InterventionServiceCentral.getInstance().getInterventionById("58d1327e9bce7c234254cf28", new Callback<Intervention>() {

            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                Log.e("La position est ==", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Intervention> call, Throwable t) {
                Log.e("Position Not retreived", "");
            }
        });*/

    }

    //GET drone position
    public void utilisation() {
        Log.e("hihooooooooo","1259999999999");

        /*
        Ici on voit comment utiliser le service d'exemple
        Cette classe peut être une activité, un fragment, etc.
        Cela permet d'appeler un service et dans le callback, de modifier l'interface graphique avec le résultat de l'appel
         */

      /*  DroneService service = new DroneServiceImpl();
        service.getDrone(new Callback<Drone>() {
            @Override
            public void onResponse(Call<Drone> call, Response<Drone> response) {
                Log.e("La position est ==",response.body().getPosition().get(0)+""+response.body().getPosition().get(1));
            }

            @Override
            public void onFailure(Call<Drone> call, Throwable t) {
                Log.e("Position Not retreived","Drone");
            }
        });*/
        InterventionServiceCentral.getInstance().getInterventionById("58d1327e9bce7c234254cf28", new Callback<Intervention>() {

            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                Log.e("La position est ==", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Intervention> call, Throwable t) {
                Log.e("Position Not retreived", "");
            }
        });
        Log.e("TEST5","TEST5");
    }

    public void test(){
        InterventionServiceCentral.getInstance().getListeInterventions(new Callback<List<Intervention>>() {

            @Override
            public void onResponse(Call<List<Intervention>> call, Response<List<Intervention>> response) {
                Log.e("La position est ==", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<List<Intervention>> call, Throwable t) {
                Log.e("Position Not retreived", "");
            }
        });
    }

        public void ajouterIntervention() {

            InterventionServiceCentral.getInstance().addNouvelleIntervention(new Intervention(), new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.e("La position est ==", String.valueOf(response.body()));
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("Position Not retreived", "");
                }
            });
        }

   /* public void testAjoutDrone(){
        Segment s=new Segment();
        Double[] tab=new Double[2];
        tab[0]=-1.158999999544115;
        tab[1]=48.44444444888;
        List<Double[]> list=new ArrayList<>();
        list.add(tab);

        s.setPoints(list);
        s.getPoints().add(tab);
        s.setBoucleFermee(false);
       Drone drone=new Drone("58ddf84c212566155e8e98ec", EtatDrone.SEGMENT,s);
       DroneServiceImpl.getInstance().setDrone(drone, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("Drone added", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Drone not created", "");
            }
        });

    }*/

    public void testrecupDrone(){

        DroneServiceImpl.getInstance().getDroneByIdIntervention("58ddf84c212566155e8e98ec",new Callback<Drone>() {
            @Override
            public void onResponse(Call<Drone> call, Response<Drone> response) {
                Log.e("Drone retreived", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Drone> call, Throwable t) {
                Log.e("Drone not created", "");
            }
        });

    }



 public void testUpdateDrone(){
        Segment s=new Segment();
        Double[] tab=new Double[2];
        tab[0]=-1.00000544115;
        tab[1]=48.44444444888;
        List<Double[]> list=new ArrayList<>();
        list.add(tab);

        s.setPoints(list);
        s.getPoints().add(tab);
        s.setBoucleFermee(true);
       Drone drone=new Drone("58ddf84c212566155e8e98ec", EtatDrone.SEGMENT,s);
       DroneServiceImpl.getInstance().updateDrone(drone, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("Drone updated", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Drone not updated", "");
            }
        });

    }



    }

