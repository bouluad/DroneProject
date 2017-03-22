package istic.fr.droneproject.service.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import istic.fr.droneproject.model.Position;
import istic.fr.droneproject.service.DroneService;
import istic.fr.droneproject.service.retrofit.DroneRestAPI;
import istic.fr.droneproject.service.retrofit.InterventionRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static istic.fr.droneproject.service.retrofit.InterventionRestAPI.BASE_URL;

/**
 * Created by yousra on 22/03/17.
 */

public class DronePositionServiceImpl implements DroneService {

    //GET drone position
    public void getPosition(Callback<Position> callback) {

        /*
        Création de l'objet Retrofit
         */
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DroneRestAPI.ENDPOINT).addConverterFactory(GsonConverterFactory.create(gson)).build();

        final DroneRestAPI droneRestAPI = retrofit.create(DroneRestAPI.class);

        /*
        Appel de la méthode pour l'API REST
         */
        Call<Position> call = droneRestAPI.getPosition();

        /*
        On lance l'appel et le callback recevra la réponse
         */
        call.enqueue(callback);
    }

    //POST drone position
    public void setPosition(Position pos, Callback<Void> callback) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final DroneRestAPI droneRestAPI = retrofit.create(DroneRestAPI.class);

        Call<Void> call = droneRestAPI.setPosition(pos);
        call.enqueue(callback);
    }
}
