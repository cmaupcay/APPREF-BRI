package bri.serveur.app.session.actions;

import java.io.IOException;
import java.util.List;

import bri.Connexion;
import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;
import bri.serveur.app.ISession;

/**
 * Action de suppression d'un utilisateur.
 */
public class SuppressionUtilisateur extends Action
{
    @Override
    public final String nom() { return "Supprimer un utilisateur"; }

    /**
     * Construction de l'action.
     * @param parent Session parente.
     */
    public SuppressionUtilisateur(ISession parent) { super(parent); }

    @Override
    public final boolean executer(Connexion connexion, String[] arguments)
    {
        try
        {
            // Requiert le pseudo de l'utilisateur authentifié en premier argument.
            if (arguments.length < 1) return false;
            IUtilisateur auteur = Utilisateurs.utilisateur(arguments[0]);
            if (auteur == null) return false;

            List<IUtilisateur> utilisateurs = Utilisateurs.liste();
            int u = -1;
            // Si l'argument est défini, récupération de l'index donné.
            if (arguments.length > 1) u = Integer.parseInt(arguments[1]);
            else
            {
                // Choix de l'utilisateur cible dans la liste des utilisateurs.
                u = connexion.demander_choix(utilisateurs.toArray(), "Quel utilisateur souhaitez-vous supprimer ?");
                connexion.ecrire(Connexion.VRAI);
                if (u == utilisateurs.size()) return true;
            }
            final String pseudo = utilisateurs.get(u).pseudo();
            if (Utilisateurs.supprimer(pseudo))
            {
                connexion.ecrire("Utilisateur supprimé !");
                // Si l'utilisateur supprimé est l'utilisateur courant, on quitte la session.
                if (pseudo.equals(auteur.pseudo())) return this.quitter_session(connexion);
            }
            else connexion.ecrire("| ERREUR | Impossible de supprimer l'utilisateur \"" + pseudo + "\" .");
            return true;
        }
        catch (IOException e)
        { return false; }
    }
}
