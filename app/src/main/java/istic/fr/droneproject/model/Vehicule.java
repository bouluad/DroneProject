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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public TypeVehicule getType() {
        return type;
    }

    public void setType(TypeVehicule type) {
        this.type = type;
    }

    public String getHeureDemande() {
        return heureDemande;
    }

    public void setHeureDemande(String heureDemande) {
        this.heureDemande = heureDemande;
    }

    public String getHeureEngagement() {
        return heureEngagement;
    }

    public void setHeureEngagement(String heureEngagement) {
        this.heureEngagement = heureEngagement;
    }

    public String getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(String heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public String getHeureLiberation() {
        return heureLiberation;
    }

    public void setHeureLiberation(String heureLiberation) {
        this.heureLiberation = heureLiberation;
    }

    public Double[] position;


    public boolean verifierArrive(){

        if ( EtatVehicule.ENGAGE.equals(this.etat) && this.position != null && this.heureEngagement != null ){
            return  true;
        }
        return false;

    }



    public boolean verifierEngage(){
        if ((EtatVehicule.DEMANDE.equals(this.etat) && this.position != null && this.heureDemande != null)
                || (EtatVehicule.PARKING.equals(this.etat)) ) {
            return  true;
        }


        return false;

    }

    public boolean verifierParking(){
        if ((EtatVehicule.DEMANDE.equals(this.etat))
                || (EtatVehicule.ARRIVE.equals(this.etat) || (EtatVehicule.ENGAGE.equals(this.etat)))  ) {
            return  true;
        }


        return false;

    }




}
