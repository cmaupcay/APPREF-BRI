package bri.serveur.utilisateur;

import java.io.IOException;

import bri.Connexion;

public class Programmeur extends Utilisateur
{
    private String ftp;
    @Override
    public final String ftp() { return this.ftp; }
    @Override
    public void modifier_ftp(final String ftp) { this.ftp = ftp; }


    public Programmeur(final String pseudo, final String mdp, final String ftp)
    { 
        super(pseudo, mdp);
        this.ftp = ftp;
    }

    public Programmeur(Connexion connexion) throws IOException
    {
        super(connexion);
        this.ftp = connexion.demander("URL du serveur FTP : ");
    }
}
