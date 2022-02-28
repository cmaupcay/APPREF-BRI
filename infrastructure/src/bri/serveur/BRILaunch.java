package bri.serveur;

import java.util.ArrayList;

import bri.serveur.app.AppAmateur;
import bri.serveur.app.AppProgrammeur;
import bri.serveur.utilisateur.Programmeur;

public class BRILaunch 
{
    public static final String VERSION = "alpha";
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

        // Ajout de l'utilisateur par défaut
        final IUtilisateur utilisateur_par_defaut = new Programmeur(Utilisateurs.DEFAUT, Utilisateurs.DEFAUT, Utilisateurs.DEFAUT_FTP);
        Utilisateurs.ajouter(utilisateur_par_defaut);

        // Ajout des services par défaut
        Services.ajouter(utilisateur_par_defaut, "Inversion");
        Services.ajouter(utilisateur_par_defaut, "AnalyseXML");
        Services.ajouter(utilisateur_par_defaut, "Messagerie");

        // Activation des services
        for (IService service : Services.services())
            service.activer();

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
