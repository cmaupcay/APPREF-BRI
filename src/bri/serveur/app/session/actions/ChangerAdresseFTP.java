package bri.serveur.app.session.actions;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IService;
import bri.serveur.IUtilisateur;
import bri.serveur.Services;
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
        IUtilisateur utilisateur = Utilisateurs.utilisateur(arguments[0]);
        if (utilisateur == null) return false;
        connexion.ecrire("Adresse FTP actuelle : " + utilisateur.ftp());
        try 
        {
            final String ftp = connexion.demander("Nouvelle adresse FTP : ");
            utilisateur.modifier_ftp(ftp);
            connexion.ecrire(Connexion.VRAI);
            connexion.ecrire("Mise à jour des classes de services...");
            for (IService service : Services.services_publies(utilisateur))
                if (!service.mettre_a_jour())
                    connexion.ecrire("ERREUR : Impossible de mettre à jour le service " + service.nom() + ".");
            return true;
        }
        catch (IOException e) { return false; }
    }
}
