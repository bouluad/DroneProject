package istic.fr.droneproject.model;

/**
 * Modèle d'un point d'intérêt
 * Le code_image représente la nature du point d'intérêt
 */
public class PointInteret {
    public String code_image;
    public Double[] position;

    public Double[] getPosition() {
        return position;
    }

    public void setPosition(Double[] position) {
        this.position = position;
    }

    public String getCode_image() {
        return code_image;
    }

    public void setCode_image(String code_image) {
        this.code_image = code_image;
    }
}
