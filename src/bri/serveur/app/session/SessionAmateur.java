package bri.serveur.app.session;

import java.util.ArrayList;

import bri.Connexion;
import bri.serveur.Console;
import bri.serveur.IService;
import bri.serveur.Services;
import bri.serveur.service.IServiceBRI;

public class SessionAmateur extends Session
{
    @Override
    public void run()
    {
        try 
        {
            ArrayList<IService> services_actifs = null;
            IServiceBRI service = null;
            int s;
            boolean continuer = true;
            while (continuer)
            {
                services_actifs = Services.services_actifs();
                s = this.connexion().demander_choix(services_actifs.toArray(), "Quel service voulez-vous utiliser ?");
                if (s < services_actifs.size())
                {
                    service = services_actifs.get(s).nouvelle_instance(this.connexion());
                    if (service != null) 
                    {
                        this.connexion().ecrire(Connexion.VRAI);
                        service.run();
                        this.connexion().ecrire("Au revoir !");
                    }
                    else
                    {
                        this.connexion().ecrire(Connexion.FAUX);
                        this.connexion().ecrire("ERREUR : Impossible d'ouvrir une nouvelle instance du service.");
                    }
                }
                else this.connexion().ecrire(Connexion.FAUX);
            }
        }
        catch (Exception e) { }
        Console.afficher(this.parent(), "Session terminée.");
    }
}
