package bri.serveur;

/**
 * Interface des utilisateurs BRI.
 */
public interface IUtilisateur 
{
    /**
     * Retourne le pseudo de l'utilisateur.
     * @return Pseudo de l'utilisateur.
     */
    public String pseudo();
    /**
     * Retourne le mot de passe de l'utilisateur.
     * @return Mot de passe de l'utilisateur.
     */
    public String mdp();

    /**
     * Retourne l'adresse FTP de l'utilisateur.
     * @return Adresse FTP de l'utilisateur.
     */
    public String ftp();
    /**
     * Modification de l'adresse FTP de l'utilisateur.
     * @param ftp Nouvelle adresse FTP.
     */
    public void modifier_ftp(final String ftp);
}
