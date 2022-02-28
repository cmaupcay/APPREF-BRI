package bri.serveur.app.session.actions;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IService;
import bri.serveur.IUtilisateur;
import bri.serveur.Services;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

public class SuppressionService extends Action
{
    @Override
    public final String nom() { return "Supprimer un service "; }

    public SuppressionService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        try
        {
            if (arguments.length < 1) return false;
            IUtilisateur auteur = Utilisateurs.utilisateur(arguments[0]);
            if (auteur == null) return false;
            final Object[] services = Services.services_publies(auteur).toArray();
            final int s = connexion.demander_choix(services, "Quel service souhaitez-vous supprimer ?");
            connexion.ecrire(Connexion.VRAI);
            if (s == services.length) return true;
            final IService service = (IService)services[s];
            if (service.actif())
                if (!this.controle_activite_service(connexion, arguments[0], s)) return true;
            if (!Services.supprimer(auteur, service.nom()))
            {
                connexion.ecrire("ERREUR : La suppression du service a échoué.");
                return false;
            }
            connexion.ecrire("Service supprimé !");
            return true;
        }
        catch (IOException e)
        { return false; }
    }
}
