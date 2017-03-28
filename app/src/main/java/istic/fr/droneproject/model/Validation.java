package istic.fr.droneproject.model;

/**
 * Created by nirina on 24/03/17.
 */

public class Validation {
    public Vehicule vehicule;
    //public String libelle;
    public Intervention intervention;

    /*public Validation(Vehicule v,String l){
        this.vehicule = v;
        this.libelle = l;
    }*/
    public Validation(Vehicule v,Intervention i){
        this.vehicule = v;
        this.intervention = i;
    }
}
