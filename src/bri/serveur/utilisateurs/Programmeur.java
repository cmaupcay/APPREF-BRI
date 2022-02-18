package bri.serveur.utilisateurs;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.Utilisateurs;

public class Programmeur extends Utilisateur
{
    public String ftp;
    @Override
    public final String ftp() { return this.ftp; }

    public Programmeur(final String pseudo, final String mdp, final String ftp)
    { 
        super(pseudo, mdp);
        this.ftp = ftp;
    }

    public Programmeur(Connexion connexion) throws IOException
    {
        super(connexion);
        this.ftp = connexion.demander("URL du serveur FTP (adresse:port) : ");
        connexion.ecrire(Connexion.VRAI);
    }
}
