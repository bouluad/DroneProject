package istic.fr.droneproject.model;

import java.util.List;

/**
 * Created by yousra on 24/04/17.
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
