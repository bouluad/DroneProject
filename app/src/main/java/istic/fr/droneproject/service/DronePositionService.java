package istic.fr.droneproject.service;

import istic.fr.droneproject.model.Drone;
import istic.fr.droneproject.model.DronePosition;
import retrofit2.Callback;

/**
 * Created by yousra on 24/04/17.
 */

public interface DronePositionService {


    /**
     * Récupérer la position du drone
     * @param callback Callback pour récupérer la réponse
     */

    void getDronePositionByIdIntervention(String id, Callback<DronePosition> callback);

}
