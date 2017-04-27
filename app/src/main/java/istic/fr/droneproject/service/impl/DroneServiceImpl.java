package istic.fr.droneproject.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import istic.fr.droneproject.model.Drone;
import istic.fr.droneproject.service.DroneService;
import istic.fr.droneproject.service.retrofit.DroneRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static istic.fr.droneproject.service.retrofit.InterventionRestAPI.BASE_URL;

/**
 * Created by yousra on 22/03/17.
 */

public class DroneServiceImpl implements DroneService {

    public static DroneServiceImpl droneInstance=new DroneServiceImpl();

    public static DroneServiceImpl getInstance(){
        return droneInstance;
    }

    //GET drone
    public void getDroneByIdIntervention(String id,Callback<Drone> callback) {

        /*
        Création de l'objet Retrofit
         */
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DroneRestAPI.ENDPOINT).addConverterFactory(GsonConverterFactory.create(gson)).build();

        final DroneRestAPI droneRestAPI = retrofit.create(DroneRestAPI.class);

        /*
        Appel de la méthode pour l'API REST
         */
        Call<Drone> call = droneRestAPI.getDroneByIdIntervention(id);

        /*
        On lance l'appel et le callback recevra la réponse
         */
        call.enqueue(callback);
    }

    //POST drone position
    public void setDrone(Drone drone, Callback<Void> callback) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final DroneRestAPI droneRestAPI = retrofit.create(DroneRestAPI.class);

        Call<Void> call = droneRestAPI.setDrone(drone);
        call.enqueue(callback);
    }

    @Override
    public void updateDrone(Drone drone, Callback<Void> callback) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final DroneRestAPI droneRestAPI = retrofit.create(DroneRestAPI.class);

        Call<Void> call = droneRestAPI.updateDrone(drone);
        call.enqueue(callback);
    }
}
