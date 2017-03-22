package istic.fr.droneproject.model;

/**
 * Modèle d'un véhicule
 * Les heures sont celles présentes dans le tableau des moyens
 */
public class Vehicule {
    public String nom;
    public TypeVehicule type;
    public EtatVehicule etat;
    public String heureDemande;
    public String heureEngagement;
    public String heureArrivee;
    public String heureAnnulation;
    public Categorie categorie;
    public Position position;
}
