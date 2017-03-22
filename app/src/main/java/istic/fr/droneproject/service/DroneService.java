package istic.fr.droneproject.service;

import istic.fr.droneproject.model.Position;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;


/**
 * Created by yousra on 22/03/17.
 */

public interface DroneService {



    /**
     * Récupérer la position du drone
     * @param callback Callback pour récupérer la réponse
     */

    void getPosition(Callback<Position> callback);


    /**
     * Envoyer une position au drone
     * @param Position Nouvelle position
     * @param callback Callback pour récupérer la réponse
     */
    void setPosition( Position position,Callback<Void> callback);
}
