package bri.serveur.app.session.actions;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IService;
import bri.serveur.IUtilisateur;
import bri.serveur.Services;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

/**
 * Action de suppression d'un service.
 */
public class SuppressionService extends Action
{
    @Override
    public final String nom() { return "Supprimer un service "; }

    /**
     * Construction de l'action.
     * @param parent Session parente.
     */
    public SuppressionService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        try
        {
            // Requiert le pseudo de l'utilisateur en premier argument.
            if (arguments.length < 1) return false;
            IUtilisateur auteur = Utilisateurs.utilisateur(arguments[0]);
            if (auteur == null) return false;
           
            final Object[] services = Services.services_publies(auteur).toArray();
            int s = -1;
            // Si l'argument est défini, récupération de l'index donné.
            if (arguments.length > 1) s = Integer.parseInt(arguments[1]);
            else
            {
                // Choix du service cible dans la liste des services publiés.
                s = connexion.demander_choix(services, "Quel service souhaitez-vous supprimer ?");
                connexion.ecrire(Connexion.VRAI);
                if (s == services.length) return true;    
            }
            // Récupération du service dans la liste des services publiés.
            final IService service = (IService)services[s];
            
            /**
             * Pour éviter l'instanciation du service pendant la suppression,
             * le service doit être désactivé.
             */
            if (service.actif())
                // Si le service est actif, on lance une action de contrôle de l'activité pour le désactiver.
                if (!this.controle_activite_service(connexion, arguments[0], s)) return true; // Si l'utilisateur refuse la désactivation, on s'arrête.

            // Suppression du service cible.
            if (Services.supprimer(auteur, service.nom())) connexion.ecrire("Service supprimé !");
            else connexion.ecrire("| ERREUR | La suppression du service a échoué.");
            return true;
        }
        catch (IOException e)
        { return false; }
    }
}
