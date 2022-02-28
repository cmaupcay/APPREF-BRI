package bri.serveur.app;

import java.io.IOException;
import bri.Connexion;
import bri.serveur.IApp;

/**
 * Interface des sessions d'application serveur.
 */
public interface ISession extends Runnable
{
    /**
     * Initialisation de la session.
     * @param parent Application parente.
     * @param connexion Connexion cliente.
     */
    public void initialiser(IApp parent, Connexion connexion);
    /**
     * Fermeture de la session et de la connexion.
     * @throws IOException Impossible de fermer la connexion.
     */
    public void fermer() throws IOException;
    /**
     * Retourne le thread d'exécution de la session.
     * @return Thread propre à la session.
     */
    public Thread thread();
    /**
     * Retourne l'application parente de la session.
     * @return Application serveur à l'origine de la session.
     */
    public IApp parent();
}
