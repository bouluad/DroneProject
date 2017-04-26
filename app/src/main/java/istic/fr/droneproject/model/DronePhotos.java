package istic.fr.droneproject.model;

import java.util.List;

/**
 * Modèle des photos prise par le drone
 * idIntervention c'est l'ID d'intervention de travail
 * photos la liste des photos proche de cette intervention
 * video lien vers video streaming
 */

public class DronePhotos {

    private String idIntervention;
    private List<Photo> photos;
    private String video;

    public String getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(String idIntervention) {
        this.idIntervention = idIntervention;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
