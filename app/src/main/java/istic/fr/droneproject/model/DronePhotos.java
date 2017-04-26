package istic.fr.droneproject.model;

import java.util.List;

/**
 * Modèle des photos prise par le drone
 * idIntervention c'est l'ID d'intervention de travail
 * photos la liste des photos proche de cette intervention
 * video lien vers video streaming
 */

public class DronePhotos {

    public String _id;
    public String nom;
    public String path;
    public Double[] position;
    public String date_heure;
    public Double[] positionPTS;
    public String idIntervention;

}
