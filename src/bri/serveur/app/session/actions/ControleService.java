package bri.serveur.app.session.actions;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IService;
import bri.serveur.IUtilisateur;
import bri.serveur.Services;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

public class ControleService extends Action
{
    @Override
    public final String nom() { return "Démarrer/Arrêter un service"; }

    public ControleService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        try
        {
            if (arguments.length < 1) return false;
            IUtilisateur auteur = Utilisateurs.utilisateur(arguments[0]);
            if (auteur == null) return false;
            final Object[] services = Services.services_publies(auteur).toArray();
            final int s = connexion.demander_choix(services, "De quel service souhaitez-vous modifier l'état ?");
            connexion.ecrire(Connexion.VRAI);
            final IService service = (IService)services[s];
            String action = "", erreur = "";
            if (service.actif())
            {
                connexion.ecrire("ATTENTION : La désactivation d'un service actif ne supprime pas les sessions actives utilisant ce service.");
                action = "Désactiver le service ";
                erreur = "Impossible de désactiver le service.";
            }
            else 
            {
                action = "Activer le service ";
                erreur = "Impossible d'activer le service.";
            }
            final String choix = connexion.demander(action + service.nom() + " (o/n) ? ");
            connexion.ecrire(Connexion.VRAI);
            if (choix.toLowerCase().equals("o"))
            {
                boolean resultat = true;
                if (service.actif()) resultat = service.desactiver();
                else resultat = service.activer();
                if (!resultat) connexion.ecrire("ERREUR : " + erreur);
            }
            return true;

        }
        catch (IOException e)
        { return false; }
    }
}
