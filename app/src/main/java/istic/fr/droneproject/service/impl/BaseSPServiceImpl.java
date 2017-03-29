package istic.fr.droneproject.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import istic.fr.droneproject.model.Drone;
import istic.fr.droneproject.model.PointInteret;
import istic.fr.droneproject.service.BaseSPService;
import istic.fr.droneproject.service.retrofit.BaseSPRestAPI;
import istic.fr.droneproject.service.retrofit.DroneRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nirina on 29/03/17.
 */

public class BaseSPServiceImpl implements BaseSPService{
    @Override
    public void getBaseSP(Callback<List<PointInteret>> callback) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseSPRestAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final BaseSPRestAPI baseSPRestAPI = retrofit.create(BaseSPRestAPI.class);
        Call<List<PointInteret>> call = baseSPRestAPI.getBaseSP();
        call.enqueue(callback);
    }

    @Override
    public void addNouveauPointInteret(PointInteret pointinteret, Callback<Void> callback) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseSPRestAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final BaseSPRestAPI baseSPRestAPI = retrofit.create(BaseSPRestAPI.class);
        Call<Void> call = baseSPRestAPI.addNouveauPointInteret(pointinteret);
        call.enqueue(callback);
    }
}
