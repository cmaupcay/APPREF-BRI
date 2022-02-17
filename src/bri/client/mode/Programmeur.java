package bri.client.mode;

import java.io.IOException;

import bri.client.Connexion;
import bri.client.Console;
import bri.serveur.apps.AppProgrammeur;

public class Programmeur extends Mode
{
    public Programmeur()
    {
        super(AppProgrammeur.NOM, AppProgrammeur.PORT);
    }

    @Override
    public final boolean accepter_connexion(Connexion connexion) 
    {
        final String pseudo = Console.demander("", "Utilisateur : ", false);
        connexion.ecrire(pseudo);
        connexion.ecrire(Console.demander("", "Mot de passe : ", false) + "\n");
        try 
        { 
            if (connexion.lire().equals(Connexion.VRAI))
            {
                Console.afficher("Bienvenue " + pseudo + " !");
                return true;
            }
            else
            {
                Console.afficher("ERREUR : Identifiants incorrects.");
                return this.accepter_connexion(connexion);
            }
        }
        catch (IOException e)
        { return false; }
    }

    @Override
    protected final void charger_actions()
    {
        
    }
}
