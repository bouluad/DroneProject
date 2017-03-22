package istic.fr.droneproject.service.retrofit;

import istic.fr.droneproject.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserRestAPI {

    /*
     * Adresse du serveur NodeJS
     */
    String BASE_URL = "http://148.60.11.238:4000/";

    @POST("users/")
    Call<Void> login(@Body User user);
}
