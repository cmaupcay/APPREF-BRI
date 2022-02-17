package bri.client.mode.action;

import java.io.IOException;

import bri.client.Connexion;
import bri.client.Console;

public class ActionAmateur extends Action
{
    @Override
    public final String nom() { return "Utiliser un service"; }

    @Override
    public final void executer(Connexion connexion)
    {
        String[] services;
        try { services = connexion.lire_tableau(); }
        catch (IOException e)
        { 
            Console.afficher("ERREUR : Impossible de récupérer la liste des services.");
            return;
        }
        final int service = Console.choisir(services, "Choisissez le service à exécuter : ");
        connexion.ecrire(String.valueOf(service));
        while (connexion.ouverte())
        {

        }
    }
}
