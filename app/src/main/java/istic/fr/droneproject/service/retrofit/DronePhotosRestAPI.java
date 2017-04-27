package istic.fr.droneproject.service.retrofit;

import java.util.List;

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
    @GET("/photosdrone/{id}/intervention")
    Call<List<DronePhotos>> getDronePhotosbyIdIntervention(@Path("id") String id);

}
