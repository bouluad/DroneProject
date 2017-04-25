package istic.fr.droneproject.service;

import istic.fr.droneproject.model.DronePhotos;
import retrofit2.Callback;

/**
 * Created by yousra on 25/04/17.
 */

public interface DronePhotosService {

    /**
     * Récupérer la position du drone
     *
     * @param callback Callback pour récupérer la réponse
     */

    void getDronePhotosbyIdIntervention(String id, Callback<DronePhotos> callback);

    /**
     * Ajouter de nouvelles photos
     * @param dronephotos pour ajouter des photos de drone
     * @param callback Callback pour récupérer la réponse
     */
    void addDronePhotos(DronePhotos dronephotos,Callback<Void> callback);


}
