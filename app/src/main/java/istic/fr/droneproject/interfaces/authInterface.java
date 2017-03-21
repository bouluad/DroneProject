package istic.fr.droneproject.interfaces;

import istic.fr.droneproject.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by bouluad on 21/03/17.
 */
public interface authInterface {

    @GET("api/{login}/{password}")
    Call<User> authenticate(@Path("login") String login, @Path("password") String password);

    @POST("api/{email}/{password}")
    Call<User> registration(@Path("email") String email, @Path("password") String password);
}