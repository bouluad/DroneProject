package istic.fr.droneproject.service;

import java.util.List;

import istic.fr.droneproject.model.Intervention;
import istic.fr.droneproject.service.impl.InterventionServiceImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO Supprimer cette classe quand les vrais services seront implémentés
public class ExempleUtilisationService {

    //TODO ATTENTION CECI EST UNE CLASSE D'EXEMPLE POUR RETROFIT

    public void utilisation() {

        /*
        Ici on voit comment utiliser le service d'exemple
        Cette classe peut être une activité, un fragment, etc.
        Cela permet d'appeler un service et dans le callback, de modifier l'interface graphique avec le résultat de l'appel
         */

        Intervention interventionAEnvoyer = new Intervention();

        InterventionService service = new InterventionServiceImpl();
        service.getListeInterventions(new Callback<List<Intervention>>() {
            @Override
            public void onResponse(Call<List<Intervention>> call, Response<List<Intervention>> response) {
                /*
                Code à exécuter quand l'appel REST a été bien fait
                Modifier l'interface graphique par exemple
                 */
            }

            @Override
            public void onFailure(Call<List<Intervention>> call, Throwable t) {
                /*
                Code à exécuter quand l'appel REST a échoué
                 */
            }
        });

    }
}
