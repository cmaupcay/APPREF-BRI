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
            final String choix = connexion.demander("Activer le service (o/n) ? ");
            connexion.ecrire(Connexion.VRAI);
            if (choix.toLowerCase().equals("o"))
            {
                if (Services.services().get(Services.services().size() - 1).activer())
                    connexion.ecrire("Service activé !");
                else connexion.ecrire("ERREUR : Impossible d'activer le service. Veuillez vérifiez le nom de la classe.");
            }
            return true;

        }
        catch (IOException e)
        { return false; }
    }
}
