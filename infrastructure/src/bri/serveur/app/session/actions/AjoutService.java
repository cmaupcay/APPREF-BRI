package bri.serveur.app.session.actions;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IUtilisateur;
import bri.serveur.Services;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

/**
 * Action d'ajout d'un nouveau service.
 */
public class AjoutService extends Action
{
    @Override
    public final String nom() { return "Ajouter un nouveau service"; }

    /**
     * Construction de l'action.
     * @param parent Session parente.
     */
    public AjoutService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    { 
        try
        {
            // Requiert le pseudo de l'utilisateur authentifié en premier argument.
            if (arguments.length < 1) return false;
            IUtilisateur auteur = Utilisateurs.utilisateur(arguments[0]);
            if (auteur == null) return false;

            // Boucle de choix du nom du service à ajouter.
            while (!Services.ajouter(auteur, connexion.demander("Nom du service : ")))
            {
                connexion.ecrire(Connexion.FAUX);
                connexion.ecrire("Un service porte déjà ce nom !");
            }
            connexion.ecrire(Connexion.VRAI);
            connexion.ecrire("Service ajouté !");
            // Activation du nouveau service.
            this.controle_activite_service(connexion, arguments[0], Services.services().size() - 1);
            return true;

        }
        catch (IOException e)
        { return false; }
    }
}
