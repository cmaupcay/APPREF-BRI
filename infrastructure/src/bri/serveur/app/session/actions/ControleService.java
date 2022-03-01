package bri.serveur.app.session.actions;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IService;
import bri.serveur.IUtilisateur;
import bri.serveur.Services;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

/**
 * Action de contrôle de l'activité d'un service.
 */
public class ControleService extends Action
{
    @Override
    public final String nom() { return "Démarrer/Arrêter un service"; }

    /**
     * Construction de l'action.
     * @param parent Session parente.
     */
    public ControleService(ISession parent) { super(parent); }

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
                s = connexion.demander_choix(services, "De quel service souhaitez-vous modifier l'état ?");
                connexion.ecrire(Connexion.VRAI);
                if (s == services.length) return true;
            } 
            // Récupération du service dans la liste des services publiés.
            final IService service = (IService)services[s];

            // Définition des messages à afficher selon l'activité actuelle du service cible.
            String action = "", succes = "", erreur = "";
            if (service.actif())
            {
                connexion.ecrire("ATTENTION : La désactivation d'un service actif ne supprime pas les sessions actives utilisant ce service.");
                action = "Désactiver le service ";
                succes = "Service désactivé. ";
                erreur = "Impossible de désactiver le service.";
            }
            else 
            {
                action = "Activer le service ";
                succes = "Service activé.";
                erreur = "Impossible d'activer le service.";
            }

            boolean continuer = true;
            String choix = "";
            // Boucle de choix de lancement de l'action.
            while (continuer)
            {
                choix = connexion.demander(action + service.nom() + " (o/n) ? ").toLowerCase();
                if (choix.equals("o"))
                {
                    // Lancement de l'action
                    connexion.ecrire(Connexion.VRAI);
                    boolean resultat = true;
                    // Si le service cible est actif, on le désactivera. S'il est inactif, on l'activera.
                    if (service.actif()) resultat = service.desactiver();
                    else resultat = service.activer();
                    // Envoi du résultat au client.
                    if (resultat) connexion.ecrire(succes);
                    else connexion.ecrire("| ERREUR | " + erreur);
                    continuer = false;
                }
                else if (choix.equals("n"))
                {
                    // Annulation de l'action
                    connexion.ecrire(Connexion.VRAI);
                    continuer = false;
                }
                else connexion.ecrire(Connexion.FAUX); // Réponse incohérente.
            }
            return true;

        }
        catch (IOException e)
        { return false; }
    }
}
