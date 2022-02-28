package bri.serveur;

import bri.Connexion;
import bri.serveur.service.IServiceBRI;

/**
 * Interface des conteneur de service BRI.
 */
public interface IService
{
    /**
     * Retourne le nom du service.
     * @return Nom du service.
     */
    public String nom();
    /**
     * Retourne l'utilisateur auteur du service.
     * @return Auteur du service.
     */
    public IUtilisateur auteur();
    /**
     * Retourne le nom de la classe du service.
     * @return Nom de la classe du service.
     */
    public String classe();

    /**
     * Retourne la valeur du drapeau d'activité du service.
     * @return Drapeau d'activité du service.
     */
    public boolean actif();
    /**
     * Activation du service.
     * @return Indique si la procédure à réussi.
     */
    public boolean activer();
    /**
     * Désactivation du service.
     * @return Indique si la procédure à réussi.
     */
    public boolean desactiver();

    /**
     * Mise à jour du service depuis sa source distante.
     * @return Indique si la procédure à réussi.
     */
    public boolean mettre_a_jour();

    /**
     * Création d'une nouvelle instance du service contenu.
     * @param connexion Connexion cliente.
     * @return Nouvelle instance du service.
     */
    public IServiceBRI nouvelle_instance(Connexion connexion);
}
