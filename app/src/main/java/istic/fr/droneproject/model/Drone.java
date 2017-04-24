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

    private Zone zone;

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


