package istic.fr.droneproject.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.service.retrofit.InterventionService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InterventionServiceImpl implements istic.fr.droneproject.service.InterventionService {

    /*
     * Adresse du serveur NodeJS
     */
    private static final String BASE_URL = "http://148.60.11.238:4000/";

    /**
     * Implémentation du service
     *
     * @param callback Callback pour récupérer la réponse
     */
    @Override
    public void getListeInterventions(Callback<List<Intervention>> callback) {

        /*
        Création de l'objet Retrofit
         */
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

        final InterventionService restAPI = retrofit.create(InterventionService.class);

        /*
        Appel de la méthode pour l'API REST
         */
        Call<List<Intervention>> call = restAPI.getListeInterventions();

        /*
        On lance l'appel et le callback recevra la réponse
         */
        call.enqueue(callback);
    }

    @Override
    public void addNouvelleIntervention(Intervention intervention, Callback<Void> callback) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final InterventionService restAPI = retrofit.create(InterventionService.class);

        Call<Void> call = restAPI.addNouvelleIntervention(intervention);
        call.enqueue(callback);
    }
}
