package istic.fr.droneproject.service.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
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

    private Callback callback;
    private String interventionId;
    private Socket socket;

    public static DroneServiceImpl droneInstance=new DroneServiceImpl();

    public static DroneServiceImpl getInstance(){
        return droneInstance;
    }

    //GET drone
    public void getDroneByIdIntervention(String id,Callback<Drone> callback) {
        this.callback = callback;
        this.interventionId = id;
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

    private DroneServiceImpl() {

        try {
            socket = IO.socket("http://148.60.11.238:8080");
        } catch (URISyntaxException e) {
            Log.e("dronenupdate", e.toString());
        }
        socket.connect();
        Log.e("dronenupdate", "Socket connectee");

        socket.on("dronenupdate", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("dronenupdate", "Listener called");
                if (args.length > 0) {
                    Log.e("dronenupdate", args[0].toString());
                    String idIntervention = (String) args[0];
                    if (idIntervention.equals(interventionId) && callback != null) {
                        //Recuperer que cette intervention
                        getDroneByIdIntervention(idIntervention, callback);
                    }
                    // Sinon l'intervention modifiee ne nous interesse pas
                }
            }
        });

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
