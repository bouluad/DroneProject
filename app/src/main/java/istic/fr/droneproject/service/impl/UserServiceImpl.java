package istic.fr.droneproject.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import istic.fr.droneproject.model.User;
import istic.fr.droneproject.service.UserService;
import istic.fr.droneproject.service.retrofit.RestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserServiceImpl implements UserService {

    /*
     * Adresse du serveur NodeJS
     */
    private static final String BASE_URL = "http://148.60.11.238:4000/";

    /**
     * Implémentation du service
     *
     * @param user     Utilisateur
     * @param callback Callback pour récupérer la réponse du serveur
     *                 200 Si ok
     */
    @Override
    public void login(User user, Callback<Void> callback) {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final RestAPI restAPI = retrofit.create(RestAPI.class);

        Call<Void> call = restAPI.login(user);
        call.enqueue(callback);
    }
}
