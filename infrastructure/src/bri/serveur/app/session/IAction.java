package bri.serveur.app.session;

import bri.Connexion;

/**
 * Interface des actions de session.
 */
public interface IAction 
{
    /**
     * Retourne le nom d'affichage de l'action de session.
     * @return Nom de l'action.
     */
    public String nom();
    /**
     * Exécution de l'action de manière interactive avec la connexion cliente, et selon des arguments passés en paramètre.
     * @param connexion Connexion cliente ouverte et connectée, la même que la session.
     * @param arguments Arguments d'exécution de l'action. Permet le transfert d'information depuis la session ou l'exécution partielle de l'action.
     * @return Indique si la session doit continuer ou non.
     */
    public boolean executer(Connexion connexion, String[] arguments);
}
