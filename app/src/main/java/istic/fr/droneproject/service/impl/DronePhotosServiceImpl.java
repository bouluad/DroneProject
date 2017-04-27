package istic.fr.droneproject.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import istic.fr.droneproject.model.DronePhotos;
import istic.fr.droneproject.service.DronePhotosService;
import istic.fr.droneproject.service.retrofit.DronePhotosRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DronePhotosServiceImpl implements DronePhotosService{

    @Override
    public void getDronePhotosbyIdIntervention(String id, Callback<List<DronePhotos>> callback) {
         /*
        Création de l'objet Retrofit
         */
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DronePhotosRestAPI.ENDPOINT).addConverterFactory(GsonConverterFactory.create(gson)).build();

        final DronePhotosRestAPI dronePhotosRestAPI = retrofit.create(DronePhotosRestAPI.class);

        /*
        Appel de la méthode pour l'API REST
         */
        Call<List<DronePhotos>> call = dronePhotosRestAPI.getDronePhotosbyIdIntervention(id);

        /*
        On lance l'appel et le callback recevra la réponse
         */
        call.enqueue(callback);
    }

}
