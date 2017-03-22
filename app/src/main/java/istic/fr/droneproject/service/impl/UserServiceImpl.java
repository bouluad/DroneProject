package istic.fr.droneproject.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import istic.fr.droneproject.model.User;
import istic.fr.droneproject.service.UserService;
import istic.fr.droneproject.service.retrofit.UserRestAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserServiceImpl implements UserService {

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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UserRestAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        final UserRestAPI userRestAPI = retrofit.create(UserRestAPI.class);

        Call<Void> call = userRestAPI.login(user);
        call.enqueue(callback);
    }
}
