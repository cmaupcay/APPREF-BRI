package bri.serveur.app.session;

import java.io.IOException;
import java.util.List;

import bri.Connexion;
import bri.serveur.Console;
import bri.serveur.IService;
import bri.serveur.Services;
import bri.serveur.app.session.actions.Quitter;
import bri.serveur.service.IServiceBRI;

/**
 * Session de l'application destinée aux amateurs.
 */
public class SessionAmateur extends Session
{
    @Override
    public void run()
    {
        try 
        {
            List<IService> services_actifs = null;
            IServiceBRI service = null;
            int s;
            boolean continuer = true;
            // Boucle de choix du service a démarré.
            while (continuer)
            {
                // Choix parmis les services actifs.
                services_actifs = Services.services_actifs();
                s = this.connexion().demander_choix(services_actifs.toArray(), "Quel service voulez-vous utiliser ?");
                if (s < services_actifs.size())
                {
                    // Démarrage d'une nouvelle instance du service dans la session.
                    service = services_actifs.get(s).nouvelle_instance(this.connexion());
                    if (service == null) 
                    {
                        this.connexion().ecrire(Connexion.FAUX);
                        this.connexion().ecrire("| ERREUR | Impossible d'ouvrir une nouvelle instance du service.");
                    }
                    else
                    {
                        this.connexion().ecrire(Connexion.VRAI);
                        service.run();
                        service = null;
                    }
                }
                else if (s == services_actifs.size())
                {
                    this.connexion().ecrire(Connexion.VRAI);
                    // Quitte la session en fermant la boucle de choix.
                    continuer = (new Quitter(this)).executer(this.connexion(), null);
                }
                else this.connexion().ecrire(Connexion.FAUX); // Choix incohérent.
            }
            this.connexion().fermer();
        }
        catch (IOException e) 
        { 
            Console.afficher(this.parent(), "| ERREUR | Une erreur est survenue dans une session : " + e.getMessage());
        }
        Console.afficher(this.parent(), "Session terminée.");
    }
}
