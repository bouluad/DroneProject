package istic.fr.droneproject.model;

import java.util.List;

/**
 * Modèle d'un trajet par segment
 * Représenté par une liste de points ordonnée selon l'ordre de parcours
 * nom_drone correspond au nom du drone (véhicule) effectuant ou ayant effectué le trajet
 */
public class ParcoursSegment {
    public List<Double[]> points;
    public Boolean tag_photo;
    public Boolean tag_loop;
    public EtatTrajetDrone etat;
    public String nom_drone;
}
