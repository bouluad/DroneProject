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
    void addNouvelleIntervention(Intervention intervention, Callback<Void> callback);

}
