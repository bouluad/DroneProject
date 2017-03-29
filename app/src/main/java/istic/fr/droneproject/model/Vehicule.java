package istic.fr.droneproject.model;

/**
 * Modèle d'un véhicule
 * Les heures sont celles présentes dans le tableau des moyens
 */
public class Vehicule {
    public String _id;
    public String nom;
    public TypeVehicule type;
    public EtatVehicule etat;
    public String heureDemande;

    public EtatVehicule getEtat() {
        return etat;
    }

    public void setEtat(EtatVehicule etat) {
        this.etat = etat;
    }

    public Double[] getPosition() {
        return position;
    }

    public void setPosition(Double[] position) {
        this.position = position;
    }

    public String heureEngagement;
    public String heureArrivee;
    public String heureLiberation;
    public Categorie categorie;
    public Double[] position;
}
