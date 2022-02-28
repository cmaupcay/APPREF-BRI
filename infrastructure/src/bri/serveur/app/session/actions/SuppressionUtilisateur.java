package bri.serveur.app.session.actions;

import java.io.IOException;

import bri.Connexion;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

public class SuppressionUtilisateur extends Action
{
    @Override
    public final String nom() { return "Supprimer un utilisateur"; }

    public SuppressionUtilisateur(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        try
        {
            final int utilisateur = connexion.demander_choix(Utilisateurs.liste().toArray(), "Quel utilisateur souhaitez-vous supprimer ?");
            connexion.ecrire(Connexion.VRAI);
            if (utilisateur == Utilisateurs.liste().size()) return true;
            Utilisateurs.supprimer(Utilisateurs.liste().get(utilisateur).pseudo());
            connexion.ecrire("Utilisateur supprimé !");
            return true;
        }
        catch (IOException e)
        { return false; }
    }
}
