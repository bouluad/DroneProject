package istic.fr.droneproject.service;

import java.util.List;

import istic.fr.droneproject.model.Intervention;
import retrofit2.Callback;

public interface InterventionService {

    /**
     * Récupérer la liste des interventions
     * @param callback Callback pour récupérer la réponse
     */
    void getListeInterventions(Callback<List<Intervention>> callback);

    /**
     * Ajouter une nouvelle intervention
     * @param intervention Nouvelle intervention
     * @param callback Callback pour récupérer la réponse
     */
    void addNouvelleIntervention(Intervention intervention, Callback<String> callback);

    /**
     * Récupérer une intervention by id
     * @param callback Callback pour récupérer la réponse
     */

    void getInterventionById(String id,Callback<Intervention> callback);

    /***
     * Mise à jour d'une Intervention
     * @param intervention
     * @param callback
     */
    void updateIntervention(Intervention intervention, Callback<Void> callback);

}
