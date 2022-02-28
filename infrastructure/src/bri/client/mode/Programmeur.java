package bri.client.mode;

import java.io.IOException;

import bri.Connexion;
import bri.client.Console;
import bri.serveur.app.AppProgrammeur;

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
            Console.afficher(connexion.lire());            // Message d'intro
            final String pseudo = repondre_a_demande(connexion);    // Pseudo
            String reponse = connexion.lire();
            if (reponse.equals(Connexion.VRAI))            // Le pseudo existe
            {
                Console.modifier_utilisateur(pseudo);
                while (true)
                {
                    repondre_a_demande(connexion);                  // Mot de passe
                    reponse = connexion.lire();
                    if (reponse.equals(Connexion.VRAI))
                    {
                        Console.afficher("Bienvenue " + pseudo + " !");
                        return true;
                    }
                    else if (reponse.equals(Connexion.FAUX))
                        Console.afficher("ERREUR : Mot de passe incorrect.");
                    else
                    {
                        Console.afficher("ERREUR : Réponse incohérente du serveur : " + reponse);
                        return false;
                    }
                }
            }
            else if (reponse.equals(Connexion.FAUX))                // Connexion refusée
            {
                Console.afficher("ERREUR : Utilisateur inconnu.");
                return this.accepter_connexion(connexion);
            }
            else
            {
                Console.afficher("ERREUR : Réponse incohérente du serveur : " + reponse);
                return false;
            }
        }
        catch (IOException e)
        { return false; }
    }
}
