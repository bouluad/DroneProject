package istic.fr.droneproject.model;

import java.util.List;

/**
 * Created by yousra on 24/04/17.
 */
public class Zone {

    List<Double[]> contours;

    List<List<Double[]>> exclusion;

    public List<Double[]> getContours() {
        return contours;
    }

    public void setContours(List<Double[]> contours) {
        this.contours = contours;
    }
}

