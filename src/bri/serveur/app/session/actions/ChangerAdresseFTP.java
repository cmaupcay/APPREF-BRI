package bri.serveur.app.session.actions;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

public class ChangerAdresseFTP extends Action
{
    @Override
    public final String nom() { return "Changer votre adresse FTP"; }

    public ChangerAdresseFTP(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        if (arguments.length < 1) return false;
        IUtilisateur utilisateur = null;
        for (IUtilisateur u : Utilisateurs.liste())
            if (u.pseudo().equals(arguments[0]))
                utilisateur = u;
        if (utilisateur == null) return false;
        connexion.ecrire("Adresse FTP actuelle : " + utilisateur.ftp());
        try 
        {
            final String ftp = connexion.demander("Nouvelle adresse FTP : ");
            utilisateur.modifier_ftp(ftp);
            connexion.ecrire(Connexion.VRAI);
            return true;
        }
        catch (IOException e) { return false; }
    }
}
