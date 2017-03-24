package istic.fr.droneproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import istic.fr.droneproject.model.Drone;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Position;
import istic.fr.droneproject.service.DroneService;
import istic.fr.droneproject.service.InterventionService;
import istic.fr.droneproject.service.impl.DronePositionServiceImpl;
import istic.fr.droneproject.service.impl.InterventionServiceImpl;
import istic.fr.droneproject.service.retrofit.DroneRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestServiceDroneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_service_drone);
        InterventionService service = new InterventionServiceImpl();
        service.getInterventionById("58d1327e9bce7c234254cf28", new Callback<Intervention>() {

            @Override
            public void onResponse(Call<Intervention> call, Response<Intervention> response) {
                Log.e("La position est ==", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Intervention> call, Throwable t) {
                Log.e("Position Not retreived", "");
            }
        });

    }

    //GET drone position
    public void utilisation() {
        Log.e("hihooooooooo","1259999999999");

        /*
        Ici on voit comment utiliser le service d'exemple
        Cette classe peut être une activité, un fragment, etc.
        Cela permet d'appeler un service et dans le callback, de modifier l'interface graphique avec le résultat de l'appel
         */

      /*  DroneService service = new DronePositionServiceImpl();
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
        InterventionService service = new InterventionServiceImpl();
        service.getInterventionById("58d1327e9bce7c234254cf28", new Callback<Intervention>() {

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
        InterventionService service = new InterventionServiceImpl();
        service.getListeInterventions(new Callback<List<Intervention>>() {

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

            InterventionService service = new InterventionServiceImpl();
            service.addNouvelleIntervention(new Intervention(), new Callback<Void>() {
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


    public void uti(){
           DroneService service = new DronePositionServiceImpl();
        service.getDrone(new Callback<Drone>() {
            @Override
            public void onResponse(Call<Drone> call, Response<Drone> response) {
                Log.e("La position est ==",response.body().getPosition().get(0)+""+response.body().getPosition().get(1));
            }

            @Override
            public void onFailure(Call<Drone> call, Throwable t) {
                Log.e("Position Not retreived","Drone");
            }
        });
    }
    }

