package istic.fr.droneproject.service.retrofit;

import java.util.List;

import istic.fr.droneproject.model.PointInteret;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by nirina on 29/03/17.
 */

public interface BaseSPRestAPI {

    /*
     * Adresse du serveur NodeJS
     */
    String BASE_URL = "http://148.60.11.238:8080/";

    @GET("baseSP/")
    Call<List<PointInteret>> getBaseSP();

    @POST("baseSP/")
    Call<Void> addNouveauPointInteret(@Body PointInteret pointinteret);
}
