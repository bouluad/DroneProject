package istic.fr.droneproject.model;

import java.util.List;

/**
 * Modèle d'un trajet de drone par zone
 * Représenté par une liste de points pour le contour extérieur
 * Contient une liste de points à éviter pendant le parcours
 * nom_drone correspond au nom du drone (véhicule) effectuant ou ayant effectué le trajet
 */
public class ParcoursZone {
    public List<Double[]> contour;
    public List<PositionRayon> points;
    public String nom_drone;
    public EtatTrajetDrone etat;
    public Boolean tag_photo;
    public Boolean tag_loop;
}
