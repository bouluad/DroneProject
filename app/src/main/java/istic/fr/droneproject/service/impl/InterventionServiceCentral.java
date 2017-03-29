package istic.fr.droneproject.service.impl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Vehicule;
import istic.fr.droneproject.service.InterventionService;
import istic.fr.droneproject.service.retrofit.InterventionRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InterventionServiceCentral implements InterventionService {
    private static InterventionServiceCentral ourInstance = new InterventionServiceCentral();
    private Callback callback;
    private String interventionId;
    private Socket socket;


    public static InterventionServiceCentral getInstance() {
        return ourInstance;
    }

    private InterventionServiceCentral() {

        try {
            socket = IO.socket("http://148.60.11.238:8080");
        } catch (URISyntaxException e) {
            Log.e("InterventionServiceCent", e.toString());
        }
        socket.connect();
        Log.e("ServiceCentral", "Socket connectee");

        socket.on("interventions", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("ServiceCentral", "Listener called");
                if (args.length > 0) {
                    Log.e("ServiceCentral", args[0].toString());
                    String idIntervention = (String) args[0];
                    if (interventionId == null && callback != null) {
                        //Recuperer tout
                        getListeInterventions(callback);
                    } else if (idIntervention.equals(interventionId) && callback != null) {
                        //Recuperer que cette intervention
                        getInterventionById(idIntervention, callback);
                    }
                    // Sinon l'intervention modifiee ne nous interesse pas
                }
            }
        });

    }

    @Override
    public void getListeInterventions(Callback<List<Intervention>> callback) {
        this.callback = callback;
        this.interventionId = null;

        /*
        Création de l'objet Retrofit
         */
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(InterventionRestAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

        final InterventionRestAPI interventionRestAPI = retrofit.create(InterventionRestAPI.class);

        /*
        Appel de la méthode pour l'API REST
         */
        Call<List<Intervention>> call = interventionRestAPI.getListeInterventions();

        /*
        On lance l'appel et le callback recevra la réponse
         */
        call.enqueue(callback);
    }

    @Override
    public void addNouvelleIntervention(Intervention intervention, Callback<Void> callback) {
        this.callback = null;
        this.interventionId = null;

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(InterventionRestAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final InterventionRestAPI interventionRestAPI = retrofit.create(InterventionRestAPI.class);

        Call<Void> call = interventionRestAPI.addNouvelleIntervention(intervention);
        call.enqueue(callback);
    }

    @Override
    public void getInterventionById(String id, Callback<Intervention> callback) {
        this.callback = callback;
        this.interventionId = id;

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(InterventionRestAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final InterventionRestAPI interventionRestAPI = retrofit.create(InterventionRestAPI.class);

        Call<Intervention> call = interventionRestAPI.getInterventionById(id);
        call.enqueue(callback);
    }

    @Override
    public void updateIntervention(Intervention intervention, Callback<Void> callback){
        this.interventionId = intervention._id;
        this.callback = callback;

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(InterventionRestAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

        InterventionRestAPI apiService = retrofit.create(InterventionRestAPI.class);

        Call<Void> call =  apiService.updateIntervention(intervention);
        call.enqueue(callback);
    }

    @Override
    public void cloturerIntervention(String idIntervention, Callback<Void> callback) {
        this.interventionId = null;
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(InterventionRestAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

        InterventionRestAPI apiService = retrofit.create(InterventionRestAPI.class);

        Call<Void> call =  apiService.cloturerIntervention(idIntervention);
        call.enqueue(callback);
    }


}
