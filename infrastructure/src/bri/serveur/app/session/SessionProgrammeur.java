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

// TOCOMMENT SessionProg
public class SessionProgrammeur extends Session
{
    private static final int TENTATIVES_MAX = 3;
    private IUtilisateur utilisateur;

    private final boolean verifier_pseudo(final String pseudo)
    {
        List<IUtilisateur> utilisateurs = Utilisateurs.liste();
        for (IUtilisateur u : utilisateurs)
        {
            if (u.getClass() == Programmeur.class && u.pseudo().equals(pseudo))
            {
                this.utilisateur = u;
                return true;
            }
        }
        return false;
    }
    private final boolean verifier_mdp(final String mdp)
    { return this.utilisateur.mdp().equals(mdp); }

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
            final String pseudo = this.connexion().demander("Utilisateur : ");
            if (this.verifier_pseudo(pseudo))
            {
                this.connexion().ecrire(Connexion.VRAI);
                int tentatives = 0;
                while (tentatives++ < TENTATIVES_MAX)
                {
                    if (this.verifier_mdp(this.connexion().demander("Mot de passe : ")))
                    {
                        tentatives = TENTATIVES_MAX;
                        this.connexion().ecrire(Connexion.VRAI);
                        Console.afficher(this.parent(), "Nouvelle connexion : " + pseudo + ".");
                        this.charger_actions();
                        int action;
                        boolean continuer = true;
                        while (continuer)
                        {
                            action = this.connexion().demander_choix(this.actions().toArray(), "Que voulez-vous faire ?");
                            if (action < this.actions().size())
                            {
                                this.connexion().ecrire(Connexion.VRAI);
                                continuer = this.actions().get(action).executer(this.connexion(), new String[]{
                                    pseudo // Les actions de programmeur attendent le pseudo de l'utilisateur en premier argument.
                                });
                            }
                            else if (action == this.actions().size()) // Quitter
                            {
                                this.connexion().ecrire(Connexion.VRAI);
                                continuer = (new Quitter(this)).executer(this.connexion(), null);
                            }
                            else this.connexion().ecrire(Connexion.FAUX);
                        }
                    }
                    else
                    {
                        Console.afficher(this.parent(), "Tentative de connexion : " + pseudo + " (" + tentatives + '/' + TENTATIVES_MAX + ").");
                        this.connexion().ecrire(Connexion.FAUX);
                    }
                }
            }
            else this.connexion().ecrire(Connexion.FAUX); // Le pseudo n'existe pas
            this.connexion().fermer();
        }
        catch (IOException e)
        {
            Console.afficher("ERREUR : Impossible de lire les informations de la connexion.");
        }
        Console.afficher(this.parent(), "Session terminée.");
    }
}
