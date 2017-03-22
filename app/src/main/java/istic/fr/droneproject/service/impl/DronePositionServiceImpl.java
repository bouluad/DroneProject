package istic.fr.droneproject.service.impl;

import android.util.Log;

import istic.fr.droneproject.model.Position;
import istic.fr.droneproject.service.retrofit.DroneRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yousra on 22/03/17.
 */

public class DronePositionServiceImpl {

    //GET drone position
    public void getDronePositionwithRetrofit()  {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(DroneRestAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create()).build();
        //Create Service
        DroneRestAPI droneRestAPI = retrofit.create(DroneRestAPI.class);
        Call<Position> position= droneRestAPI.getPosition();
        position.enqueue(new Callback<Position>() {
            @Override
            public void onResponse(Call<Position> call, Response<Position> response) {
                Position pos=response.body();
              //  Log.e("getPosition======>",pos.latitude+""+pos.longitude);

            }

            @Override
            public void onFailure(Call<Position> call, Throwable t) {
                //Handle failure
                Log.e("EROOOOOOOOOR======>","you can't retreive drone position");
            }
        });

    }
    //POST drone position
    public void setDronePositionwithRetrofit(Position pos)  {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DroneRestAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create()).build();
        //Create Service
        DroneRestAPI droneRestAPI = retrofit.create(DroneRestAPI.class);
        Call<Position> position= droneRestAPI.setPosition(pos);
        position.enqueue(new Callback<Position>() {
            @Override
            public void onResponse(Call<Position> call, Response<Position> response) {
                Position pos=response.body();
                //  Log.e("setPosition======>",pos.latitude+""+pos.longitude);
            }

            @Override
            public void onFailure(Call<Position> call, Throwable t) {
                //Handle failure
                Log.e("EROOOOOOOOOR======>","you can't set drone position");
            }
        });

    }
}
