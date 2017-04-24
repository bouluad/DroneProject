package istic.fr.droneproject.model;

import java.util.List;

/**
 * Created by yousra on 24/04/17.
 */
public class Segment {


    List<Double[]> points;
    Boolean boucleFermee;

    public Boolean getBoucleFermee() {
        return boucleFermee;
    }

    public void setBoucleFermee(Boolean boucleFermee) {
        this.boucleFermee = boucleFermee;
    }

    public List<Double[]> getPoints() {
        return points;
    }

    public void setPoints(List<Double[]> points) {
        this.points = points;
    }
}
