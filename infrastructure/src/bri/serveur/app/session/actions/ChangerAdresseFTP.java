package bri.serveur.app.session.actions;

import java.io.IOException;
import java.util.List;

import bri.Connexion;
import bri.serveur.IService;
import bri.serveur.IUtilisateur;
import bri.serveur.Services;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

/**
 * Action de changement de l'adresse FTP d'un utilisateur.
 */
public class ChangerAdresseFTP extends Action
{
    @Override
    public final String nom() { return "Changer votre adresse FTP"; }

    /**
     * Construction de l'action.
     * @param parent Session parente.
     */
    public ChangerAdresseFTP(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        // Requiert le pseudo de l'utilisateur en premier argument.
        if (arguments.length < 1) return false;
        IUtilisateur utilisateur = Utilisateurs.utilisateur(arguments[0]);
        if (utilisateur == null) return false;
        // Affichage de l'adresse FTP actuellement enregistrée.
        connexion.ecrire("Adresse FTP actuelle : " + utilisateur.ftp());
        try 
        {
            // Demande de la nouvelle adresse FTP.
            final String ftp = connexion.demander("Nouvelle adresse FTP : ");
            if (ftp == null)
            {
                connexion.ecrire(Connexion.FAUX);
                return false;
            }
            // Modification de l'adresse de l'utilisateur.
            utilisateur.modifier_ftp(ftp);
            connexion.ecrire(Connexion.VRAI);
            // Mise à jour des services publiés par l'utilisateur.
            connexion.ecrire("Mise à jour des classes de services...");
            final List<IService> services = Services.services_publies(utilisateur);
            final int n = services.size();
            for (int s = 0; s < n; s++)
                (new MiseAJourService(this.parent())).executer(connexion, new String[]{ arguments[0], Integer.toString(s) });
            return true;
        }
        catch (IOException e) { return false; }
    }
}
