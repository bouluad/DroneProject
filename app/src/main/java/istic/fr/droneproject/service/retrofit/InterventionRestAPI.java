package istic.fr.droneproject.service.retrofit;

import java.util.List;

import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Vehicule;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InterventionRestAPI {

    /*
     * Adresse du serveur NodeJS
     */
    String BASE_URL = "http://148.60.11.238:8080/";

    @GET("interventions/")
    Call<List<Intervention>> getListeInterventions();

    @POST("interventions/")
    Call<Void> addNouvelleIntervention(@Body Intervention intervention);

    @GET("interventions/{id}")
    Call<Intervention> getInterventionById(@Path("id") String id );

    @GET("updateIntervention/{id}")
    Call<Void> updateIntervention(Intervention intervention);

    @GET("interventions/{id}/vehicules")
    Call<List<Vehicule>> getListeVehicules(@Path("id") String id );
}
