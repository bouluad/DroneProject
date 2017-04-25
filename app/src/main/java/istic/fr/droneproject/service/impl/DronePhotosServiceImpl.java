package istic.fr.droneproject.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import istic.fr.droneproject.model.DronePhotos;
import istic.fr.droneproject.service.DronePhotosService;
import istic.fr.droneproject.service.retrofit.DronePhotosRestAPI;
import istic.fr.droneproject.service.retrofit.DroneRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static istic.fr.droneproject.service.retrofit.InterventionRestAPI.BASE_URL;

/**
 * Created by yousra on 25/04/17.
 */

public class DronePhotosServiceImpl implements DronePhotosService{

    @Override
    public void getDronePhotosbyIdIntervention(String id, Callback<DronePhotos> callback) {
         /*
        Création de l'objet Retrofit
         */
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DronePhotosRestAPI.ENDPOINT).addConverterFactory(GsonConverterFactory.create(gson)).build();

        final DronePhotosRestAPI dronePhotosRestAPI = retrofit.create(DronePhotosRestAPI.class);

        /*
        Appel de la méthode pour l'API REST
         */
        Call<DronePhotos> call = dronePhotosRestAPI.getDronePhotosbyIdIntervention(id);

        /*
        On lance l'appel et le callback recevra la réponse
         */
        call.enqueue(callback);
    }

    @Override
    public void addDronePhotos(DronePhotos dronephotos, Callback<Void> callback) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final DronePhotosRestAPI dronePhotosRestAPI = retrofit.create(DronePhotosRestAPI.class);

        Call<Void> call = dronePhotosRestAPI.addDronePhotos(dronephotos);
        call.enqueue(callback);

    }
}
