package istic.fr.droneproject.service;

import istic.fr.droneproject.model.Drone;
import retrofit2.Callback;


/**
 * Created by yousra on 22/03/17.
 */

public interface DroneService {



    /**
     * Récupérer le drone
     * @param callback Callback pour récupérer la réponse
     */

    void getDroneById(String id, Callback<Drone> callback);


    /**
     * Envoyer le drone
     * @param drone drone
     * @param callback Callback pour récupérer la réponse
     */
    void setDrone(Drone drone, Callback<Void> callback);
}
