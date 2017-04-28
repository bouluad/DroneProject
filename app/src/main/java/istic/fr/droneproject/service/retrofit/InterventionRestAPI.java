package istic.fr.droneproject.service.retrofit;

import java.util.List;

import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Vehicule;
import retrofit2.Call;
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
    Call<String> addNouvelleIntervention(@Body Intervention intervention);

    @GET("interventions/{id}")
    Call<Intervention> getInterventionById(@Path("id") String id );

    @POST("interventions/")
    Call<Void> updateIntervention(@Body Intervention intervention);

    @POST("interventions/{id}/cloturer")
    Call<Void> cloturerIntervention(@Path("id") String id );


}
