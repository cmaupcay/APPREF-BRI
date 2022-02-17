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
        try 
        { 
            final String pseudo = repondre_a_demande(connexion);    // Pseudo
            if (connexion.lire().equals(Connexion.VRAI))            // Le pseudo existe
            {
                Console.modifier_utilisateur(pseudo);
                while (connexion.ouverte())
                {
                    repondre_a_demande(connexion);                  // Mot de passe
                    if (connexion.lire().equals(Connexion.VRAI))
                    {
                        Console.afficher("Bienvenue " + pseudo + " !");
                        return true;
                    }
                    else Console.afficher("ERREUR : Mot de passe incorrect.");
                }
                Console.afficher("ERREUR : Vous avez été déconnecté.");
                return false;
            }
            else                                                    // Connexion refusée
            {
                Console.afficher("ERREUR : Utilisateur inconnu.");
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
