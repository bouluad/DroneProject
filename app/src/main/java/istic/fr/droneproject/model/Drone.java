package istic.fr.droneproject.model;

/**
 * Created by yousra on 22/03/17.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Drone {

    public String idIntervention;
    public EtatDrone etat;
    public Zone zone;
    public Segment segment;
    public String video;

    public Drone(String idIntervention, EtatDrone etat) {
        this.idIntervention = idIntervention;
        this.etat = etat;
    }

    public Drone(String idIntervention, EtatDrone etat, Segment segment) {
        this.idIntervention = idIntervention;
        this.etat = etat;
        this.segment = segment;
    }
}


