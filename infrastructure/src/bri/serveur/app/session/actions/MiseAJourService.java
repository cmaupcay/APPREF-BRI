package bri.serveur.app.session.actions;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IService;
import bri.serveur.IUtilisateur;
import bri.serveur.Services;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

public class MiseAJourService extends Action
{
    @Override
    public final String nom() { return "Mettre à jour un service"; }

    public MiseAJourService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        try
        {
            if (arguments.length < 1) return false;
            IUtilisateur auteur = Utilisateurs.utilisateur(arguments[0]);
            if (auteur == null) return false;
            final Object[] services = Services.services_publies(auteur).toArray();
            final int s = connexion.demander_choix(services, "Quel service souhaitez-vous mettre à jour ?");
            connexion.ecrire(Connexion.VRAI);
            final IService service = (IService)services[s];
            boolean activer = false;
            if (service.actif())
            {
                if (!this.controle_activite_service(connexion, arguments[0], s)) return true;
                activer = true;
            }
            if (!service.actif())
            {
                if (!service.mettre_a_jour())
                    connexion.ecrire("ERREUR : La mise à jour du service a échoué.");
                else connexion.ecrire("Service mis à jour !");
                if (activer) this.controle_activite_service(connexion, arguments[0], s);
            }
            return true;
        }
        catch (IOException e)
        { return false; }
    }
}