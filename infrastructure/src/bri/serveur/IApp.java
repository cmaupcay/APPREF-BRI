package bri.serveur;

import bri.IBRI;

/**
 * Interface des applications serveurs BRI.
 */
public interface IApp extends Runnable, IBRI
{
    /**
     * Retourne le thread d'exécution de l'application.
     * @return
     */
    public Thread thread();
    /**
     * Démarrage de l'application.
     */
    public void demarrer();
    /**
     * Retourne le drapeau d'acceptation de nouvelle connexion.
     * @return Valeur du drapeau d'acceptation.
     */
    public boolean accepter_nouvelle_connexion();
}
