package bri.serveur.app.session.actions;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IService;
import bri.serveur.IUtilisateur;
import bri.serveur.Services;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

/**
 * Action de mise à jour d'un service.
 */
public class MiseAJourService extends Action
{
    @Override
    public final String nom() { return "Mettre à jour un service"; }

    /**
     * Construction de l'action.
     * @param parent Session parente.
     */
    public MiseAJourService(ISession parent) { super(parent); }

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
                s = connexion.demander_choix(services, "Quel service souhaitez-vous mettre à jour ?");
                connexion.ecrire(Connexion.VRAI);
                if (s == services.length) return true;
            } 
            // Récupération du service dans la liste des services publiés.
            final IService service = (IService)services[s];
         
            /**
             * Pour éviter l'instanciation du service pendant la mise à jour de la classe de service,
             * le service doit être désactivé pour être mis à jour.
             */
            boolean activer = false; // Défini si le service doit être réactiver après la mise à jour.
            if (service.actif())
            {
                // Si le service est actif, on lance une action de contrôle de l'activité pour le désactiver.
                if (!this.controle_activite_service(connexion, arguments[0], s)) return true; // Si l'utilisateur refuse la désactivation, on s'arrête.
                else activer = true; // Sinon, on réactivera le service après la mise à jour.
            }

            // Mise à jour du service.
            if (service.mettre_a_jour()) connexion.ecrire("Service mis à jour !");
            else connexion.ecrire("| ERREUR | La mise à jour du service a échoué.");
            
            // Si besoin, réactivation du service.
            if (activer) this.controle_activite_service(connexion, arguments[0], s);
            return true;
        }
        catch (IOException e)
        { return false; }
    }
}
