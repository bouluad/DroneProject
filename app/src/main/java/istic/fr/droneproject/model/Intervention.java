package istic.fr.droneproject.model;

import java.util.List;

/**
 * Modèle d'une intervention
 */
public class Intervention {
    public String id;
    public String libelle;
    public String adresse;
    public String date;
    public CodeSinistre code;
    public List<Vehicule> vehicules;
    public List<PointInteret> points;
    public List<ParcoursSegment> segments;
    public List<ParcoursZone> zones;
    public List<Photo> photos;
}
