package istic.fr.droneproject.service.retrofit;

import java.util.List;

import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.Position;
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
//TODO Modifier avec le vrai path
    Call<List<Intervention>> getListeInterventions();

    @POST("interventions/")
//TODO Modifier avec le vrai path
    Call<Void> addNouvelleIntervention(@Body Intervention intervention);

    @GET("interventions/{id}")
    Call<Intervention> getInterventionById(@Path("id") String id );

}
