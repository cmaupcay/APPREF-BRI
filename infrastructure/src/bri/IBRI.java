package bri;

/**
 * Interface commune aux applications serveurs et aux modes du client.
 * Permet le partage du nom et du port de l'application.
 */
public interface IBRI 
{
    /**
     * Retourne le nom du mode de connexion.
     * @return Nom commun du mode.
     */
    public String nom();
    /**
     * Retourne le port associé au mode.
     * @return Port du mode.
     */
    public int port();
}