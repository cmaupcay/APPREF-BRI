package bri.serveur.app.session;


import bri.serveur.Console;

public class SessionAmateur extends Session
{
    @Override
    public void run()
    {
        try 
        {
            final int s = this.connexion().demander_choix(new String[]{ "Exemple", "Oui" }, "Choisissez le service à exécuter : ");
            Console.afficher("Choix : " + s);
        }
        catch (Exception e) { }
    }
}