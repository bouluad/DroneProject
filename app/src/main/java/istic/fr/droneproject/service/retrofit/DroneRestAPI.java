package istic.fr.droneproject.service.retrofit;

import istic.fr.droneproject.model.Drone;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by yousra on 22/03/17.
 */


public interface DroneRestAPI {
     String ENDPOINT = "http://148.60.11.238:8080/";

    @Headers({ "Accept: application/json" })
    @GET("/drones/{id}/intervention")
    Call<Drone> getDroneByIdIntervention(@Path("id") String id);


    @POST("/drones")
    Call<Void> setDrone(@Body Drone drone );


}
