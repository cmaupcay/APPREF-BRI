package bri.serveur;

import java.util.ArrayList;

import bri.serveur.apps.AppAmateur;
import bri.serveur.apps.AppProgrammeur;

public class BRILaunch 
{
    private static ArrayList<IApp> apps = new ArrayList<>();

    private static void ajouter_app(IApp app)
    {
        apps.add(app);
        Console.afficher(app, "Application serveur ajoutée !");
    }

    public static final void main(String[] args)
    {
        // Ajout des applications
        Console.afficher("Chargement des applications serveurs...");
        ajouter_app(new AppAmateur());
        ajouter_app(new AppProgrammeur());

        // Démarrage des applications dans des threads dédiés
        Console.afficher("Démarrage des applications serveurs...");
        for (IApp app : apps)
            app.demarrer();

        Console.afficher("Serveur BRI démarré.");

        // Attente de la fin des applications
        for (IApp app : apps)
        {
            try { app.thread().join(); }
            catch (InterruptedException e)
            { Console.afficher(app, "ERREUR : L'application a été interrompue."); }
        }

        Console.afficher("Serveur BRI fermé.");
    }
}
