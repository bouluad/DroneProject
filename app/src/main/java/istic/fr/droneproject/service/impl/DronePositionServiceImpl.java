package istic.fr.droneproject.service.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
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

    private Callback callback;
    private String interventionId;
    private Socket socket;

    public static DronePositionServiceImpl droneInstance=new DronePositionServiceImpl();

    public static DronePositionServiceImpl getInstance(){
        return droneInstance;
    }

    //GET drone
    public void getDronePositionByIdIntervention(String id, Callback<DronePosition> callback) {
        this.callback = callback;
        interventionId = id;
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

    private DronePositionServiceImpl() {

        try {
            socket = IO.socket("http://148.60.11.238:8080");
        } catch (URISyntaxException e) {
            Log.e("positionupdate", e.toString());
        }
        socket.connect();
        Log.e("positionupdate", "Socket connectee");

        socket.on("positionUpdate", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("positionupdate", "Listener called");
                if (args.length > 0) {
                    Log.e("positionupdate", args[0].toString());
                    String idIntervention = (String) args[0];
                    if (interventionId != null && idIntervention.equals(interventionId) && callback != null) {
                        //Recuperer que cette intervention
                        getDronePositionByIdIntervention(idIntervention, callback);
                    }
                    // Sinon l'intervention modifiee ne nous interesse pas
                }
            }
        });

    }



    public void deleteCallbacks(){
        this.callback = null;
        this.interventionId = null;
    }

}
