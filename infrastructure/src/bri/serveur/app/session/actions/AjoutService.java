package bri.serveur.app.session.actions;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.IUtilisateur;
import bri.serveur.Services;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

public class AjoutService extends Action
{
    @Override
    public final String nom() { return "Ajouter un nouveau service"; }

    public AjoutService(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    { 
        try
        {
            if (arguments.length < 1) return false;
            IUtilisateur auteur = Utilisateurs.utilisateur(arguments[0]);
            if (auteur == null) return false;
            while (!Services.ajouter(auteur, connexion.demander("Nom du service : ")))
            {
                connexion.ecrire(Connexion.FAUX);
                connexion.ecrire("Un service porte déjà ce nom !");
            }
            connexion.ecrire(Connexion.VRAI);
            connexion.ecrire("Service ajouté !");
            this.controle_activite_service(connexion, arguments[0], Services.services().size() - 1);
            return true;

        }
        catch (IOException e)
        { return false; }
    }
}
