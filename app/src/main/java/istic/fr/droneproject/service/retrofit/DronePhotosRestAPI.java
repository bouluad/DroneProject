package istic.fr.droneproject.service.retrofit;

import istic.fr.droneproject.model.Drone;
import istic.fr.droneproject.model.DronePhotos;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by yousra on 25/04/17.
 */

public interface DronePhotosRestAPI {

    String ENDPOINT = "http://148.60.11.238:8080/";

    @Headers({ "Accept: application/json" })
    @GET("/drones/photos/{id}/intervention")
    Call<DronePhotos> getDronePhotosbyIdIntervention(@Path("id") String id);


    @POST("/drones/photos")
    Call<Void> addDronePhotos(@Body DronePhotos drone );
}
