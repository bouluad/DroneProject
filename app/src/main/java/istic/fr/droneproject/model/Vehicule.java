package istic.fr.droneproject.model;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Modele d'un vehicule
 * Les heures sont celles presentes dans le tableau des moyens
 */
public class Vehicule {
    public String _id;
    public String nom;
    public TypeVehicule type;
    public EtatVehicule etat;
    public String heureDemande;
    public String heureEngagement;
    public String heureArrivee;
    public String heureLiberation;
    public Categorie categorie;
    public Double[] position;

    /**
     * @return si le vehicule peut prendre l'etat DEMANDE
     */
    public boolean peutEtreDemande() {
        return etat == null;
    }

    /**
     * @return si le vehicule peut prendre l'etat ANNULE
     */
    public boolean peutEtreAnnule() {
        return etat == EtatVehicule.DEMANDE;
    }

    /**
     * @return si le vehicule peut prendre l'etat ENGAGE
     */
    public boolean peutEtreEngage() {
        switch (etat) {
            case DEMANDE:
                return true;
            case ENGAGE:
                return true;
            case ARRIVE:
                return true;
            case LIBERE:
                return false;
            case ANNULE:
                return false;
            case PARKING:
                return true;
        }
        return false;
    }

    /**
     * @return si le vehicule peut prendre l'etat ARRIVE
     */
    public boolean peutEtreArrive() {
        return etat == EtatVehicule.ENGAGE && positionValide(position);
    }

    /**
     * @return si le vehicule peut prendre l'etat LIBERE
     */
    public boolean peutEtreLibere() {
        switch (etat) {
            case DEMANDE:
                return true;
            case ENGAGE:
                return true;
            case ARRIVE:
                return true;
            case LIBERE:
                return false;
            case ANNULE:
                return false;
            case PARKING:
                return true;
        }
        return false;
    }

    /**
     * @return si le vehicule peut prendre l'etat PARKING
     */
    public boolean peutEtreParking() {
        return etat == null //Premier depart, pas encore d'etat
                || etat == EtatVehicule.DEMANDE
                || etat == EtatVehicule.ENGAGE
                || etat == EtatVehicule.ARRIVE
                ;
    }

    /**
     * Change l'etat du vehicule a DEMANDE si c'est autorise
     * Met l'heure de demande a l'heure courante
     *
     * @return true si le changement a ete effectue
     */
    public boolean demander() {
        if (peutEtreDemande()) {
            etat = EtatVehicule.DEMANDE;
            heureDemande = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
            return true;
        }
        return false;
    }

    /**
     * Change l'etat du vehicule a ANNULE si c'est autorise
     *
     * @return true si le changement a ete effectue
     */
    public boolean annuler() {
        if (peutEtreAnnule()) {
            etat = EtatVehicule.ANNULE;
            return true;
        }
        return false;
    }

    /**
     * Change l'etat du vehicule a ENGAGE si c'est autorise
     * Met l'heure d'engagement a l'heure courante si c'est le premier engagement du vehicule
     *
     * @return true si le changement a ete effectue
     */
    public boolean engager(Double[] nouvellePosition) {
        if (peutEtreEngage() && positionValide(nouvellePosition)) {
            etat = EtatVehicule.ENGAGE;
            position = nouvellePosition;
            if (heureEngagement == null) {
                heureEngagement = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
            }
            return true;
        }
        return false;
    }

    /**
     * Change l'etat du vehicule a PARKING si c'est autorise
     * Efface la position pour assurer la coherence du modele
     * Met l'heure d'engagement a l'heure courante si elle n'existe pas encore (cas de validation de demande ou premier moyen)
     *
     * @return true si le changement a ete effectue
     */
    public boolean parking() {
        if (peutEtreParking()) {
            etat = EtatVehicule.PARKING;
            position = null;
            if (heureEngagement == null) {
                heureEngagement = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
            }
            return true;
        }
        return false;
    }

    /**
     * Change l'etat du vehicule a ARRIVE si c'est autorise
     * Met l'heure d'arrivee a l'heure courante si c'est la premiere confirmation de position du vehicule
     *
     * @return true si le changement a ete effectue
     */
    public boolean arriver() {
        if (peutEtreArrive()) {
            etat = EtatVehicule.ARRIVE;
            if (heureArrivee == null) {
                heureArrivee = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
            }
            return true;
        }
        return false;
    }

    /**
     * Change l'etat du vehicule a LIBERE si c'est autorise
     * Met l'heure de liberation a l'heure courante
     *
     * @return true si le changement a ete effectue
     */
    public boolean liberer() {
        if (peutEtreLibere()) {
            etat = EtatVehicule.LIBERE;
            heureLiberation = new SimpleDateFormat("HH:mm", Locale.FRANCE).format(new Date());
            return true;
        }
        return false;
    }

    /**
     * Change l'etat du vehicule a PARKING si la demande est autorisee
     * Met l'heure de demande a l'heure courante
     * Met l'heure d'engagement a l'heure courante
     *
     * @return true si le changement a ete effectue
     */
    public boolean creerParCodis() {
        if (peutEtreDemande()) {
            demander();
            parking();
            return true;
        }
        return false;
    }

    /**
     * @return si la position du vehicule est inconnue ou invalide
     */
    public static boolean positionValide(Double[] position) {
        return position != null && position.length == 2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vehicule) {

            Vehicule v = (Vehicule) obj;

            return stringEquals(v.nom, nom)
                    && v.type == type
                    && v.categorie == categorie
                    && v.etat == etat
                    && Arrays.equals(v.position, position)
                    && stringEquals(v.heureArrivee, heureArrivee)
                    && stringEquals(v.heureEngagement, heureEngagement)
                    && stringEquals(v.heureDemande, heureDemande)
                    && stringEquals(v.heureLiberation, heureLiberation)
                    ;
        } else {
            return false;
        }
    }

    private boolean stringEquals(String f1, String f2) {
        if (f1 == null) {
            return f2 == null;
        } else {
            return f1.equals(f2);
        }
    }
}
