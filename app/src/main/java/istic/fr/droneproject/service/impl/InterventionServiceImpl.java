package istic.fr.droneproject.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import istic.fr.droneproject.model.Intervention;

import istic.fr.droneproject.service.retrofit.InterventionRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static istic.fr.droneproject.service.retrofit.InterventionRestAPI.BASE_URL;

public class InterventionServiceImpl implements istic.fr.droneproject.service.InterventionService {

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

    /**
     * Implémentation du service
     *
     * @param intervention Nouvelle intervention
     * @param callback     Callback pour récupérer la réponse
     */
    @Override
    public void addNouvelleIntervention(Intervention intervention, Callback<Void> callback) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final InterventionRestAPI interventionRestAPI = retrofit.create(InterventionRestAPI.class);

        Call<Void> call = interventionRestAPI.addNouvelleIntervention(intervention);
        call.enqueue(callback);
    }

    @Override
    public void getInterventionById(String id,Callback<Intervention> callback) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final InterventionRestAPI interventionRestAPI = retrofit.create(InterventionRestAPI.class);
        Call<Intervention> call = interventionRestAPI.getInterventionById(id);
        call.enqueue(callback);

    }
}
