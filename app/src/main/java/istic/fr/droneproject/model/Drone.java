package istic.fr.droneproject.model;

/**
 * Created by yousra on 22/03/17.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Drone {

    private String idIntervention;

    private EtatDrone etat;

    public Drone(String idIntervention, EtatDrone etat) {
        this.idIntervention = idIntervention;
        this.etat = etat;
    }

    private Zone zone;

    public Drone(String idIntervention, EtatDrone etat, Segment segment) {
        this.idIntervention = idIntervention;
        this.etat = etat;
        this.segment = segment;
    }

    private Segment segment;


    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public EtatDrone getEtat() {
        return etat;
    }

    public void setEtat(EtatDrone etat) {
        this.etat = etat;
    }

    public String getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;
    }
}


