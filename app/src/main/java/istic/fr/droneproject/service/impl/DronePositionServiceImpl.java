package istic.fr.droneproject.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import istic.fr.droneproject.model.DronePosition;
import istic.fr.droneproject.service.DronePositionService;
import istic.fr.droneproject.service.retrofit.DronePositionRestAPI;
import istic.fr.droneproject.service.retrofit.DroneRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yousra on 24/04/17.
 */

public class DronePositionServiceImpl implements DronePositionService{

    public static DronePositionServiceImpl droneInstance=new DronePositionServiceImpl();

    public static DronePositionServiceImpl getInstance(){
        return droneInstance;
    }

    //GET drone
    public void getDronePositionByIdIntervention(String id, Callback<DronePosition> callback) {

        /*
        Création de l'objet Retrofit
         */
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DroneRestAPI.ENDPOINT).addConverterFactory(GsonConverterFactory.create(gson)).build();

        final DronePositionRestAPI dronePositionRestAPI = retrofit.create(DronePositionRestAPI.class);

        /*
        Appel de la méthode pour l'API REST
         */
        Call<DronePosition> call = dronePositionRestAPI.getDronePositionByIdIntervention(id);

        /*
        On lance l'appel et le callback recevra la réponse
         */
        call.enqueue(callback);
    }

}
