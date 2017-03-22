package istic.fr.droneproject.service;

import istic.fr.droneproject.model.User;
import retrofit2.Callback;

public interface UserService {

    /**
     * Demande vérification des infos du User pour se connecter
     *
     * @param user     Utilisateur
     * @param callback Callback pour récupérer la réponse du serveur
     *                 200 Si ok
     *                 404 Sinon
     */
    void login(User user, Callback<Void> callback);
}
