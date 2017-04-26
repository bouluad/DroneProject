package istic.fr.droneproject.service;

import istic.fr.droneproject.model.DronePhotos;
import retrofit2.Callback;


public interface DronePhotosService {

    /**
     * Récupérer la position du drone
     *
     * @param callback Callback pour récupérer la réponse
     */

    void getDronePhotosbyIdIntervention(String id, Callback<DronePhotos> callback);


}
