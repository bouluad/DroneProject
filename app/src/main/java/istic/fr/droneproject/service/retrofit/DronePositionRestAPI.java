package istic.fr.droneproject.service.retrofit;

import istic.fr.droneproject.model.DronePosition;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by yousra on 24/04/17.
 */

public interface DronePositionRestAPI {
    String ENDPOINT = "http://148.60.11.238:8080/";

    @Headers({ "Accept: application/json" })
    @GET("/positiondrone/{id}/intervention")
    Call<DronePosition> getDronePositionByIdIntervention(@Path("id") String id);

}