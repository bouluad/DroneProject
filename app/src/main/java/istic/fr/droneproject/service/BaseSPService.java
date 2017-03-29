package istic.fr.droneproject.service;

import android.graphics.Point;

import java.util.List;

import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.model.PointInteret;
import retrofit2.Callback;

/**
 * Created by nirina on 29/03/17.
 */

public interface BaseSPService {
    /**
     * Récupérer la liste des points d'intérêt
     * @param callback Callback pour récupérer la réponse
     */
    void getBaseSP(Callback<List<PointInteret>> callback);

    /**
     * Ajouter un nouveau point d'intérêt
     * @param pointinteret nouveau point d'intérêt
     * @param callback Callback pour récupérer la réponse
     */
    void addNouveauPointInteret(PointInteret pointinteret, Callback<Void> callback);

}
