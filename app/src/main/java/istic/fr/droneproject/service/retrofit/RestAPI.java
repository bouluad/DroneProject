package istic.fr.droneproject.service.retrofit;

import java.util.List;

import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestAPI {

    @GET("interventions/")
//TODO Modifier avec le vrai path
    Call<List<Intervention>> getListeInterventions();

    @POST("interventions/")
//TODO Modifier avec le vrai path
    Call<Void> addNouvelleIntervention(@Body Intervention intervention);

    @POST("users/")
    Call<Void> login(@Body User user);
}
