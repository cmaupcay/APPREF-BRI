package bri.serveur;

import java.util.ArrayList;

public class BRILaunch 
{
    private static ArrayList<IApp> apps = new ArrayList<>();

    private static void demarrer_apps()
    {
        for (IApp app : apps) 
            new Thread(app).start();
    }

    public static final void main(String[] args)
    {
        // Ajout des applications

        // Démarrage des applications dans des threads dédiés
        demarrer_apps();
    }
}
