package istic.fr.droneproject.service;

import java.util.List;

import istic.fr.droneproject.model.DronePhotos;
import istic.fr.droneproject.model.Intervention;
import retrofit2.Callback;


public interface DronePhotosService {

    /**
     * Récupérer la position du drone
     *
     * @param callback Callback pour récupérer la réponse
     */

    void getDronePhotosbyIdIntervention(String id, Callback<List<DronePhotos>> callback);

    void getDronePhotosbyPositionPTS(String pos1,String pos2 ,String idInter,Callback<List<DronePhotos>> callback);
}
