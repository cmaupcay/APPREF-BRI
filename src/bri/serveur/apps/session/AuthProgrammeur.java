package bri.serveur.apps.session;

import java.io.IOException;

import bri.client.Connexion;
import bri.serveur.Console;

public class AuthProgrammeur extends Session
{
    @Override
    public final void run()
    {
        try
        {
            final String pseudo = this.connexion().lire();
            final String mdp = this.connexion().lire();
            if (pseudo.equals("tensaiji") && mdp.equals("ok"))
                this.connexion().ecrire(Connexion.VRAI);
            else this.connexion().ecrire(Connexion.FAUX);
            this.connexion().fermer();
        }
        catch (IOException e)
        {
            Console.afficher("ERREUR : Impossible de lire les informations de la connexion.");
        }
    }
}
