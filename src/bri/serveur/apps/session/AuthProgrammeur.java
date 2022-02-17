package bri.serveur.apps.session;

import java.io.IOException;

import bri.client.Connexion;
import bri.serveur.Console;

public class AuthProgrammeur extends Session
{
    private static final int TENTATIVES_MAX = 3;

    private final boolean verifier_pseudo(final String pseudo)
    {
        return true;
    }
    private final boolean verifier_mdp(final String pseudo, final String mdp)
    {
        return false;
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
                        this.connexion().ecrire(Connexion.VRAI);
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
    }
}
