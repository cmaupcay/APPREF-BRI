package bri.serveur;

import java.util.ArrayList;

import bri.serveur.apps.AppAmateur;
import bri.serveur.apps.AppProgrammeur;
import bri.serveur.utilisateurs.Programmeur;

public class BRILaunch 
{
    private static ArrayList<IApp> apps = new ArrayList<>();

    private static void ajouter_app(IApp app)
    {
        apps.add(app);
        Console.afficher(app, "Application serveur ajoutée !");
    }

    private static final ArrayList<IUtilisateur> utilisateurs = new ArrayList<>();
    public static final  ArrayList<IUtilisateur> utilisateurs() { return utilisateurs; }
    public static boolean ajouter_utilisateur(IUtilisateur utilisateur)
    {
        for (IUtilisateur u : utilisateurs)
            if (u.pseudo().equals(utilisateur.pseudo())) return false;
        utilisateurs.add(utilisateur);
        Console.afficher("Nouvel utilisateur : " + utilisateur.pseudo() + " (" + utilisateur.type() + ").");
        return true;
    }

    public static final void main(String[] args)
    {
        // Ajout des applications
        Console.afficher("Chargement des applications serveurs...");
        ajouter_app(new AppAmateur());
        ajouter_app(new AppProgrammeur());

        ajouter_utilisateur(new Programmeur("admin", "admin", "localhost:21"));

        // Démarrage des applications dans des threads dédiés
        Console.afficher("Démarrage des applications serveurs...");
        for (IApp app : apps) app.demarrer();

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
