package bri.serveur.utilisateur;

import java.io.IOException;

import bri.Connexion;

/**
 * Classe des utilisateurs programmeurs.
 */
public class Programmeur extends Utilisateur
{
    /** Adresse du serveur FTP. */
    private String ftp;
    @Override
    public final String ftp() { return this.ftp; }
    @Override
    public void modifier_ftp(final String ftp) { this.ftp = ftp; }

    /**
     * Construction d'un nouvel utilisateur programmeur.
     * @param pseudo Pseudo de l'utilisateur.
     * @param mdp Mot de passe de l'utilisateur.
     * @param ftp Adresse FTP du programmeur.
     */
    public Programmeur(final String pseudo, final String mdp, final String ftp)
    { 
        super(pseudo, mdp);
        this.ftp = ftp;
    }
    /**
     * Construction d'un nouvel utilisateur de manière interactive depuis une connexion.
     * @param connexion Connexion cliente.
     * @throws IOException Impossible de lire les informations de la connexion.
     */
    public Programmeur(Connexion connexion) throws IOException
    {
        super(connexion);
        this.ftp = connexion.demander("URL du serveur FTP : ");
        connexion.ecrire(Connexion.VRAI);
    }
}
