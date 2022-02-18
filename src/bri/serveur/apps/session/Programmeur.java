package bri.serveur.apps.session;

import java.io.IOException;
import java.util.InputMismatchException;

import bri.client.Connexion;
import bri.serveur.Console;

public class Programmeur extends Session
{
    private static final int TENTATIVES_MAX = 3;

    private final boolean verifier_pseudo(final String pseudo)
    {
        // TODO Vérification de l'existence d'un utilisateur
        return true;
    }
    private final boolean verifier_mdp(final String pseudo, final String mdp)
    {
        // TODO Vérification du mot de passe d'un utilisateur
        return true;
    }

    // TODO Faire des classes c'est plus simple
    private static String[] ACTIONS = 
    {
        "Ajouter un nouveau service",
        "Mettre à jour un service",
        "Démarrer/Arrêter un service",
        "Supprimer un service",
        "Changer votre adresse FTP",
        "Quitter"
    };

    private final void executer_action(final int action)
    {

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
                while (connexion().ouverte() && tentatives++ < TENTATIVES_MAX)
                {
                    if (this.verifier_mdp(pseudo, this.connexion().demander("Mot de passe : ")))
                    {
                        tentatives = TENTATIVES_MAX;
                        this.connexion().ecrire(Connexion.VRAI);
                        int action;
                        boolean continuer = true;
                        while (this.connexion().ouverte() && continuer)
                        {
                            try
                            {
                                action = this.connexion().demander_choix(ACTIONS, "Que voulez-vous faire ?");
                                if (action < ACTIONS.length)
                                {
                                    this.connexion().ecrire(Connexion.VRAI);
                                    this.executer_action(action);
                                }
                                else this.connexion().ecrire(Connexion.FAUX);
                            }
                            catch (IndexOutOfBoundsException e) {}
                        }
                    }
                    else this.connexion().ecrire(Connexion.FAUX);
                }
                this.connexion().fermer();
            }
        }
        catch (IOException e)
        {
            Console.afficher("ERREUR : Impossible de lire les informations de la connexion.");
        }
        Console.afficher(this.parent(), "Session terminée.");
    }
}
