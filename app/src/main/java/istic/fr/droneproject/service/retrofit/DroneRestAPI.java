package istic.fr.droneproject.service.retrofit;

import istic.fr.droneproject.model.Drone;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
 * Created by yousra on 22/03/17.
 */


public interface DroneRestAPI {
    public static final String ENDPOINT = "http://148.60.11.238:4000";

    @Headers({ "Accept: application/json" })
    @GET("/position")
    Call<Drone> getDrone();

    @POST("/position")
    Call<Void> setDrone(@Body Drone drone );

}
