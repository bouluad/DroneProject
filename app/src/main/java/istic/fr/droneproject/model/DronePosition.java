package istic.fr.droneproject.model;

/**
 * Created by yousra on 24/04/17.
 */

public class DronePosition {

    String idIntervention;
    Double[] postion;

    public String getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;
    }

    public Double[] getPostion() {
        return postion;
    }

    public void setPostion(Double[] postion) {
        this.postion = postion;
    }
}
