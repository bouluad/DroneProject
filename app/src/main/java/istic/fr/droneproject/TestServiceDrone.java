package istic.fr.droneproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import istic.fr.droneproject.model.Drone;
import istic.fr.droneproject.service.DroneService;
import istic.fr.droneproject.service.impl.DronePositionServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestServiceDrone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_service_drone);
        utilisation();

    }

    //GET drone position
    public void utilisation() {

        /*
        Ici on voit comment utiliser le service d'exemple
        Cette classe peut être une activité, un fragment, etc.
        Cela permet d'appeler un service et dans le callback, de modifier l'interface graphique avec le résultat de l'appel
         */

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
