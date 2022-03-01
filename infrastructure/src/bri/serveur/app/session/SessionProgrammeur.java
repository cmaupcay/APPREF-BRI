package bri.serveur.app.session;

import java.io.IOException;
import java.util.List;

import bri.Connexion;
import bri.serveur.Console;
import bri.serveur.IUtilisateur;
import bri.serveur.Utilisateurs;
import bri.serveur.app.session.actions.AjoutService;
import bri.serveur.app.session.actions.AjoutUtilisateur;
import bri.serveur.app.session.actions.ChangerAdresseFTP;
import bri.serveur.app.session.actions.ControleService;
import bri.serveur.app.session.actions.MiseAJourService;
import bri.serveur.app.session.actions.Quitter;
import bri.serveur.app.session.actions.SuppressionService;
import bri.serveur.app.session.actions.SuppressionUtilisateur;
import bri.serveur.utilisateur.Programmeur;

/**
 * Session de l'application destinée aux programmeurs.
 */
public class SessionProgrammeur extends Session
{
    /** Nombre de tentatives de connexion autorisées pour une connexion. */
    private static final int TENTATIVES_MAX = 3;
    /** Utilisateur authentifié dans la session. */
    private IUtilisateur utilisateur;

    /**
     * Vérification du mot de passe de l'utilisateur authentifié.
     * @param mdp Mot de passe à vérifier.
     * @return Indique si les mots de passe sont similaires.
     */
    private final boolean verifier_mdp(final String mdp)
    { return this.utilisateur.mdp().equals(mdp); }

    /**
     * Ajout des actions de session à la liste des actions.
     */
    private final void charger_actions()
    {
        this.actions().add(new AjoutService(this));
        this.actions().add(new MiseAJourService(this));
        this.actions().add(new ControleService(this));
        this.actions().add(new SuppressionService(this));
        this.actions().add(new ChangerAdresseFTP(this));
        this.actions().add(new AjoutUtilisateur(this));
        this.actions().add(new SuppressionUtilisateur(this));
    }

    @Override
    public final void run()
    {
        try
        {
            // AUTHENTIFICATION
            final String pseudo = this.connexion().demander("Utilisateur : ");
            this.utilisateur = Utilisateurs.utilisateur(pseudo);
            if (this.utilisateur == null) this.connexion().ecrire(Connexion.FAUX); // Le pseudo n'existe pas
            else
            {
                this.connexion().ecrire(Connexion.VRAI);
                int tentatives = 0;
                // Boucle de tentative de connexion.
                while (tentatives++ < TENTATIVES_MAX)
                {
                    if (this.verifier_mdp(this.connexion().demander("Mot de passe : "))) // Vérification du mot de passe.
                    {
                        // Authentification réussi.
                        tentatives = TENTATIVES_MAX;
                        this.connexion().ecrire(Connexion.VRAI);
                        Console.afficher(this.parent(), "Nouvelle connexion : " + pseudo + ".");

                        this.charger_actions();
                        int action;
                        boolean continuer = true;
                        // Boucle de choix de l'action à effectuer.
                        while (continuer)
                        {
                            action = this.connexion().demander_choix(this.actions().toArray(), "Que voulez-vous faire ?");
                            if (action < this.actions().size())
                            {
                                // Lancement de l'action de session dans la session.
                                this.connexion().ecrire(Connexion.VRAI);
                                continuer = this.actions().get(action).executer(this.connexion(), new String[]{
                                    pseudo // Les actions de programmeur attendent le pseudo de l'utilisateur en premier argument.
                                });
                            }
                            else if (action == this.actions().size())
                            {
                                this.connexion().ecrire(Connexion.VRAI);
                                // Quitte la session en fermant la boucle de choix.
                                continuer = (new Quitter(this)).executer(this.connexion(), null);
                            }
                            else this.connexion().ecrire(Connexion.FAUX); // Choix incohérent.
                        }
                    }
                    else // Mot de passe incorrect.
                    {
                        Console.afficher(this.parent(), "Tentative de connexion : " + pseudo + " (" + tentatives + '/' + TENTATIVES_MAX + ").");
                        this.connexion().ecrire(Connexion.FAUX);
                    }
                }
            }
            this.connexion().fermer();
        }
        catch (IOException e)
        {
            Console.afficher("| ERREUR | Impossible de lire les informations de la connexion : " + e.getMessage());
        }
        Console.afficher(this.parent(), "Session terminée.");
    }
}
