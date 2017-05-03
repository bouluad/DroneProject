package istic.fr.droneproject.service.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import istic.fr.droneproject.model.DronePhotos;
import istic.fr.droneproject.service.DronePhotosService;
import istic.fr.droneproject.service.retrofit.DronePhotosRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DronePhotosServiceImpl implements DronePhotosService{

    private static DronePhotosServiceImpl instance = new DronePhotosServiceImpl();
    private Callback<List<DronePhotos>> callback;
    private Callback<List<DronePhotos>> callbackPosition;
    private Socket socket;
    private String storedIdIntervention;
    private String pos1;
    private String pos2;

    public static DronePhotosServiceImpl getInstance() {
        return instance;
    }

    private DronePhotosServiceImpl() {
        try {
            socket = IO.socket("http://148.60.11.238:8080");
        } catch (URISyntaxException e) {
            Log.e("DronePhotosService", e.toString());
        }
        socket.connect();
        Log.e("DronePhotosService", "Socket connectee");

        socket.on("newPhoto", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("DronePhotosService", "Listener called");
                if (args.length > 0) {
                    Log.e("DronePhotosService", args[0].toString());
                    String idIntervention = (String) args[0];
                    if (idIntervention.equals(storedIdIntervention)) {
                        if(callback != null){
                            getDronePhotosbyIdIntervention(storedIdIntervention, callback);
                        }else if(callbackPosition != null){
                            getDronePhotosbyPositionPTS(pos1, pos2, storedIdIntervention, callbackPosition);
                        }
                    }
                    // Sinon l'intervention modifiee ne nous interesse pas
                }
            }
        });
    }

    @Override
    public void getDronePhotosbyIdIntervention(String id, Callback<List<DronePhotos>> callback) {
        this.storedIdIntervention = id;
        this.callback = callback;
        this.callbackPosition = null;
        this.pos1 = null;
        this.pos2 = null;

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

    @Override
    public void getDronePhotosbyPositionPTS(String pos1, String pos2, String idInter,Callback<List<DronePhotos>> callback) {

        this.storedIdIntervention = idInter;
        this.callbackPosition = callback;
        this.callback = null;
        this.pos1 = pos1;
        this.pos2 = pos2;

               /*
        Création de l'objet Retrofit
         */
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DronePhotosRestAPI.ENDPOINT).addConverterFactory(GsonConverterFactory.create(gson)).build();

        final DronePhotosRestAPI dronePhotosRestAPI = retrofit.create(DronePhotosRestAPI.class);

        /*
        Appel de la méthode pour l'API REST
         */
        Call<List<DronePhotos>> call = dronePhotosRestAPI.getDronePhotosbyPositionPTS(pos1,pos2,idInter);

        /*
        On lance l'appel et le callback recevra la réponse
         */
        call.enqueue(callback);

    }

    public void deleteCallbacks(){
        this.storedIdIntervention = null;
        this.callback = null;
        this.callbackPosition = null;
        this.pos1 = null;
        this.pos2 = null;
    }

}
